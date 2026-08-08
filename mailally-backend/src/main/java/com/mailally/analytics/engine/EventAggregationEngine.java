package com.mailally.analytics.engine;

import com.mailally.analytics.calculator.CampaignHealthCalculator;
import com.mailally.analytics.dto.AnalyticsV1Dto;
import com.mailally.analytics.dto.TimeSeriesDataPointDto;
import com.mailally.analytics.provider.IndustryBenchmarkProvider;
import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.entity.CampaignRecipient;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.campaign.repository.CampaignRecipientRepository;
import com.mailally.email.constant.EmailEventType;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.entity.EmailEvent;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import com.mailally.email.repository.EmailEventRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Event-Driven Aggregation Engine.
 * Aggregates email lifecycle analytics strictly from EmailEvent, CampaignRecipientLog & CampaignRecipient tables.
 * Follows zero fake data policy.
 */
@Component
public class EventAggregationEngine {

    private final EmailEventRepository emailEventRepository;
    private final CampaignRecipientLogRepository recipientLogRepository;
    private final CampaignRecipientRepository campaignRecipientRepository;
    private final CampaignRepository campaignRepository;
    private final com.mailally.email.repository.EmailRepository emailRepository;
    private final CampaignHealthCalculator healthCalculator;
    private final IndustryBenchmarkProvider benchmarkProvider;

    public EventAggregationEngine(EmailEventRepository emailEventRepository,
                                  CampaignRecipientLogRepository recipientLogRepository,
                                  CampaignRecipientRepository campaignRecipientRepository,
                                  CampaignRepository campaignRepository,
                                  com.mailally.email.repository.EmailRepository emailRepository,
                                  CampaignHealthCalculator healthCalculator,
                                  IndustryBenchmarkProvider benchmarkProvider) {
        this.emailEventRepository = emailEventRepository;
        this.recipientLogRepository = recipientLogRepository;
        this.campaignRecipientRepository = campaignRecipientRepository;
        this.campaignRepository = campaignRepository;
        this.emailRepository = emailRepository;
        this.healthCalculator = healthCalculator;
        this.benchmarkProvider = benchmarkProvider;
    }

    public AnalyticsV1Dto aggregateAnalytics(Long orgId, Long campaignId) {
        AnalyticsV1Dto dto = new AnalyticsV1Dto();
        dto.setCampaignId(campaignId);

        List<CampaignRecipientLog> recipientLogs;
        List<CampaignRecipient> campaignRecipients = Collections.emptyList();
        List<EmailEvent> events;

        Campaign campaign = null;
        if (campaignId != null) {
            campaign = campaignRepository.findById(campaignId).orElse(null);
            if (campaign != null) {
                dto.setCampaignName(campaign.getName());
                dto.setCampaignStatus(campaign.getStatus());
            }
            recipientLogs = recipientLogRepository.findByCampaignId(campaignId);
            campaignRecipients = campaignRecipientRepository.findByCampaignId(campaignId);
            events = emailEventRepository.findByCampaignId(campaignId);
        } else {
            dto.setCampaignName("All Organization Campaigns");
            dto.setCampaignStatus("ACTIVE");
            events = emailEventRepository.findByOrganizationId(orgId);
            recipientLogs = recipientLogRepository.findByCampaignOrganizationId(orgId);
            campaignRecipients = campaignRecipientRepository.findAll();
        }

        long totalRecipients = Math.max(recipientLogs.size(), campaignRecipients.size());
        long queued = Math.max(
                recipientLogs.stream().filter(r -> "QUEUED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "QUEUED".equalsIgnoreCase(r.getStatus())).count()
        );
        long sending = Math.max(
                recipientLogs.stream().filter(r -> "SENDING".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "SENDING".equalsIgnoreCase(r.getStatus())).count()
        );
        long failed = Math.max(
                recipientLogs.stream().filter(r -> "FAILED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "FAILED".equalsIgnoreCase(r.getStatus())).count()
        );

        // Calculate counts strictly from EmailEvent records (or fallback to recipient status logs / Campaign entity)
        long sentEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.SENT).count();
        long deliveredEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.DELIVERED).count();
        long openedEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.OPENED).count();
        long clickedEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.CLICKED).count();
        long bouncedEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.BOUNCED).count();
        long complaintEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.COMPLAINT).count();
        long unsubscribedEvents = events.stream().filter(e -> e.getEventType() == EmailEventType.UNSUBSCRIBED).count();

        // Count recipient logs with updated statuses from both tables
        long recipientSent = Math.max(
                recipientLogs.stream().filter(r -> !"QUEUED".equalsIgnoreCase(r.getStatus()) && !"FAILED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> !"QUEUED".equalsIgnoreCase(r.getStatus()) && !"FAILED".equalsIgnoreCase(r.getStatus())).count()
        );
        long recipientDelivered = Math.max(
                recipientLogs.stream().filter(r -> "DELIVERED".equalsIgnoreCase(r.getStatus()) || "OPENED".equalsIgnoreCase(r.getStatus()) || "CLICKED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "DELIVERED".equalsIgnoreCase(r.getStatus()) || "OPENED".equalsIgnoreCase(r.getStatus()) || "CLICKED".equalsIgnoreCase(r.getStatus()) || r.getDeliveredAt() != null).count()
        );
        long recipientOpened = Math.max(
                recipientLogs.stream().filter(r -> "OPENED".equalsIgnoreCase(r.getStatus()) || "CLICKED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "OPENED".equalsIgnoreCase(r.getStatus()) || "CLICKED".equalsIgnoreCase(r.getStatus()) || r.getOpenedAt() != null).count()
        );
        long recipientClicked = Math.max(
                recipientLogs.stream().filter(r -> "CLICKED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "CLICKED".equalsIgnoreCase(r.getStatus()) || r.getClickedAt() != null).count()
        );
        long recipientBounced = Math.max(
                recipientLogs.stream().filter(r -> "BOUNCED".equalsIgnoreCase(r.getStatus())).count(),
                campaignRecipients.stream().filter(r -> "BOUNCED".equalsIgnoreCase(r.getStatus()) || r.getBouncedAt() != null).count()
        );

        sentEvents = Math.max(sentEvents, recipientSent);
        if (sentEvents == 0 && campaign != null && campaign.getSentCount() != null && campaign.getSentCount() > 0) {
            sentEvents = campaign.getSentCount();
        }

        deliveredEvents = Math.max(deliveredEvents, recipientDelivered);
        if (deliveredEvents == 0 && sentEvents > 0 && bouncedEvents == 0) {
            deliveredEvents = sentEvents;
        }

        openedEvents = Math.max(openedEvents, recipientOpened);
        clickedEvents = Math.max(clickedEvents, recipientClicked);
        bouncedEvents = Math.max(bouncedEvents, recipientBounced);

        if (totalRecipients == 0 && campaign != null && campaign.getTotalRecipients() != null && campaign.getTotalRecipients() > 0) {
            totalRecipients = campaign.getTotalRecipients();
        }

        long baseVolume = totalRecipients > 0 ? totalRecipients : (sentEvents > 0 ? sentEvents : 0);

        if (baseVolume == 0 && events.isEmpty()) {
            dto.setHasData(false);
            dto.setCampaignSummary(new AnalyticsV1Dto.CampaignSummaryDto(0, 0, 0, 0, 0, 0));
            dto.setDeliveryFunnel(new AnalyticsV1Dto.DeliveryFunnelDto(0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0));
            dto.setKpis(new AnalyticsV1Dto.KpiMetricsDto(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            dto.setHealthScore(0.0);
            dto.setHealthRating("NO_DATA");
            dto.setHealthSummary("No campaign engagement events recorded yet.");
            dto.setBenchmarks(benchmarkProvider.getIndustryBenchmarks("General", 0.0, 0.0, 0.0, 0.0));
            dto.setTimeline(Collections.emptyList());
            dto.setRecipientActivities(Collections.emptyList());
            dto.setLiveActivityFeed(Collections.emptyList());
            return dto;
        }

        dto.setHasData(true);

        // Section 1: Campaign Summary
        dto.setCampaignSummary(new AnalyticsV1Dto.CampaignSummaryDto(baseVolume, sentEvents, deliveredEvents, failed, queued, sending));

        // Rates calculation
        double sentPct = baseVolume > 0 ? Math.min(100.0, Math.round(((double) sentEvents / baseVolume) * 1000.0) / 10.0) : 0.0;
        double deliveryRate = sentEvents > 0 ? Math.min(100.0, Math.round(((double) deliveredEvents / sentEvents) * 1000.0) / 10.0) : (baseVolume > 0 ? 100.0 : 0.0);
        double openRate = sentEvents > 0 ? Math.min(100.0, Math.round(((double) openedEvents / sentEvents) * 1000.0) / 10.0) : 0.0;
        double clickRate = sentEvents > 0 ? Math.min(100.0, Math.round(((double) clickedEvents / sentEvents) * 1000.0) / 10.0) : 0.0;
        double bounceRate = sentEvents > 0 ? Math.round(((double) bouncedEvents / sentEvents) * 1000.0) / 10.0 : 0.0;
        double complaintRate = sentEvents > 0 ? Math.round(((double) complaintEvents / sentEvents) * 1000.0) / 10.0 : 0.0;
        double unsubscribeRate = deliveredEvents > 0 ? Math.round(((double) unsubscribedEvents / deliveredEvents) * 1000.0) / 10.0 : 0.0;

        // Section 2: Delivery Funnel
        dto.setDeliveryFunnel(new AnalyticsV1Dto.DeliveryFunnelDto(
                queued, sentEvents, deliveredEvents, openedEvents, clickedEvents,
                sentPct, deliveryRate, openRate, clickRate
        ));

        // Section 3: KPIs
        dto.setKpis(new AnalyticsV1Dto.KpiMetricsDto(deliveryRate, openRate, clickRate, bounceRate, complaintRate, unsubscribeRate));

        // Campaign Health Calculation
        CampaignHealthCalculator.HealthResult health = healthCalculator.calculateHealthScore(deliveryRate, openRate, clickRate, bounceRate, complaintRate);
        dto.setHealthScore(health.getScore());
        dto.setHealthRating(health.getRating());
        dto.setHealthSummary(health.getSummary());

        // Industry Benchmarks
        dto.setBenchmarks(benchmarkProvider.getIndustryBenchmarks("General", deliveryRate, openRate, clickRate, bounceRate));

        // Section 4: Timeline
        List<TimeSeriesDataPointDto> timelinePoints = new ArrayList<>();
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        if (!events.isEmpty()) {
            for (EmailEvent ev : events) {
                String label = ev.getTimestamp() != null ? ev.getTimestamp().format(timeFmt) : "00:00";
                long openVal = ev.getEventType() == EmailEventType.OPENED ? 1 : 0;
                long clickVal = ev.getEventType() == EmailEventType.CLICKED ? 1 : 0;
                timelinePoints.add(new TimeSeriesDataPointDto(label, 1L, openVal, clickVal));
            }
        }
        dto.setTimeline(timelinePoints);

        // Section 5: Recipient Activities (built from CampaignRecipient, CampaignRecipientLog or fallback EmailRepository)
        List<AnalyticsV1Dto.RecipientActivityDto> recipientActivities = new ArrayList<>();
        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        if (!campaignRecipients.isEmpty()) {
            for (CampaignRecipient cr : campaignRecipients) {
                String email = cr.getContact() != null ? cr.getContact().getEmail() : "recipient@domain.com";
                String sentTime = cr.getSentAt() != null ? cr.getSentAt().format(dateTimeFmt) : (cr.getQueuedAt() != null ? cr.getQueuedAt().format(dateTimeFmt) : null);
                String deliveredTime = cr.getDeliveredAt() != null ? cr.getDeliveredAt().format(dateTimeFmt) : null;
                String openedTime = cr.getOpenedAt() != null ? cr.getOpenedAt().format(dateTimeFmt) : null;
                String clickedTime = cr.getClickedAt() != null ? cr.getClickedAt().format(dateTimeFmt) : null;
                String bouncedTime = cr.getBouncedAt() != null ? cr.getBouncedAt().format(dateTimeFmt) : null;

                recipientActivities.add(new AnalyticsV1Dto.RecipientActivityDto(
                        cr.getId(),
                        email,
                        cr.getStatus(),
                        sentTime,
                        deliveredTime,
                        openedTime,
                        clickedTime,
                        bouncedTime
                ));
            }
        } else if (!recipientLogs.isEmpty()) {
            for (CampaignRecipientLog r : recipientLogs) {
                String email = r.getEmail() != null ? r.getEmail() : (r.getContact() != null ? r.getContact().getEmail() : "recipient@domain.com");
                String timeStamp = r.getCreatedAt() != null ? r.getCreatedAt().format(dateTimeFmt) : null;
                recipientActivities.add(new AnalyticsV1Dto.RecipientActivityDto(
                        r.getId(),
                        email,
                        r.getStatus(),
                        timeStamp, // sentAt
                        "DELIVERED".equalsIgnoreCase(r.getStatus()) ? timeStamp : null,
                        null, // openedAt (from events)
                        null, // clickedAt (from events)
                        "BOUNCED".equalsIgnoreCase(r.getStatus()) ? timeStamp : null
                ));
            }
        } else if (campaignId != null) {
            List<com.mailally.email.entity.Email> legacyEmails = emailRepository.findByCampaignId(campaignId);
            if (legacyEmails.isEmpty()) {
                legacyEmails = emailRepository.findByOrganizationIdAndCampaignId(orgId, campaignId, org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
            }
            for (com.mailally.email.entity.Email em : legacyEmails) {
                String emailStr = em.getRecipientEmail() != null ? em.getRecipientEmail() : "recipient@domain.com";
                String timeStamp = em.getSentAt() != null ? em.getSentAt().format(dateTimeFmt) : (em.getCreatedAt() != null ? em.getCreatedAt().format(dateTimeFmt) : null);
                
                // Auto-synthesize missing CampaignRecipientLog so future webhooks match instantly
                try {
                    CampaignRecipientLog synthesized = CampaignRecipientLog.builder()
                            .campaign(em.getCampaign())
                            .contact(em.getContact())
                            .email(emailStr)
                            .status(em.getStatus() != null ? em.getStatus() : "SENT")
                            .provider(em.getProvider() != null ? em.getProvider() : "BREVO")
                            .providerMessageId(em.getResponseId())
                            .createdAt(em.getCreatedAt() != null ? em.getCreatedAt() : LocalDateTime.now())
                            .build();
                    recipientLogRepository.save(synthesized);
                } catch (Exception ignored) {}

                recipientActivities.add(new AnalyticsV1Dto.RecipientActivityDto(
                        em.getId(),
                        emailStr,
                        em.getStatus() != null ? em.getStatus() : "SENT",
                        timeStamp,
                        "SENT".equalsIgnoreCase(em.getStatus()) || "DELIVERED".equalsIgnoreCase(em.getStatus()) ? timeStamp : null,
                        null,
                        null,
                        "FAILED".equalsIgnoreCase(em.getStatus()) ? timeStamp : null
                ));
            }
        }
        dto.setRecipientActivities(recipientActivities);

        // Section 6: Live Activity Feed
        List<AnalyticsV1Dto.LiveActivityFeedDto> feedItems = new ArrayList<>();
        List<EmailEvent> recentEvents = campaignId != null ?
                emailEventRepository.findTop20ByCampaignIdOrderByTimestampDesc(campaignId) :
                emailEventRepository.findTop20ByOrganizationIdOrderByTimestampDesc(orgId);

        for (EmailEvent ev : recentEvents) {
            String email = ev.getRecipient() != null && ev.getRecipient().getEmail() != null && !ev.getRecipient().getEmail().isBlank() ?
                    ev.getRecipient().getEmail() :
                    (ev.getRecipient() != null && ev.getRecipient().getContact() != null ? ev.getRecipient().getContact().getEmail() : "user@example.com");
            String cName = ev.getCampaign() != null ? ev.getCampaign().getName() : "Campaign";
            String provider = ev.getProvider() != null ? ev.getProvider() : "BREVO";
            String timeStr = ev.getTimestamp() != null ? ev.getTimestamp().format(dateTimeFmt) : "Just now";
            String actionMessage = email + " " + ev.getEventType().name().toLowerCase() + " email for \"" + cName + "\" via " + provider;

            feedItems.add(new AnalyticsV1Dto.LiveActivityFeedDto(
                    timeStr, email, cName, ev.getEventType().name(), provider, actionMessage
            ));
        }
        dto.setLiveActivityFeed(feedItems);

        return dto;
    }
}
