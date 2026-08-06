package com.mailally.campaign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new Campaign.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCampaignRequestDto {

    @NotBlank(message = "Campaign name is required")
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

    private Long templateId;
    private Long segmentId;

    public CreateCampaignRequestDto() {}

    public CreateCampaignRequestDto(String name, String subject, String senderName, String senderEmail,
                                    String replyTo, Long templateId, Long segmentId) {
        this.name = name;
        this.subject = subject;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.replyTo = replyTo;
        this.templateId = templateId;
        this.segmentId = segmentId;
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
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public Long getSegmentId() { return segmentId; }
    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }
}
