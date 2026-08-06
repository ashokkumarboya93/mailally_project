package com.mailally.segment.service.impl;

import com.mailally.contact.dto.ContactResponseDto;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.mapper.ContactMapper;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.segment.dto.CreateSegmentRequestDto;
import com.mailally.segment.dto.SegmentResponseDto;
import com.mailally.segment.dto.UpdateSegmentRequestDto;
import com.mailally.segment.entity.Segment;
import com.mailally.segment.mapper.SegmentMapper;
import com.mailally.segment.repository.SegmentRepository;
import com.mailally.segment.service.SegmentService;
import com.mailally.segment.validator.SegmentValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Segment management with tenant isolation,
 * rule-based contact preview, and soft deletion.
 */
@Service
@Transactional
public class SegmentServiceImpl implements SegmentService {

    private final SegmentRepository segmentRepository;
    private final OrganizationRepository organizationRepository;
    private final ContactRepository contactRepository;
    private final SegmentValidator segmentValidator;
    private final SegmentMapper segmentMapper;
    private final ContactMapper contactMapper;

    public SegmentServiceImpl(SegmentRepository segmentRepository,
                              OrganizationRepository organizationRepository,
                              ContactRepository contactRepository,
                              SegmentValidator segmentValidator,
                              SegmentMapper segmentMapper,
                              ContactMapper contactMapper) {
        this.segmentRepository = segmentRepository;
        this.organizationRepository = organizationRepository;
        this.contactRepository = contactRepository;
        this.segmentValidator = segmentValidator;
        this.segmentMapper = segmentMapper;
        this.contactMapper = contactMapper;
    }

    @Override
    public SegmentResponseDto createSegment(CustomUserDetails currentUser, CreateSegmentRequestDto dto) {
        segmentValidator.validateAdminOrManager(currentUser);
        segmentValidator.validateCreate(dto.getName(), dto.getType(), currentUser.getOrganizationId());

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Segment segment = segmentMapper.toSegmentEntity(dto, org, currentUser.getUserId());

        List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());
        segment.setContactCount(contacts.size());

        Segment saved = segmentRepository.save(segment);
        return segmentMapper.toSegmentResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SegmentResponseDto getSegmentById(CustomUserDetails currentUser, Long id) {
        Segment segment = segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Segment not found with ID: " + id));
        return segmentMapper.toSegmentResponseDto(segment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SegmentResponseDto> listSegments(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Segment> segmentsPage = segmentRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return segmentsPage.map(segmentMapper::toSegmentResponseDto);
    }

    @Override
    public SegmentResponseDto updateSegment(CustomUserDetails currentUser, Long id, UpdateSegmentRequestDto dto) {
        segmentValidator.validateAdminOrManager(currentUser);
        if (dto.getStatus() != null) segmentValidator.validateStatus(dto.getStatus());

        Segment segment = segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Segment not found with ID: " + id));

        segmentMapper.updateSegmentFromDto(segment, dto, currentUser.getUserId());
        Segment updated = segmentRepository.save(segment);
        return segmentMapper.toSegmentResponseDto(updated);
    }

    @Override
    public void softDeleteSegment(CustomUserDetails currentUser, Long id) {
        segmentValidator.validateAdminOrManager(currentUser);

        Segment segment = segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Segment not found with ID: " + id));

        segment.setIsDeleted(true);
        segment.setDeletedBy(currentUser.getUserId());
        segment.setDeletedAt(LocalDateTime.now());
        segmentRepository.save(segment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponseDto> previewSegmentContacts(CustomUserDetails currentUser, Long segmentId) {
        segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(segmentId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Segment not found with ID: " + segmentId));

        List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());
        return contacts.stream()
                .map(contactMapper::toContactResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SegmentResponseDto> searchSegments(CustomUserDetails currentUser, String name, String type,
                                                   String status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Segment> results = segmentRepository.searchSegments(
                currentUser.getOrganizationId(),
                (name != null && !name.isBlank()) ? name.trim() : null,
                (type != null && !type.isBlank()) ? type.trim().toUpperCase() : null,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                pageable
        );
        return results.map(segmentMapper::toSegmentResponseDto);
    }
}
