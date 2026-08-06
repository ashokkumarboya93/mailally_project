package com.mailally.audit.controller;

import com.mailally.audit.dto.AuditResponseDto;
import com.mailally.audit.dto.CreateAuditRequestDto;
import com.mailally.audit.service.AuditService;
import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for MailAlly System Audit Log APIs.
 */
@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuditResponseDto>> logEvent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateAuditRequestDto dto) {
        AuditResponseDto result = auditService.logEvent(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<AuditResponseDto>builder()
                .success(true).message("Audit event recorded").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuditResponseDto>>> getUserAuditLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<AuditResponseDto> logs = auditService.getUserAuditLogs(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<AuditResponseDto>>builder()
                .success(true).message("Audit logs retrieved successfully").data(logs).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AuditResponseDto>>> searchAuditLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AuditResponseDto> results = auditService.searchAuditLogs(userDetails, query, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<AuditResponseDto>>builder()
                .success(true).message("Audit log search completed").data(results).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<AuditResponseDto>>> filterAuditLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) Boolean success,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AuditResponseDto> filtered = auditService.filterAuditLogs(userDetails, module, action, success, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<AuditResponseDto>>builder()
                .success(true).message("Filtered audit logs retrieved").data(filtered).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAuditLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "CSV") String format,
            @RequestParam(required = false) String module) {

        byte[] exportData = auditService.exportAuditLogs(userDetails, format, module);

        String filename = "audit_export_" + System.currentTimeMillis();
        MediaType mediaType = "JSON".equalsIgnoreCase(format) ? MediaType.APPLICATION_JSON : MediaType.parseMediaType("text/csv");
        filename += "JSON".equalsIgnoreCase(format) ? ".json" : ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(exportData);
    }
}
