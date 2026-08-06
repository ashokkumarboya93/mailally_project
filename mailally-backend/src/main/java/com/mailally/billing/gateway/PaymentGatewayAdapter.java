package com.mailally.billing.gateway;

import com.mailally.billing.entity.Billing;

/**
 * Strategy interface for payment gateway adapters (OFFLINE, STRIPE, RAZORPAY, PAYPAL, etc.).
 */
public interface PaymentGatewayAdapter {

    /**
     * Checks if this gateway adapter handles the specified gateway or payment method name.
     */
    boolean supports(String gatewayName);

    /**
     * Processes financial payment transaction.
     */
    Billing processPayment(Billing billing, Double amount, String transactionReference, String notes);
}
