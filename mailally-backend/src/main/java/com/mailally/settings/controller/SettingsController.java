package com.mailally.settings.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import com.mailally.settings.dto.ImportExportSettingsDto;
import com.mailally.settings.dto.SettingsResponseDto;
import com.mailally.settings.dto.UpdateSettingRequestDto;
import com.mailally.settings.dto.UpdateSettingsRequestDto;
import com.mailally.settings.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for MailAlly Centralized Settings Management APIs.
 */
@RestController
@RequestMapping("/api/v1/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> getOrganizationSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SettingsResponseDto> result = settingsService.getOrganizationSettings(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Organization settings retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> getCategorySettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String category) {
        List<SettingsResponseDto> result = settingsService.getCategorySettings(userDetails, category);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Category settings retrieved for " + category).data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<ApiResponse<SettingsResponseDto>> getSettingByKey(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String key,
            @RequestParam(defaultValue = "GENERAL") String category) {
        SettingsResponseDto result = settingsService.getSettingByKey(userDetails, category, key);
        return ResponseEntity.ok(ApiResponse.<SettingsResponseDto>builder()
                .success(true).message("Setting details retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<SettingsResponseDto>> updateSingleSetting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateSettingRequestDto dto) {
        SettingsResponseDto result = settingsService.updateSingleSetting(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<SettingsResponseDto>builder()
                .success(true).message("Setting updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/bulk")
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> updateMultipleSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateSettingsRequestDto dto) {
        List<SettingsResponseDto> result = settingsService.updateMultipleSettings(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Multiple settings updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> importSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ImportExportSettingsDto dto) {
        List<SettingsResponseDto> result = settingsService.importSettingsJson(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Settings imported successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<ImportExportSettingsDto>> exportSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ImportExportSettingsDto result = settingsService.exportSettingsJson(userDetails);
        return ResponseEntity.ok(ApiResponse.<ImportExportSettingsDto>builder()
                .success(true).message("Settings configuration backup exported").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> resetOrganizationSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SettingsResponseDto> result = settingsService.resetOrganizationSettings(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Organization settings reset to system defaults").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/reset/{category}")
    public ResponseEntity<ApiResponse<List<SettingsResponseDto>>> resetCategorySettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String category) {
        List<SettingsResponseDto> result = settingsService.resetCategorySettings(userDetails, category);
        return ResponseEntity.ok(ApiResponse.<List<SettingsResponseDto>>builder()
                .success(true).message("Category settings reset to defaults for " + category).data(result).timestamp(LocalDateTime.now()).build());
    }
}
