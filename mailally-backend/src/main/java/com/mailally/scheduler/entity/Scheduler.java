package com.mailally.scheduler.entity;

import com.mailally.campaign.entity.Campaign;
import com.mailally.organization.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing a Scheduler execution record for campaign dispatch.
 */
@Entity
@Table(name = "schedulers")
public class Scheduler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @Column(name = "execution_type", nullable = false, length = 20)
    private String executionType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "executed_time")
    private LocalDateTime executedTime;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Scheduler() {}

    public Scheduler(Long id, Organization organization, Campaign campaign, String executionType, String status,
                     LocalDateTime scheduledTime, LocalDateTime executedTime, String errorMessage,
                     Long createdBy, Long updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organization = organization;
        this.campaign = campaign;
        this.executionType = executionType;
        this.status = status;
        this.scheduledTime = scheduledTime;
        this.executedTime = executedTime;
        this.errorMessage = errorMessage;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }
    public String getExecutionType() { return executionType; }
    public void setExecutionType(String executionType) { this.executionType = executionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public LocalDateTime getExecutedTime() { return executedTime; }
    public void setExecutedTime(LocalDateTime executedTime) { this.executedTime = executedTime; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static SchedulerBuilder builder() { return new SchedulerBuilder(); }

    public static class SchedulerBuilder {
        private Long id;
        private Organization organization;
        private Campaign campaign;
        private String executionType;
        private String status;
        private LocalDateTime scheduledTime;
        private LocalDateTime executedTime;
        private String errorMessage;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        SchedulerBuilder() {}

        public SchedulerBuilder id(Long id) { this.id = id; return this; }
        public SchedulerBuilder organization(Organization organization) { this.organization = organization; return this; }
        public SchedulerBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public SchedulerBuilder executionType(String executionType) { this.executionType = executionType; return this; }
        public SchedulerBuilder status(String status) { this.status = status; return this; }
        public SchedulerBuilder scheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; return this; }
        public SchedulerBuilder executedTime(LocalDateTime executedTime) { this.executedTime = executedTime; return this; }
        public SchedulerBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public SchedulerBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public SchedulerBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public SchedulerBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SchedulerBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Scheduler build() {
            return new Scheduler(id, organization, campaign, executionType, status, scheduledTime, executedTime,
                    errorMessage, createdBy, updatedBy, createdAt, updatedAt);
        }
    }
}
