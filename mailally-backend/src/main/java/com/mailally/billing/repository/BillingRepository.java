package com.mailally.billing.repository;

import com.mailally.billing.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Billing} financial database operations.
 */
@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    Optional<Billing> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Optional<Billing> findByOrganizationIdAndInvoiceNumberAndIsDeletedFalse(Long organizationId, String invoiceNumber);

    Page<Billing> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    List<Billing> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);

    long countByOrganizationIdAndPaymentStatusAndIsDeletedFalse(Long organizationId, String paymentStatus);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0.0) FROM Billing b WHERE b.organization.id = :organizationId AND b.isDeleted = false")
    Double sumTotalAmountByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT COALESCE(SUM(b.paidAmount), 0.0) FROM Billing b WHERE b.organization.id = :organizationId AND b.isDeleted = false")
    Double sumPaidAmountByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT COALESCE(SUM(b.balanceAmount), 0.0) FROM Billing b WHERE b.organization.id = :organizationId AND b.isDeleted = false")
    Double sumBalanceAmountByOrganizationId(@Param("organizationId") Long organizationId);

    @Query("SELECT b FROM Billing b WHERE b.organization.id = :organizationId AND b.isDeleted = false " +
           "AND (:status IS NULL OR b.paymentStatus = :status) " +
           "AND (:method IS NULL OR b.paymentMethod = :method) " +
           "AND (:currency IS NULL OR b.currency = :currency)")
    Page<Billing> filterBilling(
            @Param("organizationId") Long organizationId,
            @Param("status") String status,
            @Param("method") String method,
            @Param("currency") String currency,
            Pageable pageable
    );

    @Query("SELECT b FROM Billing b WHERE b.organization.id = :organizationId AND b.isDeleted = false " +
           "AND (LOWER(b.invoiceNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.billingEmail) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Billing> searchBilling(
            @Param("organizationId") Long organizationId,
            @Param("query") String query,
            Pageable pageable
    );
}
