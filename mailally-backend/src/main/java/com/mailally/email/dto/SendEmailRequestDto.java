package com.mailally.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for sending a single transactional or test email.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendEmailRequestDto {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid recipient email format")
    private String recipientEmail;

    private String recipientName;

    @NotBlank(message = "Email subject is required")
    private String subject;

    @NotBlank(message = "HTML body content is required")
    private String htmlBody;

    private String textBody;
    private String senderName;
    private String senderEmail;
    private String replyTo;
    private String provider;

    public SendEmailRequestDto() {}

    public SendEmailRequestDto(String recipientEmail, String recipientName, String subject, String htmlBody,
                               String textBody, String senderName, String senderEmail, String replyTo, String provider) {
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.subject = subject;
        this.htmlBody = htmlBody;
        this.textBody = textBody;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.replyTo = replyTo;
        this.provider = provider;
    }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getHtmlBody() { return htmlBody; }
    public void setHtmlBody(String htmlBody) { this.htmlBody = htmlBody; }
    public String getTextBody() { return textBody; }
    public void setTextBody(String textBody) { this.textBody = textBody; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
