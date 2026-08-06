package com.mailally.segment.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.contact.dto.ContactResponseDto;
import com.mailally.security.CustomUserDetails;
import com.mailally.segment.dto.CreateSegmentRequestDto;
import com.mailally.segment.dto.SegmentResponseDto;
import com.mailally.segment.dto.UpdateSegmentRequestDto;
import com.mailally.segment.service.SegmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import java.util.List;

/**
 * REST Controller for Segment Management APIs.
 */
@RestController
@RequestMapping("/api/v1/segments")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SegmentResponseDto>> createSegment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateSegmentRequestDto dto) {
        SegmentResponseDto result = segmentService.createSegment(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<SegmentResponseDto>builder()
                .success(true).message("Segment created successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SegmentResponseDto>> getSegmentById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        SegmentResponseDto result = segmentService.getSegmentById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<SegmentResponseDto>builder()
                .success(true).message("Segment retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SegmentResponseDto>>> listSegments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<SegmentResponseDto> result = segmentService.listSegments(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<SegmentResponseDto>>builder()
                .success(true).message("Segments retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SegmentResponseDto>> updateSegment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateSegmentRequestDto dto) {
        SegmentResponseDto result = segmentService.updateSegment(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<SegmentResponseDto>builder()
                .success(true).message("Segment updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteSegment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        segmentService.softDeleteSegment(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Segment soft deleted successfully").data(null).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<ApiResponse<List<ContactResponseDto>>> previewContacts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        List<ContactResponseDto> contacts = segmentService.previewSegmentContacts(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<List<ContactResponseDto>>builder()
                .success(true).message("Segment contacts preview generated").data(contacts).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SegmentResponseDto>>> searchSegments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<SegmentResponseDto> result = segmentService.searchSegments(userDetails, name, type, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<SegmentResponseDto>>builder()
                .success(true).message("Segment search completed").data(result).timestamp(LocalDateTime.now()).build());
    }
}
