package com.mailally.audit.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mailally.audit.dto.AuditResponseDto;
import com.mailally.audit.dto.CreateAuditRequestDto;
import com.mailally.audit.entity.Audit;
import com.mailally.audit.mapper.AuditMapper;
import com.mailally.audit.repository.AuditRepository;
import com.mailally.audit.service.AuditService;
import com.mailally.audit.validator.AuditValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for logging system events, searching, filtering, and exporting audit trails.
 */
@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);

    private final AuditRepository auditRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final AuditValidator auditValidator;
    private final AuditMapper auditMapper;

    public AuditServiceImpl(AuditRepository auditRepository,
                            OrganizationRepository organizationRepository,
                            UserRepository userRepository,
                            AuditValidator auditValidator,
                            AuditMapper auditMapper) {
        this.auditRepository = auditRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.auditValidator = auditValidator;
        this.auditMapper = auditMapper;
    }

    @Override
    public AuditResponseDto logEvent(CustomUserDetails currentUser, CreateAuditRequestDto dto) {
        auditValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));

        User user = userRepository.findById(currentUser.getUserId()).orElse(null);

        Audit audit = Audit.builder()
                .organization(org)
                .user(user)
                .action(dto.getAction())
                .module(dto.getModule() != null ? dto.getModule().toUpperCase() : "SYSTEM")
                .description(dto.getDescription())
                .ipAddress(dto.getIpAddress())
                .browser(dto.getBrowser())
                .timestamp(LocalDateTime.now())
                .success(dto.getSuccess() != null ? dto.getSuccess() : true)
                .failureReason(dto.getFailureReason())
                .referenceId(dto.getReferenceId())
                .build();

        Audit saved = auditRepository.save(audit);
        log.info("Logged Audit Event '{}' for User ID {}", dto.getAction(), currentUser.getUserId());
        return auditMapper.toAuditResponseDto(saved);
    }

    @Override
    public void logEventInternal(Long organizationId, Long userId, String action, String module, String description, Boolean success) {
        if (organizationId == null) return;
        Organization org = organizationRepository.findById(organizationId).orElse(null);
        if (org == null) return;

        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;

        Audit audit = Audit.builder()
                .organization(org)
                .user(user)
                .action(action)
                .module(module != null ? module.toUpperCase() : "SYSTEM")
                .description(description)
                .timestamp(LocalDateTime.now())
                .success(success != null ? success : true)
                .build();

        auditRepository.save(audit);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponseDto> getUserAuditLogs(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        auditValidator.validateAuthenticatedUser(currentUser);
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Audit> audits = auditRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return audits.map(auditMapper::toAuditResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponseDto> searchAuditLogs(CustomUserDetails currentUser, String query, int page, int size) {
        auditValidator.validateAuthenticatedUser(currentUser);
        auditValidator.validateSearchQuery(query);
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        Page<Audit> results = auditRepository.searchAuditLogs(currentUser.getOrganizationId(), query.trim(), pageable);
        return results.map(auditMapper::toAuditResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponseDto> filterAuditLogs(CustomUserDetails currentUser, String module, String action,
                                                 Boolean success, int page, int size) {
        auditValidator.validateAuthenticatedUser(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        Page<Audit> results = auditRepository.filterAuditLogs(
                currentUser.getOrganizationId(),
                (module != null && !module.isBlank()) ? module.trim().toUpperCase() : null,
                (action != null && !action.isBlank()) ? action.trim() : null,
                success,
                pageable
        );
        return results.map(auditMapper::toAuditResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportAuditLogs(CustomUserDetails currentUser, String format, String module) {
        auditValidator.validateAuthenticatedUser(currentUser);
        List<Audit> list = auditRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());

        if (module != null && !module.isBlank()) {
            list = list.stream().filter(a -> module.equalsIgnoreCase(a.getModule())).collect(Collectors.toList());
        }

        String fmt = format != null ? format.trim().toUpperCase() : "CSV";

        try {
            if ("JSON".equals(fmt)) {
                return generateJsonExport(list);
            } else {
                return generateCsvExport(list);
            }
        } catch (Exception e) {
            throw new CustomException("Failed to export audit logs: " + e.getMessage());
        }
    }

    private byte[] generateCsvExport(List<Audit> list) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "Timestamp", "User Email", "Module", "Action", "Description", "Success", "IP Address")
                .build();

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), csvFormat)) {
            for (Audit a : list) {
                printer.printRecord(
                        a.getId(),
                        a.getTimestamp(),
                        a.getUser() != null ? a.getUser().getEmail() : "System",
                        a.getModule(),
                        a.getAction(),
                        a.getDescription(),
                        a.getSuccess(),
                        a.getIpAddress()
                );
            }
            printer.flush();
        }
        return out.toByteArray();
    }

    private byte[] generateJsonExport(List<Audit> list) throws Exception {
        List<AuditResponseDto> dtos = list.stream().map(auditMapper::toAuditResponseDto).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsBytes(dtos);
    }
}
