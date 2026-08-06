package com.mailally.audit.dto;

import java.util.Map;

/**
 * Audit log metrics summary DTO.
 */
public class AuditSummaryDto {

    private long totalEvents;
    private long successfulEvents;
    private long failedEvents;
    private Map<String, Long> eventsByModule;

    public AuditSummaryDto() {}

    public AuditSummaryDto(long totalEvents, long successfulEvents, long failedEvents, Map<String, Long> eventsByModule) {
        this.totalEvents = totalEvents;
        this.successfulEvents = successfulEvents;
        this.failedEvents = failedEvents;
        this.eventsByModule = eventsByModule;
    }

    public long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }
    public long getSuccessfulEvents() { return successfulEvents; }
    public void setSuccessfulEvents(long successfulEvents) { this.successfulEvents = successfulEvents; }
    public long getFailedEvents() { return failedEvents; }
    public void setFailedEvents(long failedEvents) { this.failedEvents = failedEvents; }
    public Map<String, Long> getEventsByModule() { return eventsByModule; }
    public void setEventsByModule(Map<String, Long> eventsByModule) { this.eventsByModule = eventsByModule; }
}
