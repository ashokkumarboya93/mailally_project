package com.mailally.dashboard.dto;

/**
 * Top KPI Cards DTO (total/running/completed/failed campaigns, email counts, contact counts).
 */
public class DashboardKpiDto {

    private long totalCampaigns;
    private long activeCampaigns;
    private long completedCampaigns;
    private long draftCampaigns;
    private long scheduledCampaigns;
    private long runningCampaigns;
    private long failedCampaigns;
    private long cancelledCampaigns;
    private long totalEmailsSent;
    private long deliveredEmails;
    private long pendingEmails;
    private long failedEmails;
    private long queuedEmails;
    private long retryEmails;
    private long totalContacts;
    private long subscribedContacts;
    private long unsubscribedContacts;
    private long bouncedContacts;
    private long totalSegments;
    private long totalTemplates;
    private long totalUsers;

    public DashboardKpiDto() {}

    public DashboardKpiDto(long totalCampaigns, long activeCampaigns, long completedCampaigns, long draftCampaigns,
                           long scheduledCampaigns, long runningCampaigns, long failedCampaigns, long cancelledCampaigns,
                           long totalEmailsSent, long deliveredEmails, long pendingEmails, long failedEmails,
                           long queuedEmails, long retryEmails, long totalContacts, long subscribedContacts,
                           long unsubscribedContacts, long bouncedContacts, long totalSegments,
                           long totalTemplates, long totalUsers) {
        this.totalCampaigns = totalCampaigns;
        this.activeCampaigns = activeCampaigns;
        this.completedCampaigns = completedCampaigns;
        this.draftCampaigns = draftCampaigns;
        this.scheduledCampaigns = scheduledCampaigns;
        this.runningCampaigns = runningCampaigns;
        this.failedCampaigns = failedCampaigns;
        this.cancelledCampaigns = cancelledCampaigns;
        this.totalEmailsSent = totalEmailsSent;
        this.deliveredEmails = deliveredEmails;
        this.pendingEmails = pendingEmails;
        this.failedEmails = failedEmails;
        this.queuedEmails = queuedEmails;
        this.retryEmails = retryEmails;
        this.totalContacts = totalContacts;
        this.subscribedContacts = subscribedContacts;
        this.unsubscribedContacts = unsubscribedContacts;
        this.bouncedContacts = bouncedContacts;
        this.totalSegments = totalSegments;
        this.totalTemplates = totalTemplates;
        this.totalUsers = totalUsers;
    }

    public long getTotalCampaigns() { return totalCampaigns; }
    public void setTotalCampaigns(long totalCampaigns) { this.totalCampaigns = totalCampaigns; }
    public long getActiveCampaigns() { return activeCampaigns; }
    public void setActiveCampaigns(long activeCampaigns) { this.activeCampaigns = activeCampaigns; }
    public long getCompletedCampaigns() { return completedCampaigns; }
    public void setCompletedCampaigns(long completedCampaigns) { this.completedCampaigns = completedCampaigns; }
    public long getDraftCampaigns() { return draftCampaigns; }
    public void setDraftCampaigns(long draftCampaigns) { this.draftCampaigns = draftCampaigns; }
    public long getScheduledCampaigns() { return scheduledCampaigns; }
    public void setScheduledCampaigns(long scheduledCampaigns) { this.scheduledCampaigns = scheduledCampaigns; }
    public long getRunningCampaigns() { return runningCampaigns; }
    public void setRunningCampaigns(long runningCampaigns) { this.runningCampaigns = runningCampaigns; }
    public long getFailedCampaigns() { return failedCampaigns; }
    public void setFailedCampaigns(long failedCampaigns) { this.failedCampaigns = failedCampaigns; }
    public long getCancelledCampaigns() { return cancelledCampaigns; }
    public void setCancelledCampaigns(long cancelledCampaigns) { this.cancelledCampaigns = cancelledCampaigns; }
    public long getTotalEmailsSent() { return totalEmailsSent; }
    public void setTotalEmailsSent(long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; }
    public long getDeliveredEmails() { return deliveredEmails; }
    public void setDeliveredEmails(long deliveredEmails) { this.deliveredEmails = deliveredEmails; }
    public long getPendingEmails() { return pendingEmails; }
    public void setPendingEmails(long pendingEmails) { this.pendingEmails = pendingEmails; }
    public long getFailedEmails() { return failedEmails; }
    public void setFailedEmails(long failedEmails) { this.failedEmails = failedEmails; }
    public long getQueuedEmails() { return queuedEmails; }
    public void setQueuedEmails(long queuedEmails) { this.queuedEmails = queuedEmails; }
    public long getRetryEmails() { return retryEmails; }
    public void setRetryEmails(long retryEmails) { this.retryEmails = retryEmails; }
    public long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(long totalContacts) { this.totalContacts = totalContacts; }
    public long getSubscribedContacts() { return subscribedContacts; }
    public void setSubscribedContacts(long subscribedContacts) { this.subscribedContacts = subscribedContacts; }
    public long getUnsubscribedContacts() { return unsubscribedContacts; }
    public void setUnsubscribedContacts(long unsubscribedContacts) { this.unsubscribedContacts = unsubscribedContacts; }
    public long getBouncedContacts() { return bouncedContacts; }
    public void setBouncedContacts(long bouncedContacts) { this.bouncedContacts = bouncedContacts; }
    public long getTotalSegments() { return totalSegments; }
    public void setTotalSegments(long totalSegments) { this.totalSegments = totalSegments; }
    public long getTotalTemplates() { return totalTemplates; }
    public void setTotalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; }
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public static DashboardKpiDtoBuilder builder() { return new DashboardKpiDtoBuilder(); }

    public static class DashboardKpiDtoBuilder {
        private long totalCampaigns;
        private long activeCampaigns;
        private long completedCampaigns;
        private long draftCampaigns;
        private long scheduledCampaigns;
        private long runningCampaigns;
        private long failedCampaigns;
        private long cancelledCampaigns;
        private long totalEmailsSent;
        private long deliveredEmails;
        private long pendingEmails;
        private long failedEmails;
        private long queuedEmails;
        private long retryEmails;
        private long totalContacts;
        private long subscribedContacts;
        private long unsubscribedContacts;
        private long bouncedContacts;
        private long totalSegments;
        private long totalTemplates;
        private long totalUsers;

        DashboardKpiDtoBuilder() {}

        public DashboardKpiDtoBuilder totalCampaigns(long totalCampaigns) { this.totalCampaigns = totalCampaigns; return this; }
        public DashboardKpiDtoBuilder activeCampaigns(long activeCampaigns) { this.activeCampaigns = activeCampaigns; return this; }
        public DashboardKpiDtoBuilder completedCampaigns(long completedCampaigns) { this.completedCampaigns = completedCampaigns; return this; }
        public DashboardKpiDtoBuilder draftCampaigns(long draftCampaigns) { this.draftCampaigns = draftCampaigns; return this; }
        public DashboardKpiDtoBuilder scheduledCampaigns(long scheduledCampaigns) { this.scheduledCampaigns = scheduledCampaigns; return this; }
        public DashboardKpiDtoBuilder runningCampaigns(long runningCampaigns) { this.runningCampaigns = runningCampaigns; return this; }
        public DashboardKpiDtoBuilder failedCampaigns(long failedCampaigns) { this.failedCampaigns = failedCampaigns; return this; }
        public DashboardKpiDtoBuilder cancelledCampaigns(long cancelledCampaigns) { this.cancelledCampaigns = cancelledCampaigns; return this; }
        public DashboardKpiDtoBuilder totalEmailsSent(long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; return this; }
        public DashboardKpiDtoBuilder deliveredEmails(long deliveredEmails) { this.deliveredEmails = deliveredEmails; return this; }
        public DashboardKpiDtoBuilder pendingEmails(long pendingEmails) { this.pendingEmails = pendingEmails; return this; }
        public DashboardKpiDtoBuilder failedEmails(long failedEmails) { this.failedEmails = failedEmails; return this; }
        public DashboardKpiDtoBuilder queuedEmails(long queuedEmails) { this.queuedEmails = queuedEmails; return this; }
        public DashboardKpiDtoBuilder retryEmails(long retryEmails) { this.retryEmails = retryEmails; return this; }
        public DashboardKpiDtoBuilder totalContacts(long totalContacts) { this.totalContacts = totalContacts; return this; }
        public DashboardKpiDtoBuilder subscribedContacts(long subscribedContacts) { this.subscribedContacts = subscribedContacts; return this; }
        public DashboardKpiDtoBuilder unsubscribedContacts(long unsubscribedContacts) { this.unsubscribedContacts = unsubscribedContacts; return this; }
        public DashboardKpiDtoBuilder bouncedContacts(long bouncedContacts) { this.bouncedContacts = bouncedContacts; return this; }
        public DashboardKpiDtoBuilder totalSegments(long totalSegments) { this.totalSegments = totalSegments; return this; }
        public DashboardKpiDtoBuilder totalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; return this; }
        public DashboardKpiDtoBuilder totalUsers(long totalUsers) { this.totalUsers = totalUsers; return this; }

        public DashboardKpiDto build() {
            return new DashboardKpiDto(totalCampaigns, activeCampaigns, completedCampaigns, draftCampaigns,
                    scheduledCampaigns, runningCampaigns, failedCampaigns, cancelledCampaigns, totalEmailsSent,
                    deliveredEmails, pendingEmails, failedEmails, queuedEmails, retryEmails, totalContacts,
                    subscribedContacts, unsubscribedContacts, bouncedContacts, totalSegments, totalTemplates, totalUsers);
        }
    }
}
