package com.mailally.audit.service;

import com.mailally.audit.dto.AuditResponseDto;
import com.mailally.audit.dto.CreateAuditRequestDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;

/**
 * Service interface for logging system events, searching, filtering, and exporting audit trails.
 */
public interface AuditService {

    AuditResponseDto logEvent(CustomUserDetails currentUser, CreateAuditRequestDto dto);

    void logEventInternal(Long organizationId, Long userId, String action, String module, String description, Boolean success);

    Page<AuditResponseDto> getUserAuditLogs(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    Page<AuditResponseDto> searchAuditLogs(CustomUserDetails currentUser, String query, int page, int size);

    Page<AuditResponseDto> filterAuditLogs(CustomUserDetails currentUser, String module, String action, Boolean success, int page, int size);

    byte[] exportAuditLogs(CustomUserDetails currentUser, String format, String module);
}
