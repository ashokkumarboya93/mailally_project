package com.mailally.campaign.service;

import com.mailally.campaign.dto.CampaignResponseDto;
import com.mailally.campaign.dto.CreateCampaignRequestDto;
import com.mailally.campaign.dto.ScheduleCampaignRequestDto;
import com.mailally.campaign.dto.UpdateCampaignRequestDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;

/**
 * Service interface for Campaign management operations.
 */
public interface CampaignService {

    CampaignResponseDto createCampaign(CustomUserDetails currentUser, CreateCampaignRequestDto dto);

    CampaignResponseDto getCampaignById(CustomUserDetails currentUser, Long id);

    Page<CampaignResponseDto> listCampaigns(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    CampaignResponseDto updateCampaign(CustomUserDetails currentUser, Long id, UpdateCampaignRequestDto dto);

    CampaignResponseDto attachTemplate(CustomUserDetails currentUser, Long campaignId, Long templateId);

    CampaignResponseDto attachSegment(CustomUserDetails currentUser, Long campaignId, Long segmentId);

    CampaignResponseDto scheduleCampaign(CustomUserDetails currentUser, Long campaignId, ScheduleCampaignRequestDto dto);

    CampaignResponseDto cancelCampaign(CustomUserDetails currentUser, Long campaignId);

    void softDeleteCampaign(CustomUserDetails currentUser, Long id);

    Page<CampaignResponseDto> searchCampaigns(CustomUserDetails currentUser, String name, String status,
                                              int page, int size, String sortBy, String sortDir);
}
