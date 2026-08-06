package com.mailally.contact.service;

import com.mailally.contact.dto.*;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Enterprise Service interface for Contact Management operations.
 */
public interface ContactService {

    ContactResponseDto createContact(CustomUserDetails currentUser, CreateContactRequestDto dto);

    ContactResponseDto getContactById(CustomUserDetails currentUser, Long id);

    Page<ContactResponseDto> listContacts(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    ContactResponseDto updateContact(CustomUserDetails currentUser, Long id, UpdateContactRequestDto dto);

    void softDeleteContact(CustomUserDetails currentUser, Long id);

    ContactResponseDto restoreContact(CustomUserDetails currentUser, Long id);

    ContactResponseDto duplicateContact(CustomUserDetails currentUser, Long id);

    ContactResponseDto inlineUpdateField(CustomUserDetails currentUser, Long id, InlineUpdateContactDto dto);

    Page<ContactResponseDto> searchContacts(
            CustomUserDetails currentUser,
            String email,
            String firstName,
            String lastName,
            String company,
            String phone,
            String city,
            String country,
            String status,
            String tag,
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    Page<ContactResponseDto> filterContacts(
            CustomUserDetails currentUser,
            ContactFilterRequestDto filterDto,
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    ImportResultDto importCsv(CustomUserDetails currentUser, MultipartFile file);

    ImportResultDto importExcel(CustomUserDetails currentUser, MultipartFile file);

    ImportResultDto startImport(CustomUserDetails currentUser, MultipartFile file, ImportSettingsDto settings);

    ImportProgressDto getImportProgress(CustomUserDetails currentUser, String batchCode);

    Page<ImportBatchResponseDto> getImportHistory(CustomUserDetails currentUser, int page, int size);

    ImportBatchResponseDto getImportBatchDetails(CustomUserDetails currentUser, Long batchId);

    void deleteImportBatch(CustomUserDetails currentUser, Long batchId);

    ExportResponseDto downloadImportErrorReport(CustomUserDetails currentUser, Long batchId);

    ExportResponseDto getImportTemplate(String format);

    ExportResponseDto exportContacts(CustomUserDetails currentUser);

    List<ContactTimelineDto> getContactTimeline(CustomUserDetails currentUser, Long contactId);

    List<DomainStatDto> getDomainStats(CustomUserDetails currentUser);

    void bulkUpdateTags(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    void bulkUpdateStatus(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    void bulkDelete(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    void bulkAddToCampaign(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    void bulkAddToSegment(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    List<TagDto> getTags(CustomUserDetails currentUser);

    List<SavedFilterDto> getSavedFilters(CustomUserDetails currentUser);

    SavedFilterDto createSavedFilter(CustomUserDetails currentUser, SavedFilterDto dto);

    ImportResultDto importContactsFromFile(Long orgId, MultipartFile file, Long userId, String sourceType, String duplicateStrategy);

    List<ContactCollectionDto> getCollections(CustomUserDetails currentUser);

    ContactCollectionDto createCollection(CustomUserDetails currentUser, ContactCollectionDto dto);

    List<DynamicFieldRegistryDto> getDynamicFields(CustomUserDetails currentUser);

    ContactResponseDto inlineCellEdit(CustomUserDetails currentUser, Long contactId, InlineCellEditRequestDto dto);

    List<ContactAuditHistoryDto> getContactAuditHistory(CustomUserDetails currentUser, Long contactId);

    ContactResponseDto restoreAuditHistory(CustomUserDetails currentUser, Long contactId, Long historyId);

    void undoImportBatch(CustomUserDetails currentUser, Long batchId);

    void executeBulkAction(CustomUserDetails currentUser, BulkOperationRequestDto dto);

    Page<ContactResponseDto> getContactsByCollection(CustomUserDetails currentUser, Long collectionId, int page, int size);

    void deleteCollection(CustomUserDetails currentUser, Long collectionId);
}
