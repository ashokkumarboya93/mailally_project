package com.mailally.campaign.repository;

import com.mailally.campaign.entity.CampaignActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignActivityLogRepository extends JpaRepository<CampaignActivityLog, Long> {

    List<CampaignActivityLog> findTop50ByCampaignIdOrderByCreatedAtDesc(Long campaignId);
}
