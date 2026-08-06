package com.mailally.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for sending bulk ad-hoc emails to a list of recipients.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkEmailRequestDto {

    @NotNull(message = "Recipients list is required")
    @NotEmpty(message = "Recipients list cannot be empty")
    @Valid
    private List<SendEmailRequestDto> emails;

    public BulkEmailRequestDto() {}

    public BulkEmailRequestDto(List<SendEmailRequestDto> emails) {
        this.emails = emails;
    }

    public List<SendEmailRequestDto> getEmails() { return emails; }
    public void setEmails(List<SendEmailRequestDto> emails) { this.emails = emails; }
}
