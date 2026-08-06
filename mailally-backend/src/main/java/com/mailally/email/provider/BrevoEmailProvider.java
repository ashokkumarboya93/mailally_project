package com.mailally.email.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mailally.email.config.EmailEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Brevo (Sendinblue) API provider implementation of {@link EmailProvider}.
 * Sends transactional and bulk campaign emails using Brevo REST API v3.
 */
@Component
public class BrevoEmailProvider implements EmailProvider {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailProvider.class);
    public static final String PROVIDER_NAME = "BREVO";

    private final EmailEngineConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BrevoEmailProvider(EmailEngineConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public EmailSendResult send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        if (!isAvailable()) {
            log.warn("Attempted to send via Brevo provider, but API key is not configured.");
            return EmailSendResult.fail("Brevo API key not configured", PROVIDER_NAME);
        }

        try {
            String apiUrl = config.getBrevo().getApiUrl();
            if (apiUrl == null || apiUrl.isBlank()) {
                apiUrl = "https://api.brevo.com/v3/smtp/email";
            }

            String senderEmail = (from != null && !from.isBlank()) ? from : config.getDefaultSenderEmail();
            String senderName = (fromName != null && !fromName.isBlank()) ? fromName : config.getDefaultSenderName();

            ObjectNode rootNode = objectMapper.createObjectNode();
            
            // Sender
            ObjectNode senderNode = rootNode.putObject("sender");
            senderNode.put("email", senderEmail);
            if (senderName != null && !senderName.isBlank()) {
                senderNode.put("name", senderName);
            }

            // Recipient
            ArrayNode toArray = rootNode.putArray("to");
            ObjectNode recipientNode = toArray.addObject();
            recipientNode.put("email", to);
            if (toName != null && !toName.isBlank()) {
                recipientNode.put("name", toName);
            }

            // Subject & HTML
            rootNode.put("subject", subject != null ? subject : "");
            rootNode.put("htmlContent", htmlBody != null ? htmlBody : "");

            // ReplyTo
            if (replyTo != null && !replyTo.isBlank()) {
                ObjectNode replyToNode = rootNode.putObject("replyTo");
                replyToNode.put("email", replyTo);
            }

            String jsonPayload = objectMapper.writeValueAsString(rootNode);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("accept", "application/json")
                    .header("api-key", config.getBrevo().getApiKey())
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                String messageId = "BREVO-" + System.currentTimeMillis();
                try {
                    JsonNode respJson = objectMapper.readTree(response.body());
                    if (respJson.has("messageId")) {
                        messageId = respJson.get("messageId").asText();
                    }
                } catch (Exception e) {
                    // Fail-safe extraction
                }
                log.info("Successfully sent email via Brevo API to {} [Message ID: {}]", to, messageId);
                return EmailSendResult.ok(messageId, PROVIDER_NAME);
            } else {
                log.error("Brevo API error response (HTTP {}): {}", response.statusCode(), response.body());
                return EmailSendResult.fail("Brevo API Error (" + response.statusCode() + "): " + response.body(), PROVIDER_NAME);
            }

        } catch (Exception ex) {
            log.error("Failed to send email via Brevo API to {}: {}", to, ex.getMessage(), ex);
            return EmailSendResult.fail("Brevo Send Exception: " + ex.getMessage(), PROVIDER_NAME);
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return config != null && config.getBrevo() != null && config.getBrevo().isConfigured();
    }

    @Override
    public com.mailally.email.dto.ProviderHealthDto health() {
        return com.mailally.email.dto.ProviderHealthDto.builder()
                .providerName(PROVIDER_NAME)
                .available(isAvailable())
                .active(true)
                .statusMessage(isAvailable() ? "Brevo API HTTP client Healthy" : "Brevo API Key Missing")
                .build();
    }

    @Override
    public int quota() {
        return 100; // 100 sends per second maximum rate limit for Brevo REST API
    }

    @Override
    public int batch() {
        return 100; // Chunk size optimal for Brevo
    }

    @Override
    public boolean supportsBulk() {
        return true;
    }

    @Override
    public boolean supportsWebhook() {
        return true;
    }

    @Override
    public boolean supportsTracking() {
        return true;
    }
}

