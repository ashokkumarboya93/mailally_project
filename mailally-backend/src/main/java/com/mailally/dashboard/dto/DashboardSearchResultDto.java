package com.mailally.dashboard.dto;

import com.mailally.campaign.dto.CampaignResponseDto;
import com.mailally.contact.dto.ContactResponseDto;
import com.mailally.segment.dto.SegmentResponseDto;
import com.mailally.template.dto.TemplateResponseDto;
import com.mailally.user.dto.UserResponseDto;

import java.util.List;

/**
 * Global dashboard search results grouped across Campaigns, Contacts, Templates, Segments, and Users.
 */
public class DashboardSearchResultDto {

    private String query;
    private List<CampaignResponseDto> matchedCampaigns;
    private List<ContactResponseDto> matchedContacts;
    private List<TemplateResponseDto> matchedTemplates;
    private List<SegmentResponseDto> matchedSegments;
    private List<UserResponseDto> matchedUsers;

    public DashboardSearchResultDto() {}

    public DashboardSearchResultDto(String query, List<CampaignResponseDto> matchedCampaigns,
                                    List<ContactResponseDto> matchedContacts, List<TemplateResponseDto> matchedTemplates,
                                    List<SegmentResponseDto> matchedSegments, List<UserResponseDto> matchedUsers) {
        this.query = query;
        this.matchedCampaigns = matchedCampaigns;
        this.matchedContacts = matchedContacts;
        this.matchedTemplates = matchedTemplates;
        this.matchedSegments = matchedSegments;
        this.matchedUsers = matchedUsers;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public List<CampaignResponseDto> getMatchedCampaigns() { return matchedCampaigns; }
    public void setMatchedCampaigns(List<CampaignResponseDto> matchedCampaigns) { this.matchedCampaigns = matchedCampaigns; }
    public List<ContactResponseDto> getMatchedContacts() { return matchedContacts; }
    public void setMatchedContacts(List<ContactResponseDto> matchedContacts) { this.matchedContacts = matchedContacts; }
    public List<TemplateResponseDto> getMatchedTemplates() { return matchedTemplates; }
    public void setMatchedTemplates(List<TemplateResponseDto> matchedTemplates) { this.matchedTemplates = matchedTemplates; }
    public List<SegmentResponseDto> getMatchedSegments() { return matchedSegments; }
    public void setMatchedSegments(List<SegmentResponseDto> matchedSegments) { this.matchedSegments = matchedSegments; }
    public List<UserResponseDto> getMatchedUsers() { return matchedUsers; }
    public void setMatchedUsers(List<UserResponseDto> matchedUsers) { this.matchedUsers = matchedUsers; }

    public static DashboardSearchResultDtoBuilder builder() { return new DashboardSearchResultDtoBuilder(); }

    public static class DashboardSearchResultDtoBuilder {
        private String query;
        private List<CampaignResponseDto> matchedCampaigns;
        private List<ContactResponseDto> matchedContacts;
        private List<TemplateResponseDto> matchedTemplates;
        private List<SegmentResponseDto> matchedSegments;
        private List<UserResponseDto> matchedUsers;

        DashboardSearchResultDtoBuilder() {}

        public DashboardSearchResultDtoBuilder query(String query) { this.query = query; return this; }
        public DashboardSearchResultDtoBuilder matchedCampaigns(List<CampaignResponseDto> matchedCampaigns) { this.matchedCampaigns = matchedCampaigns; return this; }
        public DashboardSearchResultDtoBuilder matchedContacts(List<ContactResponseDto> matchedContacts) { this.matchedContacts = matchedContacts; return this; }
        public DashboardSearchResultDtoBuilder matchedTemplates(List<TemplateResponseDto> matchedTemplates) { this.matchedTemplates = matchedTemplates; return this; }
        public DashboardSearchResultDtoBuilder matchedSegments(List<SegmentResponseDto> matchedSegments) { this.matchedSegments = matchedSegments; return this; }
        public DashboardSearchResultDtoBuilder matchedUsers(List<UserResponseDto> matchedUsers) { this.matchedUsers = matchedUsers; return this; }

        public DashboardSearchResultDto build() {
            return new DashboardSearchResultDto(query, matchedCampaigns, matchedContacts, matchedTemplates,
                    matchedSegments, matchedUsers);
        }
    }
}
