package com.mailally;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MailallyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailallyBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner logExistingCampaigns(
			com.mailally.campaign.repository.CampaignRepository campaignRepository,
			com.mailally.email.repository.EmailEventRepository emailEventRepository,
			com.mailally.email.repository.CampaignRecipientLogRepository recipientLogRepository,
			com.mailally.email.repository.EmailRepository emailRepository) {
		return args -> {
			runStartupRepair(campaignRepository, emailEventRepository, recipientLogRepository, emailRepository);
		};
	}

	@org.springframework.transaction.annotation.Transactional
	public void runStartupRepair(
			com.mailally.campaign.repository.CampaignRepository campaignRepository,
			com.mailally.email.repository.EmailEventRepository emailEventRepository,
			com.mailally.email.repository.CampaignRecipientLogRepository recipientLogRepository,
			com.mailally.email.repository.EmailRepository emailRepository) {
		// Clean up any leftover test simulation rows
		List<com.mailally.email.entity.EmailEvent> simEvents = emailEventRepository.findAll().stream()
				.filter(e -> e.getProviderMessageId() != null && e.getProviderMessageId().startsWith("TEST-SIM-"))
				.toList();
		if (!simEvents.isEmpty()) {
			emailEventRepository.deleteAll(simEvents);
		}

		List<com.mailally.email.entity.CampaignRecipientLog> simRecipients = recipientLogRepository.findAll().stream()
				.filter(r -> r.getProviderMessageId() != null && r.getProviderMessageId().startsWith("TEST-SIM-"))
				.toList();
		if (!simRecipients.isEmpty()) {
			recipientLogRepository.deleteAll(simRecipients);
		}

		List<com.mailally.campaign.entity.Campaign> campaigns = campaignRepository.findAll();
		System.out.println("==================================================");
		System.out.println("REAL DATABASE CAMPAIGNS COUNT: " + campaigns.size());
		for (com.mailally.campaign.entity.Campaign c : campaigns) {
			System.out.println("-> ID: " + c.getId() + " | Name: " + c.getName() + " | Status: " + c.getStatus() + " | Sent: " + c.getSentCount() + " | Total: " + c.getTotalRecipients());
		}
		System.out.println("==================================================");

		// Retroactively synthesize missing CampaignRecipientLog entries from legacy Email table
		List<com.mailally.email.entity.Email> legacyEmails = emailRepository.findAll().stream()
				.filter(e -> e.getCampaign() != null && e.getRecipientEmail() != null)
				.toList();
		for (com.mailally.email.entity.Email emailLog : legacyEmails) {
			var recOpt = recipientLogRepository.findFirstByEmailOrderByCreatedAtDesc(emailLog.getRecipientEmail());
			if (recOpt.isEmpty()) {
				var newRec = com.mailally.email.entity.CampaignRecipientLog.builder()
						.campaign(emailLog.getCampaign())
						.contact(emailLog.getContact())
						.email(emailLog.getRecipientEmail())
						.status(emailLog.getStatus() != null ? emailLog.getStatus() : "SENT")
						.provider(emailLog.getProvider() != null ? emailLog.getProvider() : "BREVO")
						.providerMessageId(emailLog.getResponseId())
						.createdAt(emailLog.getCreatedAt() != null ? emailLog.getCreatedAt() : java.time.LocalDateTime.now())
						.build();
				recipientLogRepository.save(newRec);
				System.out.println("SYNTHESIZED RECIPIENT LOG FOR CAMPAIGN ID " + emailLog.getCampaign().getId() + " AND EMAIL " + emailLog.getRecipientEmail());
			}
		}

		// Retroactively repair any unlinked email events from webhooks
		List<com.mailally.email.entity.EmailEvent> unlinkedEvents = emailEventRepository.findAll().stream()
				.filter(e -> e.getCampaign() == null)
				.toList();
		for (com.mailally.email.entity.EmailEvent event : unlinkedEvents) {
			String meta = event.getMetadata();
			if (meta != null && meta.contains("email=")) {
				String email = null;
				int idx = meta.indexOf("email=");
				if (idx != -1) {
					int end = meta.indexOf(",", idx);
					if (end == -1) end = meta.indexOf("}", idx);
					if (end != -1) email = meta.substring(idx + 6, end).replace("}", "").trim();
				}
				if (email != null && !email.isBlank()) {
					var recipientOpt = recipientLogRepository.findFirstByEmailOrderByCreatedAtDesc(email);
					if (recipientOpt.isPresent()) {
						var rec = recipientOpt.get();
						event.setRecipient(rec);
						event.setCampaign(rec.getCampaign());
						rec.setStatus(event.getEventType().name());
						recipientLogRepository.save(rec);
						emailEventRepository.save(event);
						System.out.println("RETROACTIVELY LINKED EVENT " + event.getEventType() + " TO EMAIL " + email + " AND CAMPAIGN ID " + rec.getCampaign().getId());
					}
				}
			}
		}
	}
}

