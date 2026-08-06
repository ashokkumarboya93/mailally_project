package com.mailally.template.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import com.mailally.template.dto.CreateTemplateRequestDto;
import com.mailally.template.dto.TemplateResponseDto;
import com.mailally.template.dto.UpdateTemplateRequestDto;
import com.mailally.template.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for Email Template Management APIs.
 */
@RestController
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;
    private final com.mailally.template.service.TemplateVariableEngine variableEngine;
    private final com.mailally.template.service.AiTemplateService aiTemplateService;

    public TemplateController(TemplateService templateService,
                              com.mailally.template.service.TemplateVariableEngine variableEngine,
                              com.mailally.template.service.AiTemplateService aiTemplateService) {
        this.templateService = templateService;
        this.variableEngine = variableEngine;
        this.aiTemplateService = aiTemplateService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateResponseDto>> createTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateTemplateRequestDto dto) {
        TemplateResponseDto result = templateService.createTemplate(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<TemplateResponseDto>builder()
                .success(true).message("Template created successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateResponseDto>> getTemplateById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        TemplateResponseDto result = templateService.getTemplateById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<TemplateResponseDto>builder()
                .success(true).message("Template retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TemplateResponseDto>>> listTemplates(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<TemplateResponseDto> result = templateService.listTemplates(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<TemplateResponseDto>>builder()
                .success(true).message("Templates retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateResponseDto>> updateTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateRequestDto dto) {
        TemplateResponseDto result = templateService.updateTemplate(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<TemplateResponseDto>builder()
                .success(true).message("Template updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<ApiResponse<TemplateResponseDto>> cloneTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        TemplateResponseDto result = templateService.cloneTemplate(userDetails, id);
        return new ResponseEntity<>(ApiResponse.<TemplateResponseDto>builder()
                .success(true).message("Template cloned successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<String> previewTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        String htmlContent = templateService.previewTemplate(userDetails, id);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlContent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        templateService.softDeleteTemplate(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Template soft deleted successfully").data(null).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TemplateResponseDto>>> searchTemplates(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<TemplateResponseDto> result = templateService.searchTemplates(userDetails, name, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<TemplateResponseDto>>builder()
                .success(true).message("Template search completed").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/dynamic-variables")
    public ResponseEntity<ApiResponse<java.util.List<String>>> getDynamicVariables(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        java.util.List<String> variables = variableEngine.getAvailableVariablesForOrg(userDetails.getOrganizationId());
        return ResponseEntity.ok(ApiResponse.<java.util.List<String>>builder()
                .success(true).message("Dynamic variables retrieved").data(variables).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/generate-ai")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> generateAiTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody java.util.Map<String, String> request) {
        java.util.Map<String, Object> result = aiTemplateService.generateTemplateWithAi(
                request.get("campaignGoal"),
                request.get("audience"),
                request.get("tone"),
                request.get("language"),
                request.get("cta")
        );
        return ResponseEntity.ok(ApiResponse.<java.util.Map<String, Object>>builder()
                .success(true).message("AI template generated").data(result).timestamp(LocalDateTime.now()).build());
    }
}
