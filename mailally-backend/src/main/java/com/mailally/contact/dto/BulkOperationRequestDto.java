package com.mailally.contact.dto;

import java.util.List;

public class BulkOperationRequestDto {
    private List<Long> contactIds;
    private String operation; // ADD_TAG, REMOVE_TAG, CHANGE_STATUS, MOVE_COLLECTION, ADD_TO_CAMPAIGN, DELETE
    private String tag;
    private String status;
    private Long targetCollectionId;
    private Long targetCampaignId;
    private String tagAction;
    private Long segmentId;

    public BulkOperationRequestDto() {}

    public List<Long> getContactIds() { return contactIds; }
    public void setContactIds(List<Long> contactIds) { this.contactIds = contactIds; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getTargetCollectionId() { return targetCollectionId; }
    public void setTargetCollectionId(Long targetCollectionId) { this.targetCollectionId = targetCollectionId; }

    public Long getTargetCampaignId() { return targetCampaignId; }
    public void setTargetCampaignId(Long targetCampaignId) { this.targetCampaignId = targetCampaignId; }

    public String getTagAction() { return tagAction; }
    public void setTagAction(String tagAction) { this.tagAction = tagAction; }

    public Long getSegmentId() { return segmentId; }
    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }
}
