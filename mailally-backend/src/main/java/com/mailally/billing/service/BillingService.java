package com.mailally.billing.service;

import com.mailally.billing.dto.BillingResponseDto;
import com.mailally.billing.dto.BillingStatisticsDto;
import com.mailally.billing.dto.BillingSummaryDto;
import com.mailally.billing.dto.CreateInvoiceRequestDto;
import com.mailally.billing.dto.RecordPaymentRequestDto;
import com.mailally.billing.dto.RecordRefundRequestDto;
import com.mailally.billing.dto.UpdateBillingRequestDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;

/**
 * Service interface for financial invoice ledger, payment processing, refund recording, summaries, and exports.
 */
public interface BillingService {

    BillingResponseDto createInvoice(CustomUserDetails currentUser, CreateInvoiceRequestDto dto);

    BillingResponseDto updateInvoice(CustomUserDetails currentUser, Long id, UpdateBillingRequestDto dto);

    BillingResponseDto recordPayment(CustomUserDetails currentUser, RecordPaymentRequestDto dto);

    BillingResponseDto recordRefund(CustomUserDetails currentUser, RecordRefundRequestDto dto);

    BillingResponseDto getInvoiceById(CustomUserDetails currentUser, Long id);

    Page<BillingResponseDto> getBillingHistory(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    Double getOutstandingBalance(CustomUserDetails currentUser);

    BillingSummaryDto getBillingSummary(CustomUserDetails currentUser);

    BillingStatisticsDto getBillingStatistics(CustomUserDetails currentUser);

    Page<BillingResponseDto> searchBilling(CustomUserDetails currentUser, String query, int page, int size);

    Page<BillingResponseDto> filterBilling(CustomUserDetails currentUser, String status, String method, String currency, int page, int size);

    byte[] exportBilling(CustomUserDetails currentUser, String format, String status);
}
