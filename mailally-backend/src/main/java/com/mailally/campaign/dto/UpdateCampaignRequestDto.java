package com.mailally.campaign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing Campaign.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCampaignRequestDto {

    @Size(max = 200, message = "Campaign name must not exceed 200 characters")
    private String name;

    @Size(max = 500, message = "Subject must not exceed 500 characters")
    private String subject;

    @Size(max = 150, message = "Sender name must not exceed 150 characters")
    private String senderName;

    @Email(message = "Invalid sender email format")
    @Size(max = 255, message = "Sender email must not exceed 255 characters")
    private String senderEmail;

    @Email(message = "Invalid reply-to email format")
    @Size(max = 255, message = "Reply-to email must not exceed 255 characters")
    private String replyTo;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    public UpdateCampaignRequestDto() {}

    public UpdateCampaignRequestDto(String name, String subject, String senderName, String senderEmail,
                                    String replyTo, String status) {
        this.name = name;
        this.subject = subject;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.replyTo = replyTo;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
