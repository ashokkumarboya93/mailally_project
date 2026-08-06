package com.mailally.segment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new Segment.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSegmentRequestDto {

    @NotBlank(message = "Segment name is required")
    @Size(max = 150, message = "Segment name must not exceed 150 characters")
    private String name;

    private String description;

    @NotBlank(message = "Segment type is required (STATIC or DYNAMIC)")
    @Size(max = 20, message = "Type must not exceed 20 characters")
    private String type;

    private String rulesJson;

    public CreateSegmentRequestDto() {}

    public CreateSegmentRequestDto(String name, String description, String type, String rulesJson) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.rulesJson = rulesJson;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRulesJson() { return rulesJson; }
    public void setRulesJson(String rulesJson) { this.rulesJson = rulesJson; }
}
