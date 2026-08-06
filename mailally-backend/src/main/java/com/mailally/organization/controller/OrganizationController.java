package com.mailally.organization.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.organization.dto.OrganizationDto;
import com.mailally.organization.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller providing HTTP endpoints for Organization management.
 */
@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationDto>> createOrganization(@Valid @RequestBody OrganizationDto dto) {
        OrganizationDto created = organizationService.createOrganization(dto);
        ApiResponse<OrganizationDto> response = ApiResponse.<OrganizationDto>builder()
                .success(true)
                .message("Organization created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationDto>> getOrganizationById(@PathVariable Long id) {
        OrganizationDto dto = organizationService.getOrganizationById(id);
        ApiResponse<OrganizationDto> response = ApiResponse.<OrganizationDto>builder()
                .success(true)
                .message("Organization retrieved successfully")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<OrganizationDto>> getOrganizationBySlug(@PathVariable String slug) {
        OrganizationDto dto = organizationService.getOrganizationBySlug(slug);
        ApiResponse<OrganizationDto> response = ApiResponse.<OrganizationDto>builder()
                .success(true)
                .message("Organization retrieved successfully")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationDto>>> getAllOrganizations() {
        List<OrganizationDto> list = organizationService.getAllOrganizations();
        ApiResponse<List<OrganizationDto>> response = ApiResponse.<List<OrganizationDto>>builder()
                .success(true)
                .message("Organizations retrieved successfully")
                .data(list)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationDto>> updateOrganization(@PathVariable Long id,
                                                                             @Valid @RequestBody OrganizationDto dto) {
        OrganizationDto updated = organizationService.updateOrganization(id, dto);
        ApiResponse<OrganizationDto> response = ApiResponse.<OrganizationDto>builder()
                .success(true)
                .message("Organization updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Organization deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}

