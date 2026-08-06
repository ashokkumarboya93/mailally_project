package com.mailally.billing.gateway;

import com.mailally.billing.entity.Billing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Offline / manual financial payment gateway adapter.
 */
@Component
public class OfflinePaymentGatewayAdapter implements PaymentGatewayAdapter {

    private static final Logger log = LoggerFactory.getLogger(OfflinePaymentGatewayAdapter.class);

    @Override
    public boolean supports(String gatewayName) {
        return gatewayName == null || gatewayName.isBlank() || "OFFLINE".equalsIgnoreCase(gatewayName) || "BANK_TRANSFER".equalsIgnoreCase(gatewayName);
    }

    @Override
    public Billing processPayment(Billing billing, Double amount, String transactionReference, String notes) {
        double currentPaid = billing.getPaidAmount() != null ? billing.getPaidAmount() : 0.0;
        double newPaid = currentPaid + amount;
        double newBalance = Math.max(0.0, billing.getTotalAmount() - newPaid);

        billing.setPaidAmount(newPaid);
        billing.setBalanceAmount(newBalance);
        billing.setPaymentDate(LocalDateTime.now());

        if (transactionReference != null && !transactionReference.isBlank()) {
            billing.setTransactionReference(transactionReference);
        }

        if (newBalance <= 0.001) {
            billing.setPaymentStatus("PAID");
        } else {
            billing.setPaymentStatus("PARTIALLY_PAID");
        }

        if (notes != null && !notes.isBlank()) {
            String existing = billing.getNotes() != null ? billing.getNotes() + "\n" : "";
            billing.setNotes(existing + "[Payment Record]: " + notes);
        }

        log.info("Processed offline payment of {} for Invoice {}", amount, billing.getInvoiceNumber());
        return billing;
    }
}
