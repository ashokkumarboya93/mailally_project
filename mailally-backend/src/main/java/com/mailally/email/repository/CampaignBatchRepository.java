package com.mailally.email.repository;

import com.mailally.email.entity.CampaignBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignBatchRepository extends JpaRepository<CampaignBatch, Long> {
    List<CampaignBatch> findByCampaignId(Long campaignId);
    List<CampaignBatch> findByStatus(String status);
}
