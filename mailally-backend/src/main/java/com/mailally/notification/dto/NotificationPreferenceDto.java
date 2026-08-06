package com.mailally.notification.dto;

/**
 * User channel preferences DTO.
 */
public class NotificationPreferenceDto {

    private boolean enableInApp;
    private boolean enableEmail;
    private boolean enableSms;
    private boolean enablePush;
    private boolean enableSlack;

    public NotificationPreferenceDto() {}

    public NotificationPreferenceDto(boolean enableInApp, boolean enableEmail, boolean enableSms,
                                    boolean enablePush, boolean enableSlack) {
        this.enableInApp = enableInApp;
        this.enableEmail = enableEmail;
        this.enableSms = enableSms;
        this.enablePush = enablePush;
        this.enableSlack = enableSlack;
    }

    public boolean isEnableInApp() { return enableInApp; }
    public void setEnableInApp(boolean enableInApp) { this.enableInApp = enableInApp; }
    public boolean isEnableEmail() { return enableEmail; }
    public void setEnableEmail(boolean enableEmail) { this.enableEmail = enableEmail; }
    public boolean isEnableSms() { return enableSms; }
    public void setEnableSms(boolean enableSms) { this.enableSms = enableSms; }
    public boolean isEnablePush() { return enablePush; }
    public void setEnablePush(boolean enablePush) { this.enablePush = enablePush; }
    public boolean isEnableSlack() { return enableSlack; }
    public void setEnableSlack(boolean enableSlack) { this.enableSlack = enableSlack; }
}
