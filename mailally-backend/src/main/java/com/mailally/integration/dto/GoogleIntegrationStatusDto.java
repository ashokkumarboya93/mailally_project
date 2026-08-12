package com.mailally.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleIntegrationStatusDto {

    private String status; // NOT_CONNECTED, CONNECTED, REVOKED, ERROR
    private String accountEmail;
    private Long connectedByUserId;
    private LocalDateTime connectedAt;
    private LocalDateTime lastUsedAt;
}
