package com.mailally.dashboard.dto;

/**
 * System and infrastructure health indicators DTO.
 */
public class DashboardHealthDto {

    private String systemStatus;
    private String databaseHealth;
    private String emailProviderHealth;
    private String schedulerHealth;
    private String queueHealth;
    private String cpuUsagePercentage;
    private String memoryUsagePercentage;
    private String applicationUptime;

    public DashboardHealthDto() {}

    public DashboardHealthDto(String systemStatus, String databaseHealth, String emailProviderHealth,
                              String schedulerHealth, String queueHealth, String cpuUsagePercentage,
                              String memoryUsagePercentage, String applicationUptime) {
        this.systemStatus = systemStatus;
        this.databaseHealth = databaseHealth;
        this.emailProviderHealth = emailProviderHealth;
        this.schedulerHealth = schedulerHealth;
        this.queueHealth = queueHealth;
        this.cpuUsagePercentage = cpuUsagePercentage;
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.applicationUptime = applicationUptime;
    }

    public String getSystemStatus() { return systemStatus; }
    public void setSystemStatus(String systemStatus) { this.systemStatus = systemStatus; }
    public String getDatabaseHealth() { return databaseHealth; }
    public void setDatabaseHealth(String databaseHealth) { this.databaseHealth = databaseHealth; }
    public String getEmailProviderHealth() { return emailProviderHealth; }
    public void setEmailProviderHealth(String emailProviderHealth) { this.emailProviderHealth = emailProviderHealth; }
    public String getSchedulerHealth() { return schedulerHealth; }
    public void setSchedulerHealth(String schedulerHealth) { this.schedulerHealth = schedulerHealth; }
    public String getQueueHealth() { return queueHealth; }
    public void setQueueHealth(String queueHealth) { this.queueHealth = queueHealth; }
    public String getCpuUsagePercentage() { return cpuUsagePercentage; }
    public void setCpuUsagePercentage(String cpuUsagePercentage) { this.cpuUsagePercentage = cpuUsagePercentage; }
    public String getMemoryUsagePercentage() { return memoryUsagePercentage; }
    public void setMemoryUsagePercentage(String memoryUsagePercentage) { this.memoryUsagePercentage = memoryUsagePercentage; }
    public String getApplicationUptime() { return applicationUptime; }
    public void setApplicationUptime(String applicationUptime) { this.applicationUptime = applicationUptime; }

    public static DashboardHealthDtoBuilder builder() { return new DashboardHealthDtoBuilder(); }

    public static class DashboardHealthDtoBuilder {
        private String systemStatus;
        private String databaseHealth;
        private String emailProviderHealth;
        private String schedulerHealth;
        private String queueHealth;
        private String cpuUsagePercentage;
        private String memoryUsagePercentage;
        private String applicationUptime;

        DashboardHealthDtoBuilder() {}

        public DashboardHealthDtoBuilder systemStatus(String systemStatus) { this.systemStatus = systemStatus; return this; }
        public DashboardHealthDtoBuilder databaseHealth(String databaseHealth) { this.databaseHealth = databaseHealth; return this; }
        public DashboardHealthDtoBuilder emailProviderHealth(String emailProviderHealth) { this.emailProviderHealth = emailProviderHealth; return this; }
        public DashboardHealthDtoBuilder schedulerHealth(String schedulerHealth) { this.schedulerHealth = schedulerHealth; return this; }
        public DashboardHealthDtoBuilder queueHealth(String queueHealth) { this.queueHealth = queueHealth; return this; }
        public DashboardHealthDtoBuilder cpuUsagePercentage(String cpuUsagePercentage) { this.cpuUsagePercentage = cpuUsagePercentage; return this; }
        public DashboardHealthDtoBuilder memoryUsagePercentage(String memoryUsagePercentage) { this.memoryUsagePercentage = memoryUsagePercentage; return this; }
        public DashboardHealthDtoBuilder applicationUptime(String applicationUptime) { this.applicationUptime = applicationUptime; return this; }

        public DashboardHealthDto build() {
            return new DashboardHealthDto(systemStatus, databaseHealth, emailProviderHealth, schedulerHealth,
                    queueHealth, cpuUsagePercentage, memoryUsagePercentage, applicationUptime);
        }
    }
}
