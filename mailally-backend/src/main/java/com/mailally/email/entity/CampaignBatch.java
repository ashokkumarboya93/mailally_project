package com.mailally.email.entity;

import com.mailally.campaign.entity.Campaign;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a chunk/batch of recipients during an asynchronous campaign execution.
 */
@Entity
@Table(name = "campaign_batches")
public class CampaignBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @Column(name = "worker_node_id", length = 100)
    private String workerNodeId;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "optimal_size")
    private Integer optimalSize;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CampaignBatch() {}

    public CampaignBatch(Long id, Campaign campaign, String workerNodeId, String status, Integer optimalSize,
                          Integer retryCount, LocalDateTime startedAt, LocalDateTime completedAt,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.campaign = campaign;
        this.workerNodeId = workerNodeId;
        this.status = status;
        this.optimalSize = optimalSize;
        this.retryCount = retryCount;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public String getWorkerNodeId() { return workerNodeId; }
    public void setWorkerNodeId(String workerNodeId) { this.workerNodeId = workerNodeId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getOptimalSize() { return optimalSize; }
    public void setOptimalSize(Integer optimalSize) { this.optimalSize = optimalSize; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
        if (this.retryCount == null) this.retryCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static CampaignBatchBuilder builder() { return new CampaignBatchBuilder(); }

    public static class CampaignBatchBuilder {
        private Long id;
        private Campaign campaign;
        private String workerNodeId;
        private String status;
        private Integer optimalSize;
        private Integer retryCount;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        CampaignBatchBuilder() {}

        public CampaignBatchBuilder id(Long id) { this.id = id; return this; }
        public CampaignBatchBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public CampaignBatchBuilder workerNodeId(String workerNodeId) { this.workerNodeId = workerNodeId; return this; }
        public CampaignBatchBuilder status(String status) { this.status = status; return this; }
        public CampaignBatchBuilder optimalSize(Integer optimalSize) { this.optimalSize = optimalSize; return this; }
        public CampaignBatchBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public CampaignBatchBuilder startedAt(LocalDateTime startedAt) { this.startedAt = startedAt; return this; }
        public CampaignBatchBuilder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }
        public CampaignBatchBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CampaignBatchBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CampaignBatch build() {
            return new CampaignBatch(id, campaign, workerNodeId, status, optimalSize, retryCount, startedAt,
                    completedAt, createdAt, updatedAt);
        }
    }
}
