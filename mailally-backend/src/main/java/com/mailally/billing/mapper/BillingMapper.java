package com.mailally.billing.mapper;

import com.mailally.billing.dto.BillingResponseDto;
import com.mailally.billing.dto.InvoiceDto;
import com.mailally.billing.dto.PaymentDto;
import com.mailally.billing.dto.RefundDto;
import com.mailally.billing.entity.Billing;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Billing entities and DTOs.
 */
@Component
public class BillingMapper {

    public BillingResponseDto toBillingResponseDto(Billing billing) {
        if (billing == null) return null;
        return BillingResponseDto.builder()
                .id(billing.getId())
                .organizationId(billing.getOrganization() != null ? billing.getOrganization().getId() : null)
                .invoiceNumber(billing.getInvoiceNumber())
                .invoiceDate(billing.getInvoiceDate())
                .dueDate(billing.getDueDate())
                .paymentDate(billing.getPaymentDate())
                .currency(billing.getCurrency())
                .subtotal(billing.getSubtotal())
                .taxAmount(billing.getTaxAmount())
                .discountAmount(billing.getDiscountAmount())
                .totalAmount(billing.getTotalAmount())
                .paidAmount(billing.getPaidAmount())
                .balanceAmount(billing.getBalanceAmount())
                .paymentMethod(billing.getPaymentMethod())
                .paymentStatus(billing.getPaymentStatus())
                .transactionReference(billing.getTransactionReference())
                .billingAddress(billing.getBillingAddress())
                .billingEmail(billing.getBillingEmail())
                .notes(billing.getNotes())
                .createdAt(billing.getCreatedAt())
                .build();
    }

    public InvoiceDto toInvoiceDto(Billing billing) {
        if (billing == null) return null;
        return new InvoiceDto(
                billing.getInvoiceNumber(),
                billing.getInvoiceDate(),
                billing.getDueDate(),
                billing.getBillingEmail(),
                billing.getBillingAddress(),
                billing.getCurrency(),
                billing.getSubtotal() != null ? billing.getSubtotal() : 0.0,
                billing.getTaxAmount() != null ? billing.getTaxAmount() : 0.0,
                billing.getDiscountAmount() != null ? billing.getDiscountAmount() : 0.0,
                billing.getTotalAmount() != null ? billing.getTotalAmount() : 0.0,
                billing.getBalanceAmount() != null ? billing.getBalanceAmount() : 0.0,
                billing.getPaymentStatus()
        );
    }

    public PaymentDto toPaymentDto(Billing billing) {
        if (billing == null) return null;
        return new PaymentDto(
                billing.getInvoiceNumber(),
                billing.getPaidAmount() != null ? billing.getPaidAmount() : 0.0,
                billing.getPaymentMethod(),
                billing.getTransactionReference(),
                billing.getPaymentDate()
        );
    }

    public RefundDto toRefundDto(Billing billing, Double refundAmount, String reason) {
        if (billing == null) return null;
        return new RefundDto(
                billing.getInvoiceNumber(),
                refundAmount != null ? refundAmount : 0.0,
                reason,
                billing.getTransactionReference(),
                billing.getUpdatedAt()
        );
    }
}
