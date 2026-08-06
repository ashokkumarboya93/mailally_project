package com.mailally.settings.dto;

/**
 * Notification channel settings DTO.
 */
public class NotificationSettingsDto {

    private boolean enableEmailNotifications;
    private boolean enableSystemNotifications;
    private boolean enableCampaignNotifications;
    private boolean enableSchedulerNotifications;

    public NotificationSettingsDto() {}

    public NotificationSettingsDto(boolean enableEmailNotifications, boolean enableSystemNotifications,
                                   boolean enableCampaignNotifications, boolean enableSchedulerNotifications) {
        this.enableEmailNotifications = enableEmailNotifications;
        this.enableSystemNotifications = enableSystemNotifications;
        this.enableCampaignNotifications = enableCampaignNotifications;
        this.enableSchedulerNotifications = enableSchedulerNotifications;
    }

    public boolean isEnableEmailNotifications() { return enableEmailNotifications; }
    public void setEnableEmailNotifications(boolean enableEmailNotifications) { this.enableEmailNotifications = enableEmailNotifications; }
    public boolean isEnableSystemNotifications() { return enableSystemNotifications; }
    public void setEnableSystemNotifications(boolean enableSystemNotifications) { this.enableSystemNotifications = enableSystemNotifications; }
    public boolean isEnableCampaignNotifications() { return enableCampaignNotifications; }
    public void setEnableCampaignNotifications(boolean enableCampaignNotifications) { this.enableCampaignNotifications = enableCampaignNotifications; }
    public boolean isEnableSchedulerNotifications() { return enableSchedulerNotifications; }
    public void setEnableSchedulerNotifications(boolean enableSchedulerNotifications) { this.enableSchedulerNotifications = enableSchedulerNotifications; }
}
