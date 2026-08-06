package com.mailally.contact.service.impl;

import com.mailally.contact.dto.*;
import com.mailally.contact.entity.*;
import com.mailally.contact.mapper.ContactMapper;
import com.mailally.contact.provider.ContactRawRow;
import com.mailally.contact.pipeline.ContactValidationPipeline;
import com.mailally.contact.provider.ContactImportProvider;
import com.mailally.contact.provider.ImportProviderFactory;
import com.mailally.contact.provider.SourceType;
import com.mailally.contact.repository.*;
import com.mailally.contact.service.ContactService;
import com.mailally.contact.service.ImportProgressTracker;
import com.mailally.contact.validator.ContactValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final OrganizationRepository organizationRepository;
    private final ImportBatchRepository importBatchRepository;
    private final ImportErrorRepository importErrorRepository;
    private final TagRepository tagRepository;
    private final ContactTimelineRepository contactTimelineRepository;
    private final SavedFilterRepository savedFilterRepository;
    private final ContactValidator contactValidator;
    private final ContactMapper contactMapper;
    private final ImportProviderFactory importProviderFactory;
    private final ContactValidationPipeline validationPipeline;
    private final ImportProgressTracker progressTracker;

    private final ContactCollectionRepository collectionRepository;
    private final DynamicFieldRegistryRepository fieldRegistryRepository;
    private final ContactAuditHistoryRepository auditHistoryRepository;
    private final com.mailally.campaign.repository.CampaignRecipientRepository campaignRecipientRepository;
    private final com.mailally.campaign.repository.CampaignRepository campaignRepository;

    public ContactServiceImpl(ContactRepository contactRepository,
                              OrganizationRepository organizationRepository,
                              ImportBatchRepository importBatchRepository,
                              ImportErrorRepository importErrorRepository,
                              TagRepository tagRepository,
                              ContactTimelineRepository contactTimelineRepository,
                              SavedFilterRepository savedFilterRepository,
                              ContactValidator contactValidator,
                              ContactMapper contactMapper,
                              ImportProviderFactory importProviderFactory,
                              ContactValidationPipeline validationPipeline,
                              ImportProgressTracker progressTracker,
                              ContactCollectionRepository collectionRepository,
                              DynamicFieldRegistryRepository fieldRegistryRepository,
                              ContactAuditHistoryRepository auditHistoryRepository,
                              com.mailally.campaign.repository.CampaignRecipientRepository campaignRecipientRepository,
                              com.mailally.campaign.repository.CampaignRepository campaignRepository) {
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.importBatchRepository = importBatchRepository;
        this.importErrorRepository = importErrorRepository;
        this.tagRepository = tagRepository;
        this.contactTimelineRepository = contactTimelineRepository;
        this.savedFilterRepository = savedFilterRepository;
        this.contactValidator = contactValidator;
        this.contactMapper = contactMapper;
        this.importProviderFactory = importProviderFactory;
        this.validationPipeline = validationPipeline;
        this.progressTracker = progressTracker;
        this.collectionRepository = collectionRepository;
        this.fieldRegistryRepository = fieldRegistryRepository;
        this.auditHistoryRepository = auditHistoryRepository;
        this.campaignRecipientRepository = campaignRecipientRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public ContactResponseDto createContact(CustomUserDetails currentUser, CreateContactRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        contactValidator.validateCreate(dto, currentUser.getOrganizationId());

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Contact contact = contactMapper.toContactEntity(dto, org, currentUser.getUserId());
        Contact savedContact = contactRepository.save(contact);

        logTimeline(savedContact.getId(), currentUser.getOrganizationId(), "CONTACT_CREATED", "Contact created manually", currentUser.getUserId());
        return contactMapper.toContactResponseDto(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponseDto getContactById(CustomUserDetails currentUser, Long id) {
        Contact contact = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));
        return contactMapper.toContactResponseDto(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> listContacts(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Contact> contactsPage = contactRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return contactsPage.map(contactMapper::toContactResponseDto);
    }

    @Override
    public ContactResponseDto updateContact(CustomUserDetails currentUser, Long id, UpdateContactRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        Contact contact = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));

        contactMapper.updateContactFromDto(contact, dto, currentUser.getUserId());
        Contact savedContact = contactRepository.save(contact);

        logTimeline(savedContact.getId(), currentUser.getOrganizationId(), "CONTACT_UPDATED", "Contact details updated", currentUser.getUserId());
        return contactMapper.toContactResponseDto(savedContact);
    }

    @Override
    public void softDeleteContact(CustomUserDetails currentUser, Long id) {
        contactValidator.validateAdminOrManager(currentUser);
        Contact contact = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));

        contact.setIsDeleted(true);
        contact.setDeletedBy(currentUser.getUserId());
        contact.setDeletedAt(LocalDateTime.now());
        contactRepository.save(contact);

        logTimeline(contact.getId(), currentUser.getOrganizationId(), "CONTACT_DELETED", "Contact soft deleted", currentUser.getUserId());
    }

    @Override
    public ContactResponseDto restoreContact(CustomUserDetails currentUser, Long id) {
        contactValidator.validateAdminOrManager(currentUser);
        Contact contact = contactRepository.findById(id)
                .filter(c -> c.getOrganization().getId().equals(currentUser.getOrganizationId()))
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));

        contact.setIsDeleted(false);
        contact.setDeletedBy(null);
        contact.setDeletedAt(null);
        Contact saved = contactRepository.save(contact);

        logTimeline(saved.getId(), currentUser.getOrganizationId(), "CONTACT_RESTORED", "Contact restored from trash", currentUser.getUserId());
        return contactMapper.toContactResponseDto(saved);
    }

    @Override
    public ContactResponseDto duplicateContact(CustomUserDetails currentUser, Long id) {
        contactValidator.validateAdminOrManager(currentUser);
        Contact original = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));

        String newEmail = "copy_" + System.currentTimeMillis() + "_" + original.getEmail();
        Contact copy = Contact.builder()
                .organization(original.getOrganization())
                .firstName(original.getFirstName() != null ? original.getFirstName() + " (Copy)" : "Copy")
                .lastName(original.getLastName())
                .email(newEmail)
                .phone(original.getPhone())
                .company(original.getCompany())
                .department(original.getDepartment())
                .designation(original.getDesignation())
                .city(original.getCity())
                .state(original.getState())
                .country(original.getCountry())
                .address(original.getAddress())
                .postalCode(original.getPostalCode())
                .website(original.getWebsite())
                .tags(original.getTags())
                .notes(original.getNotes())
                .status(original.getStatus())
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .isDeleted(false)
                .build();

        Contact saved = contactRepository.save(copy);
        logTimeline(saved.getId(), currentUser.getOrganizationId(), "CONTACT_DUPLICATED", "Contact duplicated from #" + id, currentUser.getUserId());
        return contactMapper.toContactResponseDto(saved);
    }

    @Override
    public ContactResponseDto inlineUpdateField(CustomUserDetails currentUser, Long id, InlineUpdateContactDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        Contact contact = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found with ID: " + id));

        String field = dto.getFieldName().trim().toLowerCase();
        String val = dto.getFieldValue() != null ? dto.getFieldValue().trim() : null;

        switch (field) {
            case "firstname": contact.setFirstName(val); break;
            case "lastname": contact.setLastName(val); break;
            case "email":
                if (val == null || !val.contains("@")) throw new CustomException("Invalid email format");
                contact.setEmail(val.toLowerCase());
                break;
            case "phone": contact.setPhone(val); break;
            case "company": contact.setCompany(val); break;
            case "department": contact.setDepartment(val); break;
            case "designation": contact.setDesignation(val); break;
            case "city": contact.setCity(val); break;
            case "state": contact.setState(val); break;
            case "country": contact.setCountry(val); break;
            case "tags": contact.setTags(val); break;
            case "status":
                if (val != null) contact.setStatus(val.toUpperCase());
                break;
            default:
                throw new CustomException("Field '" + field + "' is not editable inline");
        }

        contact.setUpdatedBy(currentUser.getUserId());
        Contact saved = contactRepository.save(contact);
        logTimeline(saved.getId(), currentUser.getOrganizationId(), "FIELD_UPDATED", "Field '" + field + "' updated inline", currentUser.getUserId());
        return contactMapper.toContactResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> searchContacts(CustomUserDetails currentUser, String email, String firstName,
                                                    String lastName, String company, String phone, String city,
                                                    String country, String status, String tag, int page, int size,
                                                    String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Contact> contactsPage = contactRepository.searchContacts(
                currentUser.getOrganizationId(), email, firstName, lastName, company, phone, city, country, status, tag, pageable
        );
        return contactsPage.map(contactMapper::toContactResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> filterContacts(CustomUserDetails currentUser, ContactFilterRequestDto filterDto,
                                                    int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Contact> contactsPage;
        if (filterDto.getSearch() != null && !filterDto.getSearch().isBlank()) {
            contactsPage = contactRepository.searchAllFields(currentUser.getOrganizationId(), filterDto.getSearch().trim(), pageable);
        } else {
            contactsPage = contactRepository.filterContacts(
                    currentUser.getOrganizationId(),
                    filterDto.getEmail(),
                    filterDto.getFirstName(),
                    filterDto.getLastName(),
                    filterDto.getCompany(),
                    filterDto.getCity(),
                    filterDto.getCountry(),
                    filterDto.getStatus(),
                    filterDto.getTag(),
                    filterDto.getImportBatchId(),
                    filterDto.getEmailDomain(),
                    pageable
            );
        }
        return contactsPage.map(contactMapper::toContactResponseDto);
    }

    @Override
    public ImportResultDto importCsv(CustomUserDetails currentUser, MultipartFile file) {
        ImportSettingsDto settings = new ImportSettingsDto();
        settings.setSourceType("CSV");
        return startImport(currentUser, file, settings);
    }

    @Override
    public ImportResultDto importExcel(CustomUserDetails currentUser, MultipartFile file) {
        ImportSettingsDto settings = new ImportSettingsDto();
        settings.setSourceType("EXCEL");
        return startImport(currentUser, file, settings);
    }

    @Override
    public ImportResultDto startImport(CustomUserDetails currentUser, MultipartFile file, ImportSettingsDto settings) {
        contactValidator.validateAdminOrManager(currentUser);
        if (file == null || file.isEmpty()) {
            throw new CustomException("Uploaded file is empty");
        }

        long startTime = System.currentTimeMillis();
        SourceType sourceType = SourceType.valueOf(settings.getSourceType() != null ? settings.getSourceType().toUpperCase() : "CSV");
        ContactImportProvider provider = importProviderFactory.getProvider(sourceType);

        List<ContactRawRow> rawRows;
        try (InputStream inputStream = file.getInputStream()) {
            rawRows = provider.readRows(inputStream);
        } catch (Exception e) {
            throw new CustomException("Failed to parse import file: " + e.getMessage());
        }

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        String batchCode = "IMPORT_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String batchName = (settings.getBatchName() != null && !settings.getBatchName().isBlank())
                ? settings.getBatchName().trim()
                : file.getOriginalFilename();

        ImportBatch batch = new ImportBatch();
        batch.setBatchCode(batchCode);
        batch.setBatchName(batchName);
        batch.setOriginalFileName(file.getOriginalFilename());
        batch.setOrganizationId(org.getId());
        batch.setUploadedBy(currentUser.getUserId());
        batch.setSourceType(sourceType.name());
        batch.setDuplicateStrategy(settings.getDuplicateStrategy() != null ? settings.getDuplicateStrategy().toUpperCase() : "SKIP");
        batch.setTotalRows(rawRows.size());
        batch.setStatus("PROCESSING");
        batch = importBatchRepository.save(batch);

        ContactCollection collection = new ContactCollection();
        collection.setName(batchName);
        collection.setType("IMPORT");
        collection.setOrganization(org);
        collection.setSourceType(sourceType.name());
        collection.setTag("CSV");
        collection.setColorCode("#1F57F5");
        collection.setCreatedBy(currentUser.getUserId());
        collection.setUpdatedBy(currentUser.getUserId());
        collection = collectionRepository.save(collection);

        progressTracker.init(batchCode, rawRows.size());
        progressTracker.update(batchCode, "VALIDATING", 0, 0, 0, 0);

        int importedCount = 0;
        int skippedCount = 0;
        int invalidCount = 0;
        int duplicateCount = 0;

        List<ImportError> errorLogs = new ArrayList<>();

        for (int i = 0; i < rawRows.size(); i++) {
            ContactRawRow row = rawRows.get(i);
            progressTracker.update(batchCode, "PROCESSING", i + 1, importedCount, skippedCount, invalidCount);

            ContactValidationPipeline.ValidationResult vr = validationPipeline.validate(row);
            if (!vr.isValid()) {
                invalidCount++;
                skippedCount++;
                ImportError err = new ImportError();
                err.setImportBatchId(batch.getId());
                err.setOrganizationId(org.getId());
                err.setRowNumber(row.getRowNumber());
                err.setRawEmail(row.getRawEmail());
                err.setRawRecord(row.getFirstName() + " " + row.getLastName());
                err.setErrorReason(vr.getReason());
                errorLogs.add(err);
                continue;
            }

            String cleanEmail = row.getRawEmail().trim().toLowerCase();
            Optional<Contact> existingOpt = contactRepository.findByOrganizationIdAndEmail(org.getId(), cleanEmail);

            if (existingOpt.isPresent()) {
                duplicateCount++;
                Contact existing = existingOpt.get();
                existing.setCollectionId(collection.getId());
                existing.setIsDeleted(false);

                if ("SKIP".equals(batch.getDuplicateStrategy()) || "KEEP_EXISTING".equals(batch.getDuplicateStrategy())) {
                    contactRepository.save(existing);
                    importedCount++;
                    skippedCount++;
                    continue;
                } else {
                    if (row.getFirstName() != null && !row.getFirstName().isBlank()) existing.setFirstName(row.getFirstName());
                    if (row.getLastName() != null && !row.getLastName().isBlank()) existing.setLastName(row.getLastName());
                    if (row.getPhone() != null && !row.getPhone().isBlank()) existing.setPhone(row.getPhone());
                    if (row.getCompany() != null && !row.getCompany().isBlank()) existing.setCompany(row.getCompany());
                    if (row.getCity() != null && !row.getCity().isBlank()) existing.setCity(row.getCity());
                    if (row.getCountry() != null && !row.getCountry().isBlank()) existing.setCountry(row.getCountry());
                    existing.setImportBatchId(batch.getId());
                    existing.setImportBatchName(batch.getBatchName());
                    existing.setImportDate(LocalDateTime.now());
                    existing.setSourceType(sourceType.name());
                    existing.setUpdatedBy(currentUser.getUserId());
                    contactRepository.save(existing);
                    importedCount++;
                    continue;
                }
            }

            Contact contact = Contact.builder()
                    .organization(org)
                    .firstName(row.getFirstName())
                    .lastName(row.getLastName())
                    .email(cleanEmail)
                    .phone(row.getPhone())
                    .company(row.getCompany())
                    .department(row.getDepartment())
                    .designation(row.getDesignation())
                    .city(row.getCity())
                    .state(row.getState())
                    .country(row.getCountry())
                    .address(row.getAddress())
                    .postalCode(row.getPostalCode())
                    .website(row.getWebsite())
                    .tags(row.getTags())
                    .notes(row.getNotes())
                    .status(row.getStatus() != null && !row.getStatus().isBlank() ? row.getStatus().toUpperCase() : "SUBSCRIBED")
                    .createdBy(currentUser.getUserId())
                    .updatedBy(currentUser.getUserId())
                    .isDeleted(false)
                    .collectionId(collection.getId())
                    .build();

            contact.setImportBatchId(batch.getId());
            contact.setImportBatchName(batch.getBatchName());
            contact.setImportDate(LocalDateTime.now());
            contact.setSourceType(sourceType.name());

            try {
                contactRepository.save(contact);
                importedCount++;
            } catch (Exception e) {
                log.warn("Skipping duplicate or problematic row {}: {}", row.getRawEmail(), e.getMessage());
                skippedCount++;
            }
        }

        if (!errorLogs.isEmpty()) {
            importErrorRepository.saveAll(errorLogs);
        }

        long durationMs = System.currentTimeMillis() - startTime;
        batch.setImportedCount(importedCount);
        batch.setSkippedCount(skippedCount);
        batch.setInvalidCount(invalidCount);
        batch.setDuplicateCount(duplicateCount);
        batch.setDurationMs(durationMs);
        batch.setStatus("COMPLETED");
        importBatchRepository.save(batch);

        collection.setContactCount(importedCount);
        collection.setSubscribedCount(importedCount);
        collection.setInvalidCount(invalidCount);
        collection.setDuplicateCount(duplicateCount);
        collectionRepository.save(collection);

        progressTracker.complete(batchCode);

        ImportResultDto result = new ImportResultDto();
        result.setTotalRows(rawRows.size());
        result.setSuccessCount(importedCount);
        result.setSkippedCount(skippedCount);
        result.setInvalidCount(invalidCount);
        result.setDuplicateCount(duplicateCount);
        result.setDurationMs(durationMs);
        result.setCollectionId(collection.getId());
        result.setMessage("Import completed successfully. " + importedCount + " contacts processed into collection.");
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ImportProgressDto getImportProgress(CustomUserDetails currentUser, String batchCode) {
        return progressTracker.getProgress(batchCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImportBatchResponseDto> getImportHistory(CustomUserDetails currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ImportBatch> batchPage = importBatchRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return batchPage.map(this::toImportBatchResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ImportBatchResponseDto getImportBatchDetails(CustomUserDetails currentUser, Long batchId) {
        ImportBatch batch = importBatchRepository.findByIdAndOrganizationIdAndIsDeletedFalse(batchId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Import batch not found with ID: " + batchId));
        return toImportBatchResponseDto(batch);
    }

    @Override
    public void deleteImportBatch(CustomUserDetails currentUser, Long batchId) {
        contactValidator.validateAdminOrManager(currentUser);
        ImportBatch batch = importBatchRepository.findByIdAndOrganizationIdAndIsDeletedFalse(batchId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Import batch not found with ID: " + batchId));

        // Rollback only contacts imported in this batch
        contactRepository.deleteByOrganizationIdAndImportBatchId(currentUser.getOrganizationId(), batchId);
        importErrorRepository.deleteByImportBatchIdAndOrganizationId(batchId, currentUser.getOrganizationId());

        batch.setIsDeleted(true);
        importBatchRepository.save(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public ExportResponseDto downloadImportErrorReport(CustomUserDetails currentUser, Long batchId) {
        List<ImportError> errors = importErrorRepository.findByImportBatchIdAndOrganizationId(batchId, currentUser.getOrganizationId());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader("Row Number", "Raw Email", "Raw Record", "Error Reason").build())) {

            for (ImportError err : errors) {
                csvPrinter.printRecord(err.getRowNumber(), err.getRawEmail(), err.getRawRecord(), err.getErrorReason());
            }
            csvPrinter.flush();

            return ExportResponseDto.builder()
                    .fileName("invalid_contacts_batch_" + batchId + ".csv")
                    .contentType("text/csv")
                    .fileContent(baos.toByteArray())
                    .recordCount(errors.size())
                    .build();
        } catch (Exception e) {
            throw new CustomException("Failed to generate error report CSV: " + e.getMessage());
        }
    }

    @Override
    public ExportResponseDto getImportTemplate(String format) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader(
                     "First Name", "Last Name", "Email Address", "Phone Number", "Company Name",
                     "Department", "Job Title", "City", "Country", "Tags", "Status"
             ).build())) {

            csvPrinter.printRecord("John", "Doe", "john.doe@example.com", "+1-555-0192", "Acme Corp", "Sales", "Manager", "San Francisco", "United States", "VIP,Lead", "SUBSCRIBED");
            csvPrinter.printRecord("Maria", "Garcia", "maria.garcia@techcorp.io", "+34-91-555-0123", "TechCorp", "Engineering", "Lead", "Madrid", "Spain", "Customer", "SUBSCRIBED");
            csvPrinter.flush();

            return ExportResponseDto.builder()
                    .fileName("mailally_contacts_template.csv")
                    .contentType("text/csv")
                    .fileContent(baos.toByteArray())
                    .recordCount(2)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Failed to generate import template: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ExportResponseDto exportContacts(CustomUserDetails currentUser) {
        List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader(
                     "ID", "First Name", "Last Name", "Email", "Phone", "Company", "Department",
                     "Designation", "City", "State", "Country", "Tags", "Status", "Created At"
             ).build())) {

            for (Contact c : contacts) {
                csvPrinter.printRecord(
                        c.getId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone(),
                        c.getCompany(), c.getDepartment(), c.getDesignation(), c.getCity(),
                        c.getState(), c.getCountry(), c.getTags(), c.getStatus(), c.getCreatedAt()
                );
            }
            csvPrinter.flush();

            String fileName = "contacts_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            return ExportResponseDto.builder()
                    .fileName(fileName)
                    .contentType("text/csv")
                    .fileContent(baos.toByteArray())
                    .recordCount(contacts.size())
                    .build();
        } catch (Exception e) {
            throw new CustomException("Failed to export contacts to CSV: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactTimelineDto> getContactTimeline(CustomUserDetails currentUser, Long contactId) {
        List<ContactTimeline> timeline = contactTimelineRepository.findByContactIdAndOrganizationIdOrderByCreatedAtDesc(contactId, currentUser.getOrganizationId());
        return timeline.stream().map(t -> new ContactTimelineDto(t.getId(), t.getContactId(), t.getEventType(), t.getDescription(), t.getPerformedBy(), t.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainStatDto> getDomainStats(CustomUserDetails currentUser) {
        List<DomainStatDto> stats = contactRepository.findDomainStatsByOrganizationId(currentUser.getOrganizationId());
        long total = stats.stream().mapToLong(DomainStatDto::getCount).sum();
        if (total > 0) {
            stats.forEach(s -> s.setPercentage(Math.round((double) s.getCount() / total * 1000.0) / 10.0));
        }
        return stats;
    }

    @Override
    public void bulkUpdateTags(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty() || dto.getTag() == null) return;

        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());
        String tag = dto.getTag().trim();
        boolean remove = "REMOVE".equalsIgnoreCase(dto.getTagAction());

        for (Contact c : contacts) {
            String currentTags = c.getTags() != null ? c.getTags() : "";
            Set<String> tagSet = Arrays.stream(currentTags.split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .collect(Collectors.toSet());

            if (remove) {
                tagSet.remove(tag);
            } else {
                tagSet.add(tag);
            }
            c.setTags(String.join(",", tagSet));
        }
        contactRepository.saveAll(contacts);
    }

    @Override
    public void bulkUpdateStatus(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty() || dto.getStatus() == null) return;

        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());
        String status = dto.getStatus().trim().toUpperCase();

        for (Contact c : contacts) {
            c.setStatus(status);
            c.setUpdatedBy(currentUser.getUserId());
        }
        contactRepository.saveAll(contacts);
    }

    @Override
    public void bulkDelete(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty()) return;

        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());
        for (Contact c : contacts) {
            c.setIsDeleted(true);
            c.setDeletedBy(currentUser.getUserId());
            c.setDeletedAt(LocalDateTime.now());
        }
        contactRepository.saveAll(contacts);
    }

    @Override
    public void bulkAddToCampaign(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty()) return;
        // Integrates directly using existing contact IDs without record duplication
        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());
        for (Contact c : contacts) {
            logTimeline(c.getId(), currentUser.getOrganizationId(), "ADDED_TO_CAMPAIGN", "Contact added to campaign targets", currentUser.getUserId());
        }
    }

    @Override
    public void bulkAddToSegment(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        contactValidator.validateAdminOrManager(currentUser);
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty()) return;
        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());
        for (Contact c : contacts) {
            logTimeline(c.getId(), currentUser.getOrganizationId(), "ADDED_TO_SEGMENT", "Contact added to segment #" + dto.getSegmentId(), currentUser.getUserId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getTags(CustomUserDetails currentUser) {
        List<Tag> tags = tagRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());
        return tags.stream().map(t -> new TagDto(t.getId(), t.getOrganizationId(), t.getName(), t.getColorCode(), t.getUsageCount(), t.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedFilterDto> getSavedFilters(CustomUserDetails currentUser) {
        List<SavedFilter> filters = savedFilterRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());
        return filters.stream().map(f -> new SavedFilterDto(f.getId(), f.getOrganizationId(), f.getName(), f.getFilterJson(), f.getCreatedBy(), f.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    public SavedFilterDto createSavedFilter(CustomUserDetails currentUser, SavedFilterDto dto) {
        SavedFilter sf = new SavedFilter();
        sf.setOrganizationId(currentUser.getOrganizationId());
        sf.setName(dto.getName());
        sf.setFilterJson(dto.getFilterJson());
        sf.setCreatedBy(currentUser.getUserId());
        SavedFilter saved = savedFilterRepository.save(sf);
        return new SavedFilterDto(saved.getId(), saved.getOrganizationId(), saved.getName(), saved.getFilterJson(), saved.getCreatedBy(), saved.getCreatedAt());
    }

    private void logTimeline(Long contactId, Long orgId, String eventType, String description, Long userId) {
        try {
            ContactTimeline timeline = new ContactTimeline(contactId, orgId, eventType, description, userId);
            contactTimelineRepository.save(timeline);
        } catch (Exception ignored) {
        }
    }

    private ImportBatchResponseDto toImportBatchResponseDto(ImportBatch b) {
        ImportBatchResponseDto dto = new ImportBatchResponseDto();
        dto.setId(b.getId());
        dto.setBatchCode(b.getBatchCode());
        dto.setBatchName(b.getBatchName());
        dto.setOriginalFileName(b.getOriginalFileName());
        dto.setOrganizationId(b.getOrganizationId());
        dto.setUploadedBy(b.getUploadedBy());
        dto.setImportDate(b.getImportDate());
        dto.setSourceType(b.getSourceType());
        dto.setDuplicateStrategy(b.getDuplicateStrategy());
        dto.setTotalRows(b.getTotalRows());
        dto.setImportedCount(b.getImportedCount());
        dto.setSkippedCount(b.getSkippedCount());
        dto.setInvalidCount(b.getInvalidCount());
        dto.setDuplicateCount(b.getDuplicateCount());
        dto.setStatus(b.getStatus());
        dto.setDurationMs(b.getDurationMs());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }

    @Override
    public ImportResultDto importContactsFromFile(Long orgId, MultipartFile file, Long userId, String sourceType, String duplicateStrategy) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));
        
        ImportSettingsDto settings = new ImportSettingsDto();
        settings.setSourceType(sourceType != null ? sourceType : "EXCEL");
        settings.setDuplicateStrategy(duplicateStrategy != null ? duplicateStrategy : "SKIP");
        
        CustomUserDetails stubUser = new CustomUserDetails(userId, "user@mailally.com", "pass", orgId, "Organization Admin", true);
        return startImport(stubUser, file, settings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactCollectionDto> getCollections(CustomUserDetails currentUser) {
        List<ContactCollection> collections = collectionRepository.findByOrganizationIdAndIsDeletedFalseOrderByCreatedAtDesc(currentUser.getOrganizationId());
        List<ContactCollectionDto> dtos = new ArrayList<>();
        for (ContactCollection c : collections) {
            ContactCollectionDto dto = new ContactCollectionDto();
            dto.setId(c.getId());
            dto.setOrganizationId(c.getOrganization().getId());
            dto.setName(c.getName());
            dto.setDescription(c.getDescription());
            dto.setType(c.getType());
            dto.setColorCode(c.getColorCode());
            dto.setTag(c.getTag());
            dto.setContactCount(c.getContactCount());
            dto.setSubscribedCount(c.getSubscribedCount());
            dto.setUnsubscribedCount(c.getUnsubscribedCount());
            dto.setInvalidCount(c.getInvalidCount());
            dto.setDuplicateCount(c.getDuplicateCount());
            dto.setSourceType(c.getSourceType());
            dto.setCreatedAt(c.getCreatedAt());
            dto.setUpdatedAt(c.getUpdatedAt());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ContactCollectionDto createCollection(CustomUserDetails currentUser, ContactCollectionDto dto) {
        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));
        ContactCollection collection = new ContactCollection();
        collection.setOrganization(org);
        collection.setName(dto.getName());
        collection.setDescription(dto.getDescription());
        collection.setType(dto.getType() != null ? dto.getType() : "MANUAL");
        collection.setTag(dto.getTag());
        collection.setColorCode(dto.getColorCode() != null ? dto.getColorCode() : "#3B82F6");
        collection.setCreatedBy(currentUser.getUserId());
        ContactCollection saved = collectionRepository.save(collection);

        dto.setId(saved.getId());
        dto.setOrganizationId(org.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setUpdatedAt(saved.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DynamicFieldRegistryDto> getDynamicFields(CustomUserDetails currentUser) {
        List<DynamicFieldRegistry> fields = fieldRegistryRepository.findByOrganizationIdOrderByOrderIndexAscCreatedAtAsc(currentUser.getOrganizationId());
        List<DynamicFieldRegistryDto> dtos = new ArrayList<>();
        for (DynamicFieldRegistry f : fields) {
            DynamicFieldRegistryDto dto = new DynamicFieldRegistryDto();
            dto.setId(f.getId());
            dto.setOrganizationId(f.getOrganization().getId());
            dto.setFieldKey(f.getFieldKey());
            dto.setDisplayName(f.getDisplayName());
            dto.setDataType(f.getDataType());
            dto.setIsFilterable(f.getIsFilterable());
            dto.setIsSortable(f.getIsSortable());
            dto.setIsVisible(f.getIsVisible());
            dto.setSampleValue(f.getSampleValue());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ContactResponseDto inlineCellEdit(CustomUserDetails currentUser, Long contactId, InlineCellEditRequestDto dto) {
        Contact contact = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(contactId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Contact not found"));

        String oldValue = "";
        String newValue = dto.getNewValue();

        if (Boolean.TRUE.equals(dto.getIsCustomField())) {
            Map<String, Object> map = new HashMap<>();
            if (contact.getCustomFields() != null && !contact.getCustomFields().isEmpty()) {
                try {
                    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(contact.getCustomFields(), Map.class);
                } catch (Exception ignored) {}
            }
            oldValue = map.get(dto.getFieldName()) != null ? map.get(dto.getFieldName()).toString() : "";
            map.put(dto.getFieldName(), newValue);
            try {
                contact.setCustomFields(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(map));
            } catch (Exception ignored) {}
        } else {
            switch (dto.getFieldName()) {
                case "firstName": oldValue = contact.getFirstName(); contact.setFirstName(newValue); break;
                case "lastName": oldValue = contact.getLastName(); contact.setLastName(newValue); break;
                case "email": oldValue = contact.getEmail(); contact.setEmail(newValue); break;
                case "phone": oldValue = contact.getPhone(); contact.setPhone(newValue); break;
                case "company": oldValue = contact.getCompany(); contact.setCompany(newValue); break;
                case "department": oldValue = contact.getDepartment(); contact.setDepartment(newValue); break;
                case "designation": oldValue = contact.getDesignation(); contact.setDesignation(newValue); break;
                case "city": oldValue = contact.getCity(); contact.setCity(newValue); break;
                case "country": oldValue = contact.getCountry(); contact.setCountry(newValue); break;
                case "status": oldValue = contact.getStatus(); contact.setStatus(newValue); break;
                case "tags": oldValue = contact.getTags(); contact.setTags(newValue); break;
            }
        }

        Contact saved = contactRepository.save(contact);

        // Audit log
        ContactAuditHistory history = new ContactAuditHistory();
        history.setContactId(contactId);
        history.setOrganization(contact.getOrganization());
        history.setFieldName(dto.getFieldName());
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setEditedBy(currentUser.getUserId());
        auditHistoryRepository.save(history);

        return contactMapper.toContactResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactAuditHistoryDto> getContactAuditHistory(CustomUserDetails currentUser, Long contactId) {
        List<ContactAuditHistory> list = auditHistoryRepository.findByContactIdAndOrganizationIdOrderByEditedAtDesc(contactId, currentUser.getOrganizationId());
        List<ContactAuditHistoryDto> dtos = new ArrayList<>();
        for (ContactAuditHistory h : list) {
            ContactAuditHistoryDto dto = new ContactAuditHistoryDto();
            dto.setId(h.getId());
            dto.setContactId(h.getContactId());
            dto.setOrganizationId(h.getOrganization().getId());
            dto.setFieldName(h.getFieldName());
            dto.setOldValue(h.getOldValue());
            dto.setNewValue(h.getNewValue());
            dto.setEditedBy(h.getEditedBy());
            dto.setEditedAt(h.getEditedAt());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ContactResponseDto restoreAuditHistory(CustomUserDetails currentUser, Long contactId, Long historyId) {
        ContactAuditHistory history = auditHistoryRepository.findById(historyId)
                .orElseThrow(() -> new CustomException("History record not found"));
        InlineCellEditRequestDto dto = new InlineCellEditRequestDto();
        dto.setFieldName(history.getFieldName());
        dto.setNewValue(history.getOldValue());
        return inlineCellEdit(currentUser, contactId, dto);
    }

    @Override
    public void undoImportBatch(CustomUserDetails currentUser, Long batchId) {
        contactRepository.deleteByOrganizationIdAndImportBatchId(currentUser.getOrganizationId(), batchId);
        importBatchRepository.deleteById(batchId);
    }

    @Override
    public void executeBulkAction(CustomUserDetails currentUser, BulkOperationRequestDto dto) {
        if (dto.getContactIds() == null || dto.getContactIds().isEmpty()) return;
        List<Contact> contacts = contactRepository.findAllByOrganizationIdAndIdInAndIsDeletedFalse(currentUser.getOrganizationId(), dto.getContactIds());

        if ("DELETE".equalsIgnoreCase(dto.getOperation())) {
            for (Contact c : contacts) {
                c.setIsDeleted(true);
            }
            contactRepository.saveAll(contacts);
        } else if ("ADD_TAG".equalsIgnoreCase(dto.getOperation()) && dto.getTag() != null) {
            for (Contact c : contacts) {
                String existing = c.getTags() != null ? c.getTags() : "";
                if (!existing.contains(dto.getTag())) {
                    c.setTags(existing.isEmpty() ? dto.getTag() : existing + "," + dto.getTag());
                }
            }
            contactRepository.saveAll(contacts);
        } else if ("CHANGE_STATUS".equalsIgnoreCase(dto.getOperation()) && dto.getStatus() != null) {
            for (Contact c : contacts) {
                c.setStatus(dto.getStatus());
            }
            contactRepository.saveAll(contacts);
        } else if ("MOVE_COLLECTION".equalsIgnoreCase(dto.getOperation()) && dto.getTargetCollectionId() != null) {
            for (Contact c : contacts) {
                c.setCollectionId(dto.getTargetCollectionId());
            }
            contactRepository.saveAll(contacts);
        } else if ("ADD_TO_CAMPAIGN".equalsIgnoreCase(dto.getOperation()) && dto.getTargetCampaignId() != null) {
            com.mailally.campaign.entity.Campaign campaign = campaignRepository.findById(dto.getTargetCampaignId()).orElse(null);
            if (campaign != null) {
                Organization org = organizationRepository.findById(currentUser.getOrganizationId()).orElse(null);
                for (Contact c : contacts) {
                    if (!campaignRecipientRepository.existsByCampaignIdAndContactId(campaign.getId(), c.getId())) {
                        try {
                            com.mailally.campaign.entity.CampaignRecipient recipient = new com.mailally.campaign.entity.CampaignRecipient();
                            recipient.setCampaign(campaign);
                            recipient.setContact(c);
                            recipient.setOrganization(org);
                            campaignRecipientRepository.save(recipient);
                        } catch (Exception ignored) {
                            // Safe fallback for duplicate entry DB constraints
                        }
                    }
                }
                long count = campaignRecipientRepository.countByCampaignId(campaign.getId());
                campaign.setTotalRecipients((int) count);
                campaignRepository.save(campaign);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getContactsByCollection(CustomUserDetails currentUser, Long collectionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Contact> contacts = contactRepository.findByOrganizationIdAndCollectionIdAndIsDeletedFalse(currentUser.getOrganizationId(), collectionId, pageable);
        return contacts.map(contactMapper::toContactResponseDto);
    }

    @Override
    @Transactional
    public void deleteCollection(CustomUserDetails currentUser, Long collectionId) {
        contactValidator.validateAdminOrManager(currentUser);
        ContactCollection collection = collectionRepository.findByIdAndOrganizationIdAndIsDeletedFalse(collectionId, currentUser.getOrganizationId())
                .orElse(null);

        List<Contact> collectionContacts = contactRepository.findByOrganizationIdAndCollectionIdAndIsDeletedFalse(currentUser.getOrganizationId(), collectionId);
        for (Contact c : collectionContacts) {
            c.setIsDeleted(true);
            c.setDeletedAt(LocalDateTime.now());
            c.setDeletedBy(currentUser.getUserId());
            c.setCollection(null);
            c.setCollectionId(null);
        }
        contactRepository.saveAll(collectionContacts);

        if (collection != null) {
            collection.setIsDeleted(true);
            collectionRepository.save(collection);
        }
    }
}
