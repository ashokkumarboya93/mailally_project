package com.mailally.segment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing Segment.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSegmentRequestDto {

    @Size(max = 150, message = "Segment name must not exceed 150 characters")
    private String name;

    private String description;

    private String rulesJson;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    public UpdateSegmentRequestDto() {}

    public UpdateSegmentRequestDto(String name, String description, String rulesJson, String status) {
        this.name = name;
        this.description = description;
        this.rulesJson = rulesJson;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRulesJson() { return rulesJson; }
    public void setRulesJson(String rulesJson) { this.rulesJson = rulesJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
