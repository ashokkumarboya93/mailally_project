package com.mailally.segment.service;

import com.mailally.contact.dto.ContactResponseDto;
import com.mailally.security.CustomUserDetails;
import com.mailally.segment.dto.CreateSegmentRequestDto;
import com.mailally.segment.dto.SegmentResponseDto;
import com.mailally.segment.dto.UpdateSegmentRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for Segment management operations.
 */
public interface SegmentService {

    SegmentResponseDto createSegment(CustomUserDetails currentUser, CreateSegmentRequestDto dto);

    SegmentResponseDto getSegmentById(CustomUserDetails currentUser, Long id);

    Page<SegmentResponseDto> listSegments(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    SegmentResponseDto updateSegment(CustomUserDetails currentUser, Long id, UpdateSegmentRequestDto dto);

    void softDeleteSegment(CustomUserDetails currentUser, Long id);

    List<ContactResponseDto> previewSegmentContacts(CustomUserDetails currentUser, Long segmentId);

    Page<SegmentResponseDto> searchSegments(CustomUserDetails currentUser, String name, String type, String status,
                                            int page, int size, String sortBy, String sortDir);
}
