package com.mailally.subscription.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for changing plan tiers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanUpgradeRequestDto {

    @NotBlank(message = "Target plan code is required")
    private String planCode; // FREE, STARTER, PRO, BUSINESS, ENTERPRISE

    public PlanUpgradeRequestDto() {}

    public PlanUpgradeRequestDto(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
}
