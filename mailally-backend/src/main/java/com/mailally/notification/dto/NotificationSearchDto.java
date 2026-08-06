package com.mailally.notification.dto;

/**
 * Search parameter DTO.
 */
public class NotificationSearchDto {

    private String query;

    public NotificationSearchDto() {}

    public NotificationSearchDto(String query) {
        this.query = query;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}
