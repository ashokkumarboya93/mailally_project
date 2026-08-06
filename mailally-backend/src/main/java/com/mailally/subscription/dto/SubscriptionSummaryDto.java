package com.mailally.subscription.dto;

/**
 * Summary view of organization subscription status and limits.
 */
public class SubscriptionSummaryDto {

    private String planCode;
    private String planName;
    private String status;
    private int contactLimit;
    private int emailLimit;
    private int userLimit;
    private int campaignLimit;

    public SubscriptionSummaryDto() {}

    public SubscriptionSummaryDto(String planCode, String planName, String status, int contactLimit,
                                  int emailLimit, int userLimit, int campaignLimit) {
        this.planCode = planCode;
        this.planName = planName;
        this.status = status;
        this.contactLimit = contactLimit;
        this.emailLimit = emailLimit;
        this.userLimit = userLimit;
        this.campaignLimit = campaignLimit;
    }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getContactLimit() { return contactLimit; }
    public void setContactLimit(int contactLimit) { this.contactLimit = contactLimit; }
    public int getEmailLimit() { return emailLimit; }
    public void setEmailLimit(int emailLimit) { this.emailLimit = emailLimit; }
    public int getUserLimit() { return userLimit; }
    public void setUserLimit(int userLimit) { this.userLimit = userLimit; }
    public int getCampaignLimit() { return campaignLimit; }
    public void setCampaignLimit(int campaignLimit) { this.campaignLimit = campaignLimit; }
}
