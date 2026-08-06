package com.mailally.campaign.dto;

import java.util.ArrayList;
import java.util.List;

public class CampaignDiagnosticsDto {
    private Long campaignId;
    private Boolean isReady = true;
    private Long totalRecipients = 0L;
    private Long validRecipientsCount = 0L;
    private Long missingEmailCount = 0L;
    private Long missingVariableCount = 0L;
    private Long duplicateCount = 0L;
    private Boolean templateExists = false;
    private Boolean providerHealthy = true;
    private String activeProvider = "SMTP";
    private Integer estimatedDurationMinutes = 0;
    private String estimatedCost = "$0.00";
    private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public CampaignDiagnosticsDto() {}

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }

    public Boolean getIsReady() { return isReady; }
    public void setIsReady(Boolean ready) { isReady = ready; }

    public Long getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(Long totalRecipients) { this.totalRecipients = totalRecipients; }

    public Long getValidRecipientsCount() { return validRecipientsCount; }
    public void setValidRecipientsCount(Long validRecipientsCount) { this.validRecipientsCount = validRecipientsCount; }

    public Long getMissingEmailCount() { return missingEmailCount; }
    public void setMissingEmailCount(Long missingEmailCount) { this.missingEmailCount = missingEmailCount; }

    public Long getMissingVariableCount() { return missingVariableCount; }
    public void setMissingVariableCount(Long missingVariableCount) { this.missingVariableCount = missingVariableCount; }

    public Long getDuplicateCount() { return duplicateCount; }
    public void setDuplicateCount(Long duplicateCount) { this.duplicateCount = duplicateCount; }

    public Boolean getTemplateExists() { return templateExists; }
    public void setTemplateExists(Boolean templateExists) { this.templateExists = templateExists; }

    public Boolean getProviderHealthy() { return providerHealthy; }
    public void setProviderHealthy(Boolean providerHealthy) { this.providerHealthy = providerHealthy; }

    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }

    public Integer getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }

    public String getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(String estimatedCost) { this.estimatedCost = estimatedCost; }

    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
