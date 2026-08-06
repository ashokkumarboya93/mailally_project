package com.mailally.billing.controller;

import com.mailally.billing.dto.BillingResponseDto;
import com.mailally.billing.dto.BillingStatisticsDto;
import com.mailally.billing.dto.BillingSummaryDto;
import com.mailally.billing.dto.CreateInvoiceRequestDto;
import com.mailally.billing.dto.RecordPaymentRequestDto;
import com.mailally.billing.dto.RecordRefundRequestDto;
import com.mailally.billing.dto.UpdateBillingRequestDto;
import com.mailally.billing.service.BillingService;
import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

/**
 * REST Controller for MailAlly Financial Ledger and Billing APIs.
 */
@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/invoice")
    public ResponseEntity<ApiResponse<BillingResponseDto>> createInvoice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateInvoiceRequestDto dto) {
        BillingResponseDto result = billingService.createInvoice(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<BillingResponseDto>builder()
                .success(true).message("Invoice issued successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<ApiResponse<BillingResponseDto>> updateInvoice(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody UpdateBillingRequestDto dto) {
        BillingResponseDto result = billingService.updateInvoice(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<BillingResponseDto>builder()
                .success(true).message("Invoice details updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<ApiResponse<BillingResponseDto>> getInvoiceById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        BillingResponseDto result = billingService.getInvoiceById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<BillingResponseDto>builder()
                .success(true).message("Invoice details retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<BillingResponseDto>>> getBillingHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<BillingResponseDto> history = billingService.getBillingHistory(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<BillingResponseDto>>builder()
                .success(true).message("Billing history retrieved successfully").data(history).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<BillingResponseDto>> recordPayment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody RecordPaymentRequestDto dto) {
        BillingResponseDto result = billingService.recordPayment(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<BillingResponseDto>builder()
                .success(true).message("Payment transaction recorded successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<BillingResponseDto>> recordRefund(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody RecordRefundRequestDto dto) {
        BillingResponseDto result = billingService.recordRefund(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<BillingResponseDto>builder()
                .success(true).message("Refund transaction recorded successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<BillingSummaryDto>> getBillingSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        BillingSummaryDto summary = billingService.getBillingSummary(userDetails);
        return ResponseEntity.ok(ApiResponse.<BillingSummaryDto>builder()
                .success(true).message("Billing summary retrieved").data(summary).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<BillingStatisticsDto>> getBillingStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        BillingStatisticsDto stats = billingService.getBillingStatistics(userDetails);
        return ResponseEntity.ok(ApiResponse.<BillingStatisticsDto>builder()
                .success(true).message("Billing statistics retrieved").data(stats).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BillingResponseDto>>> searchBilling(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BillingResponseDto> results = billingService.searchBilling(userDetails, query, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<BillingResponseDto>>builder()
                .success(true).message("Billing search completed").data(results).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBilling(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "CSV") String format,
            @RequestParam(required = false) String status) {

        byte[] exportData = billingService.exportBilling(userDetails, format, status);

        String filename = "billing_export_" + System.currentTimeMillis();
        MediaType mediaType;

        if ("EXCEL".equalsIgnoreCase(format) || "XLSX".equalsIgnoreCase(format)) {
            filename += ".xlsx";
            mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if ("JSON".equalsIgnoreCase(format)) {
            filename += ".json";
            mediaType = MediaType.APPLICATION_JSON;
        } else {
            filename += ".csv";
            mediaType = MediaType.parseMediaType("text/csv");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(exportData);
    }
}
