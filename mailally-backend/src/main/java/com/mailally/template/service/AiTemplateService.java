package com.mailally.template.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiTemplateService {

    public Map<String, Object> generateTemplateWithAi(String campaignGoal, String audience, String tone, String language, String cta) {
        Map<String, Object> result = new HashMap<>();

        String safeGoal = campaignGoal != null && !campaignGoal.isEmpty() ? campaignGoal : "Exclusive Offer";
        String safeAudience = audience != null && !audience.isEmpty() ? audience : "Valued Customers";
        String safeTone = tone != null ? tone.toLowerCase() : "professional";
        String safeCta = cta != null && !cta.isEmpty() ? cta : "Claim Offer Now";

        String subject = "Exclusive " + safeGoal + " for " + safeAudience;
        String preheader = "Special announcement for " + safeAudience + ". Open inside to view your offer.";

        StringBuilder htmlBody = new StringBuilder();
        htmlBody.append("<div style=\"font-family: 'Inter', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; color: #1f2937; background-color: #ffffff;\">\n");
        htmlBody.append("  <h2 style=\"color: #111827;\">Hello {{firstName}},</h2>\n");
        htmlBody.append("  <p style=\"font-size: 16px; line-height: 1.6;\">We are thrilled to bring you an exclusive update tailored for <strong>{{company}}</strong>. Our latest ")
                .append(safeGoal).append(" is designed specifically for your team in {{city}}, {{country}}.</p>\n");

        if ("promotional".equals(safeTone) || "urgent".equals(safeTone)) {
            htmlBody.append("  <div style=\"background-color: #eff6ff; border-left: 4px solid #2563eb; padding: 16px; margin: 20px 0; border-radius: 4px;\">\n");
            htmlBody.append("    <p style=\"margin: 0; font-weight: 600; color: #1e40af;\">Limited Time Opportunity for ").append(safeAudience).append("!</p>\n");
            htmlBody.append("  </div>\n");
        }

        htmlBody.append("  <p style=\"font-size: 15px; line-height: 1.6;\">Discover how our solutions can empower your operations today.</p>\n");
        htmlBody.append("  <div style=\"text-align: center; margin: 32px 0;\">\n");
        htmlBody.append("    <a href=\"https://mailally.com/cta\" style=\"background-color: #2563eb; color: #ffffff; padding: 14px 28px; font-weight: 600; text-decoration: none; border-radius: 6px; display: inline-block;\">")
                .append(safeCta).append("</a>\n");
        htmlBody.append("  </div>\n");
        htmlBody.append("  <hr style=\"border: none; border-top: 1px solid #e5e7eb; margin: 32px 0;\" />\n");
        htmlBody.append("  <p style=\"font-size: 12px; color: #6b7280; text-align: center;\">Sent via MailAlly Enterprise | <a href=\"{{unsubscribeLink}}\" style=\"color: #6b7280;\">Unsubscribe</a></p>\n");
        htmlBody.append("</div>");

        result.put("subject", subject);
        result.put("preheader", preheader);
        result.put("htmlContent", htmlBody.toString());
        result.put("spamScore", 0.05); // Low spam risk
        result.put("recommendedTone", safeTone);

        return result;
    }
}
