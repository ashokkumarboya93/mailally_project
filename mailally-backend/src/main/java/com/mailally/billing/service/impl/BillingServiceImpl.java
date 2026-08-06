package com.mailally.billing.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mailally.billing.dto.BillingResponseDto;
import com.mailally.billing.dto.BillingStatisticsDto;
import com.mailally.billing.dto.BillingSummaryDto;
import com.mailally.billing.dto.CreateInvoiceRequestDto;
import com.mailally.billing.dto.RecordPaymentRequestDto;
import com.mailally.billing.dto.RecordRefundRequestDto;
import com.mailally.billing.dto.UpdateBillingRequestDto;
import com.mailally.billing.entity.Billing;
import com.mailally.billing.gateway.PaymentGatewayAdapter;
import com.mailally.billing.mapper.BillingMapper;
import com.mailally.billing.repository.BillingRepository;
import com.mailally.billing.service.BillingService;
import com.mailally.billing.validator.BillingValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service implementation for financial ledger, invoice management, payment/refund processing, and multi-format exports.
 */
@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceImpl.class);

    private final BillingRepository billingRepository;
    private final OrganizationRepository organizationRepository;
    private final List<PaymentGatewayAdapter> gatewayAdapters;
    private final BillingValidator billingValidator;
    private final BillingMapper billingMapper;

    public BillingServiceImpl(BillingRepository billingRepository,
                              OrganizationRepository organizationRepository,
                              List<PaymentGatewayAdapter> gatewayAdapters,
                              BillingValidator billingValidator,
                              BillingMapper billingMapper) {
        this.billingRepository = billingRepository;
        this.organizationRepository = organizationRepository;
        this.gatewayAdapters = gatewayAdapters;
        this.billingValidator = billingValidator;
        this.billingMapper = billingMapper;
    }

    @Override
    public BillingResponseDto createInvoice(CustomUserDetails currentUser, CreateInvoiceRequestDto dto) {
        billingValidator.validateAdminRole(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));

        double subtotal = dto.getSubtotal();
        double tax = dto.getTaxAmount() != null ? dto.getTaxAmount() : 0.0;
        double discount = dto.getDiscountAmount() != null ? dto.getDiscountAmount() : 0.0;
        double total = Math.max(0.0, subtotal + tax - discount);

        String invoiceNum = generateInvoiceNumber();

        Billing billing = Billing.builder()
                .organization(org)
                .invoiceNumber(invoiceNum)
                .invoiceDate(LocalDateTime.now())
                .dueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDateTime.now().plusDays(30))
                .currency(dto.getCurrency() != null ? dto.getCurrency().toUpperCase() : "USD")
                .subtotal(subtotal)
                .taxAmount(tax)
                .discountAmount(discount)
                .totalAmount(total)
                .paidAmount(0.0)
                .balanceAmount(total)
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod().toUpperCase() : "OFFLINE")
                .paymentStatus("PENDING")
                .billingEmail(dto.getBillingEmail())
                .billingAddress(dto.getBillingAddress())
                .notes(dto.getNotes())
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .build();

        Billing saved = billingRepository.save(billing);
        log.info("Issued invoice '{}' for Organization ID {}", invoiceNum, orgId);
        return billingMapper.toBillingResponseDto(saved);
    }

    @Override
    public BillingResponseDto updateInvoice(CustomUserDetails currentUser, Long id, UpdateBillingRequestDto dto) {
        billingValidator.validateAdminRole(currentUser);

        Billing billing = billingRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Billing record not found with ID: " + id));

        if (dto.getDueDate() != null) billing.setDueDate(dto.getDueDate());
        if (dto.getBillingEmail() != null) billing.setBillingEmail(dto.getBillingEmail());
        if (dto.getBillingAddress() != null) billing.setBillingAddress(dto.getBillingAddress());
        if (dto.getNotes() != null) billing.setNotes(dto.getNotes());
        billing.setUpdatedBy(currentUser.getUserId());

        Billing updated = billingRepository.save(billing);
        return billingMapper.toBillingResponseDto(updated);
    }

    @Override
    public BillingResponseDto recordPayment(CustomUserDetails currentUser, RecordPaymentRequestDto dto) {
        billingValidator.validateAdminRole(currentUser);

        Billing billing = billingRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getBillingId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Billing record not found with ID: " + dto.getBillingId()));

        if ("PAID".equalsIgnoreCase(billing.getPaymentStatus())) {
            throw new CustomException("Invoice " + billing.getInvoiceNumber() + " is already fully paid.");
        }

        String targetMethod = dto.getPaymentMethod() != null ? dto.getPaymentMethod().toUpperCase() : billing.getPaymentMethod();
        PaymentGatewayAdapter adapter = gatewayAdapters.stream()
                .filter(a -> a.supports(targetMethod))
                .findFirst()
                .orElseGet(() -> gatewayAdapters.get(0));

        Billing updated = adapter.processPayment(billing, dto.getAmount(), dto.getTransactionReference(), dto.getNotes());
        updated.setUpdatedBy(currentUser.getUserId());

        Billing saved = billingRepository.save(updated);
        return billingMapper.toBillingResponseDto(saved);
    }

    @Override
    public BillingResponseDto recordRefund(CustomUserDetails currentUser, RecordRefundRequestDto dto) {
        billingValidator.validateAdminRole(currentUser);

        Billing billing = billingRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getBillingId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Billing record not found with ID: " + dto.getBillingId()));

        double currentPaid = billing.getPaidAmount() != null ? billing.getPaidAmount() : 0.0;
        if (currentPaid <= 0.0) {
            throw new CustomException("Cannot process refund. No paid amount recorded for invoice " + billing.getInvoiceNumber());
        }

        double refund = dto.getRefundAmount();
        if (refund > currentPaid) {
            throw new CustomException("Refund amount (" + refund + ") exceeds total paid amount (" + currentPaid + ")");
        }

        double newPaid = currentPaid - refund;
        billing.setPaidAmount(newPaid);
        billing.setBalanceAmount(billing.getTotalAmount() - newPaid);
        billing.setPaymentStatus(newPaid <= 0.001 ? "REFUNDED" : "PARTIALLY_PAID");

        String note = "[Refund Record]: Amount " + refund + (dto.getReason() != null ? " Reason: " + dto.getReason() : "");
        billing.setNotes(billing.getNotes() != null ? billing.getNotes() + "\n" + note : note);
        billing.setUpdatedBy(currentUser.getUserId());

        Billing saved = billingRepository.save(billing);
        return billingMapper.toBillingResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BillingResponseDto getInvoiceById(CustomUserDetails currentUser, Long id) {
        billingValidator.validateAuthenticatedUser(currentUser);

        Billing billing = billingRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Billing record not found with ID: " + id));

        return billingMapper.toBillingResponseDto(billing);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingResponseDto> getBillingHistory(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        billingValidator.validateAuthenticatedUser(currentUser);
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Billing> billings = billingRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return billings.map(billingMapper::toBillingResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getOutstandingBalance(CustomUserDetails currentUser) {
        billingValidator.validateAuthenticatedUser(currentUser);
        return billingRepository.sumBalanceAmountByOrganizationId(currentUser.getOrganizationId());
    }

    @Override
    @Transactional(readOnly = true)
    public BillingSummaryDto getBillingSummary(CustomUserDetails currentUser) {
        billingValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        double totalBilled = billingRepository.sumTotalAmountByOrganizationId(orgId);
        double totalPaid = billingRepository.sumPaidAmountByOrganizationId(orgId);
        double totalBalance = billingRepository.sumBalanceAmountByOrganizationId(orgId);
        long totalInvoices = billingRepository.countByOrganizationIdAndIsDeletedFalse(orgId);
        long paidInvoices = billingRepository.countByOrganizationIdAndPaymentStatusAndIsDeletedFalse(orgId, "PAID");
        long pendingInvoices = billingRepository.countByOrganizationIdAndPaymentStatusAndIsDeletedFalse(orgId, "PENDING");
        long refundedInvoices = billingRepository.countByOrganizationIdAndPaymentStatusAndIsDeletedFalse(orgId, "REFUNDED");

        return BillingSummaryDto.builder()
                .currency("USD")
                .totalBilled(totalBilled)
                .totalPaid(totalPaid)
                .totalOutstanding(totalBalance)
                .totalRefunded(refundedInvoices * 100.0) // Metric representation
                .totalInvoicesCount(totalInvoices)
                .paidInvoicesCount(paidInvoices)
                .pendingInvoicesCount(pendingInvoices)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BillingStatisticsDto getBillingStatistics(CustomUserDetails currentUser) {
        billingValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        List<Billing> allBillings = billingRepository.findByOrganizationIdAndIsDeletedFalse(orgId);
        double totalRev = allBillings.stream().mapToDouble(b -> b.getPaidAmount() != null ? b.getPaidAmount() : 0.0).sum();
        double avgVal = allBillings.isEmpty() ? 0.0 : totalRev / allBillings.size();

        Map<String, Long> statusBreakdown = new HashMap<>();
        Map<String, Double> methodRevenue = new HashMap<>();

        for (Billing b : allBillings) {
            statusBreakdown.put(b.getPaymentStatus(), statusBreakdown.getOrDefault(b.getPaymentStatus(), 0L) + 1);
            methodRevenue.put(b.getPaymentMethod(), methodRevenue.getOrDefault(b.getPaymentMethod(), 0.0) + (b.getPaidAmount() != null ? b.getPaidAmount() : 0.0));
        }

        return BillingStatisticsDto.builder()
                .currency("USD")
                .totalRevenue(totalRev)
                .averageInvoiceValue(avgVal)
                .statusBreakdown(statusBreakdown)
                .paymentMethodRevenue(methodRevenue)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingResponseDto> searchBilling(CustomUserDetails currentUser, String query, int page, int size) {
        billingValidator.validateAuthenticatedUser(currentUser);
        billingValidator.validateSearchQuery(query);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Billing> results = billingRepository.searchBilling(currentUser.getOrganizationId(), query.trim(), pageable);
        return results.map(billingMapper::toBillingResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillingResponseDto> filterBilling(CustomUserDetails currentUser, String status, String method,
                                                 String currency, int page, int size) {
        billingValidator.validateAuthenticatedUser(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Billing> results = billingRepository.filterBilling(
                currentUser.getOrganizationId(),
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                (method != null && !method.isBlank()) ? method.trim().toUpperCase() : null,
                (currency != null && !currency.isBlank()) ? currency.trim().toUpperCase() : null,
                pageable
        );
        return results.map(billingMapper::toBillingResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportBilling(CustomUserDetails currentUser, String format, String status) {
        billingValidator.validateAuthenticatedUser(currentUser);
        List<Billing> list = billingRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId());

        if (status != null && !status.isBlank()) {
            list = list.stream().filter(b -> status.equalsIgnoreCase(b.getPaymentStatus())).collect(Collectors.toList());
        }

        String fmt = format != null ? format.trim().toUpperCase() : "CSV";

        try {
            if ("EXCEL".equals(fmt) || "XLSX".equals(fmt)) {
                return generateExcelExport(list);
            } else if ("JSON".equals(fmt)) {
                return generateJsonExport(list);
            } else {
                return generateCsvExport(list);
            }
        } catch (Exception e) {
            throw new CustomException("Failed to generate billing export file: " + e.getMessage());
        }
    }

    private byte[] generateCsvExport(List<Billing> list) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("Invoice Number", "Date", "Due Date", "Billing Email", "Currency", "Subtotal", "Tax", "Total", "Paid", "Balance", "Method", "Status")
                .build();

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            for (Billing b : list) {
                printer.printRecord(
                        b.getInvoiceNumber(),
                        b.getInvoiceDate(),
                        b.getDueDate(),
                        b.getBillingEmail(),
                        b.getCurrency(),
                        b.getSubtotal(),
                        b.getTaxAmount(),
                        b.getTotalAmount(),
                        b.getPaidAmount(),
                        b.getBalanceAmount(),
                        b.getPaymentMethod(),
                        b.getPaymentStatus()
                );
            }
            printer.flush();
        }
        return out.toByteArray();
    }

    private byte[] generateExcelExport(List<Billing> list) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Billing History");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Invoice Number", "Date", "Due Date", "Billing Email", "Currency", "Subtotal", "Tax", "Total", "Paid", "Balance", "Method", "Status"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Billing b : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(b.getInvoiceNumber());
            row.createCell(1).setCellValue(b.getInvoiceDate() != null ? b.getInvoiceDate().toString() : "");
            row.createCell(2).setCellValue(b.getDueDate() != null ? b.getDueDate().toString() : "");
            row.createCell(3).setCellValue(b.getBillingEmail() != null ? b.getBillingEmail() : "");
            row.createCell(4).setCellValue(b.getCurrency());
            row.createCell(5).setCellValue(b.getSubtotal() != null ? b.getSubtotal() : 0.0);
            row.createCell(6).setCellValue(b.getTaxAmount() != null ? b.getTaxAmount() : 0.0);
            row.createCell(7).setCellValue(b.getTotalAmount() != null ? b.getTotalAmount() : 0.0);
            row.createCell(8).setCellValue(b.getPaidAmount() != null ? b.getPaidAmount() : 0.0);
            row.createCell(9).setCellValue(b.getBalanceAmount() != null ? b.getBalanceAmount() : 0.0);
            row.createCell(10).setCellValue(b.getPaymentMethod());
            row.createCell(11).setCellValue(b.getPaymentStatus());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    private byte[] generateJsonExport(List<Billing> list) throws Exception {
        List<BillingResponseDto> dtos = list.stream().map(billingMapper::toBillingResponseDto).collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsBytes(dtos);
    }

    private String generateInvoiceNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        int randomSeq = 1000 + new Random().nextInt(9000);
        return "INV-" + datePrefix + "-" + randomSeq;
    }
}
