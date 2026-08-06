package com.mailally.audit.mapper;

import com.mailally.audit.dto.AuditResponseDto;
import com.mailally.audit.entity.Audit;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Audit entity and AuditResponseDto.
 */
@Component
public class AuditMapper {

    public AuditResponseDto toAuditResponseDto(Audit audit) {
        if (audit == null) return null;
        return AuditResponseDto.builder()
                .id(audit.getId())
                .organizationId(audit.getOrganization() != null ? audit.getOrganization().getId() : null)
                .userId(audit.getUser() != null ? audit.getUser().getId() : null)
                .userEmail(audit.getUser() != null ? audit.getUser().getEmail() : "System")
                .action(audit.getAction())
                .module(audit.getModule())
                .description(audit.getDescription())
                .ipAddress(audit.getIpAddress())
                .browser(audit.getBrowser())
                .timestamp(audit.getTimestamp())
                .success(audit.getSuccess())
                .failureReason(audit.getFailureReason())
                .referenceId(audit.getReferenceId())
                .build();
    }
}
