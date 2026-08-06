package com.mailally.notification.dto;

/**
 * Action button metadata associated with a notification item.
 */
public class NotificationActionDto {

    private String label;
    private String url;
    private String method;

    public NotificationActionDto() {}

    public NotificationActionDto(String label, String url, String method) {
        this.label = label;
        this.url = url;
        this.method = method;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}
