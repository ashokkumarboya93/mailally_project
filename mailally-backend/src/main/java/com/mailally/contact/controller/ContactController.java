package com.mailally.contact.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.contact.dto.*;
import com.mailally.contact.service.ContactService;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller exposing tenant-isolated APIs for Enterprise Contact Management, Import Pipeline, History & Analytics.
 */
@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactResponseDto>> createContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateContactRequestDto dto
    ) {
        ContactResponseDto responseDto = contactService.createContact(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact created successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactResponseDto>> getContactById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        ContactResponseDto responseDto = contactService.getContactById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact retrieved successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ContactResponseDto>>> listContacts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<ContactResponseDto> pageResult = contactService.listContacts(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<ContactResponseDto>>builder()
                .success(true)
                .message("Contacts retrieved successfully")
                .data(pageResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactResponseDto>> updateContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactRequestDto dto
    ) {
        ContactResponseDto responseDto = contactService.updateContact(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact updated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        contactService.softDeleteContact(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Contact soft deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<ContactResponseDto>> restoreContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        ContactResponseDto responseDto = contactService.restoreContact(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact restored successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<ApiResponse<ContactResponseDto>> duplicateContact(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        ContactResponseDto responseDto = contactService.duplicateContact(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact duplicated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/{id}/field")
    public ResponseEntity<ApiResponse<ContactResponseDto>> inlineUpdateField(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody InlineUpdateContactDto dto
    ) {
        ContactResponseDto responseDto = contactService.inlineUpdateField(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Field updated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ContactResponseDto>>> searchContacts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<ContactResponseDto> pageResult = contactService.searchContacts(
                userDetails, email, firstName, lastName, company, phone, city, country, status, tag, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.<Page<ContactResponseDto>>builder()
                .success(true)
                .message("Contact search completed successfully")
                .data(pageResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ContactResponseDto>>> filterContacts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ContactFilterRequestDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<ContactResponseDto> pageResult = contactService.filterContacts(userDetails, filterDto, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<ContactResponseDto>>builder()
                .success(true)
                .message("Contacts filtered successfully")
                .data(pageResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/import/csv")
    public ResponseEntity<ApiResponse<ImportResultDto>> importCsv(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        ImportResultDto importResult = contactService.importCsv(userDetails, file);
        return ResponseEntity.ok(ApiResponse.<ImportResultDto>builder()
                .success(true)
                .message("CSV import executed successfully")
                .data(importResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/import/excel")
    public ResponseEntity<ApiResponse<ImportResultDto>> importExcel(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        ImportResultDto importResult = contactService.importExcel(userDetails, file);
        return ResponseEntity.ok(ApiResponse.<ImportResultDto>builder()
                .success(true)
                .message("Excel import executed successfully")
                .data(importResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/import/start")
    public ResponseEntity<ApiResponse<ImportResultDto>> startImport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute ImportSettingsDto settings
    ) {
        ImportResultDto importResult = contactService.startImport(userDetails, file, settings);
        return ResponseEntity.ok(ApiResponse.<ImportResultDto>builder()
                .success(true)
                .message("Import batch process started successfully")
                .data(importResult)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/import/progress/{batchCode}")
    public ResponseEntity<ApiResponse<ImportProgressDto>> getImportProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String batchCode
    ) {
        ImportProgressDto progress = contactService.getImportProgress(userDetails, batchCode);
        return ResponseEntity.ok(ApiResponse.<ImportProgressDto>builder()
                .success(true)
                .message("Import progress retrieved successfully")
                .data(progress)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/import/history")
    public ResponseEntity<ApiResponse<Page<ImportBatchResponseDto>>> getImportHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ImportBatchResponseDto> history = contactService.getImportHistory(userDetails, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<ImportBatchResponseDto>>builder()
                .success(true)
                .message("Import history retrieved successfully")
                .data(history)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/import/{batchId}")
    public ResponseEntity<ApiResponse<ImportBatchResponseDto>> getImportBatchDetails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long batchId
    ) {
        ImportBatchResponseDto details = contactService.getImportBatchDetails(userDetails, batchId);
        return ResponseEntity.ok(ApiResponse.<ImportBatchResponseDto>builder()
                .success(true)
                .message("Import batch details retrieved successfully")
                .data(details)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/import/{batchId}")
    public ResponseEntity<ApiResponse<Void>> deleteImportBatch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long batchId
    ) {
        contactService.deleteImportBatch(userDetails, batchId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Import batch and imported contacts rolled back successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/import/{batchId}/error-report")
    public ResponseEntity<byte[]> downloadImportErrorReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long batchId
    ) {
        ExportResponseDto exportDto = contactService.downloadImportErrorReport(userDetails, batchId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportDto.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(exportDto.getContentType()))
                .body(exportDto.getFileContent());
    }

    @GetMapping("/import/template/{format}")
    public ResponseEntity<byte[]> getImportTemplate(@PathVariable String format) {
        ExportResponseDto exportDto = contactService.getImportTemplate(format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportDto.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(exportDto.getContentType()))
                .body(exportDto.getFileContent());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportContacts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ExportResponseDto exportDto = contactService.exportContacts(userDetails);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportDto.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(exportDto.getContentType()))
                .body(exportDto.getFileContent());
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<ApiResponse<List<ContactTimelineDto>>> getContactTimeline(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        List<ContactTimelineDto> timeline = contactService.getContactTimeline(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<List<ContactTimelineDto>>builder()
                .success(true)
                .message("Contact timeline retrieved successfully")
                .data(timeline)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/stats/domains")
    public ResponseEntity<ApiResponse<List<DomainStatDto>>> getDomainStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<DomainStatDto> stats = contactService.getDomainStats(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<DomainStatDto>>builder()
                .success(true)
                .message("Email domain analytics retrieved successfully")
                .data(stats)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/bulk/tag")
    public ResponseEntity<ApiResponse<Void>> bulkUpdateTags(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.bulkUpdateTags(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Bulk tag operation executed successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/bulk/status")
    public ResponseEntity<ApiResponse<Void>> bulkUpdateStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.bulkUpdateStatus(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Bulk status update executed successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> bulkDelete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.bulkDelete(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Bulk delete executed successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/bulk/campaign")
    public ResponseEntity<ApiResponse<Void>> bulkAddToCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.bulkAddToCampaign(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Contacts added to campaign targets successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/bulk/segment")
    public ResponseEntity<ApiResponse<Void>> bulkAddToSegment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.bulkAddToSegment(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Contacts added to segment successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/tags")
    public ResponseEntity<ApiResponse<List<TagDto>>> getTags(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<TagDto> tags = contactService.getTags(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<TagDto>>builder()
                .success(true)
                .message("Tags retrieved successfully")
                .data(tags)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/filters/saved")
    public ResponseEntity<ApiResponse<List<SavedFilterDto>>> getSavedFilters(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<SavedFilterDto> filters = contactService.getSavedFilters(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<SavedFilterDto>>builder()
                .success(true)
                .message("Saved filters retrieved successfully")
                .data(filters)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/filters/saved")
    public ResponseEntity<ApiResponse<SavedFilterDto>> createSavedFilter(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SavedFilterDto dto
    ) {
        SavedFilterDto saved = contactService.createSavedFilter(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<SavedFilterDto>builder()
                .success(true)
                .message("Saved filter created successfully")
                .data(saved)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<List<ContactCollectionDto>>> getCollections(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ContactCollectionDto> collections = contactService.getCollections(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<ContactCollectionDto>>builder()
                .success(true)
                .message("Collections retrieved successfully")
                .data(collections)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/collections")
    public ResponseEntity<ApiResponse<ContactCollectionDto>> createCollection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ContactCollectionDto dto
    ) {
        ContactCollectionDto created = contactService.createCollection(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<ContactCollectionDto>builder()
                .success(true)
                .message("Collection created successfully")
                .data(created)
                .timestamp(LocalDateTime.now())
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/collections/{collectionId}/contacts")
    public ResponseEntity<ApiResponse<Page<ContactResponseDto>>> getContactsByCollection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long collectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<ContactResponseDto> contacts = contactService.getContactsByCollection(userDetails, collectionId, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<ContactResponseDto>>builder()
                .success(true)
                .message("Collection contacts retrieved successfully")
                .data(contacts)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/collections/{collectionId}")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long collectionId
    ) {
        contactService.deleteCollection(userDetails, collectionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Collection deleted successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/fields")
    public ResponseEntity<ApiResponse<List<DynamicFieldRegistryDto>>> getDynamicFields(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<DynamicFieldRegistryDto> fields = contactService.getDynamicFields(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<DynamicFieldRegistryDto>>builder()
                .success(true)
                .message("Dynamic field registry retrieved successfully")
                .data(fields)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PatchMapping("/{id}/cell")
    public ResponseEntity<ApiResponse<ContactResponseDto>> inlineCellEdit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody InlineCellEditRequestDto dto
    ) {
        ContactResponseDto updated = contactService.inlineCellEdit(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Cell updated successfully")
                .data(updated)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<ContactAuditHistoryDto>>> getContactHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        List<ContactAuditHistoryDto> history = contactService.getContactAuditHistory(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<List<ContactAuditHistoryDto>>builder()
                .success(true)
                .message("Contact history retrieved successfully")
                .data(history)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/{id}/history/{historyId}/restore")
    public ResponseEntity<ApiResponse<ContactResponseDto>> restoreContactHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long historyId
    ) {
        ContactResponseDto restored = contactService.restoreAuditHistory(userDetails, id, historyId);
        return ResponseEntity.ok(ApiResponse.<ContactResponseDto>builder()
                .success(true)
                .message("Contact value restored successfully")
                .data(restored)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/import-batch/{batchId}/undo")
    public ResponseEntity<ApiResponse<Void>> undoImportBatch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long batchId
    ) {
        contactService.undoImportBatch(userDetails, batchId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Import batch undone successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> executeBulkAction(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BulkOperationRequestDto dto
    ) {
        contactService.executeBulkAction(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Bulk operation executed successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
