package com.mailally.contact.repository;

import com.mailally.contact.dto.DomainStatDto;
import com.mailally.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Contact} entity database operations.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    boolean existsByOrganizationIdAndEmailAndIsDeletedFalse(Long organizationId, String email);

    Optional<Contact> findByOrganizationIdAndEmailAndIsDeletedFalse(Long organizationId, String email);

    List<Contact> findByEmailAndIsDeletedFalse(String email);

    Optional<Contact> findByOrganizationIdAndEmail(Long organizationId, String email);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);

    Optional<Contact> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Page<Contact> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    List<Contact> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    List<Contact> findAllByOrganizationIdAndIdInAndIsDeletedFalse(Long organizationId, List<Long> ids);

    List<Contact> findByOrganizationIdAndImportBatchIdAndIsDeletedFalse(Long organizationId, Long importBatchId);

    Page<Contact> findByOrganizationIdAndCollectionIdAndIsDeletedFalse(Long organizationId, Long collectionId, Pageable pageable);

    List<Contact> findByOrganizationIdAndCollectionIdAndIsDeletedFalse(Long organizationId, Long collectionId);

    long countByOrganizationIdAndCollectionIdAndIsDeletedFalse(Long organizationId, Long collectionId);

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.organization.id = :organizationId AND c.importBatchId = :importBatchId")
    void deleteByOrganizationIdAndImportBatchId(@Param("organizationId") Long organizationId, @Param("importBatchId") Long importBatchId);

    @Query("SELECT c FROM Contact c WHERE c.organization.id = :organizationId " +
           "AND c.isDeleted = false " +
           "AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
           "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
           "AND (:company IS NULL OR LOWER(c.company) LIKE LOWER(CONCAT('%', :company, '%'))) " +
           "AND (:phone IS NULL OR c.phone LIKE CONCAT('%', :phone, '%')) " +
           "AND (:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%'))) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:tag IS NULL OR LOWER(c.tags) LIKE LOWER(CONCAT('%', :tag, '%')))")
    Page<Contact> searchContacts(
            @Param("organizationId") Long organizationId,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("company") String company,
            @Param("phone") String phone,
            @Param("city") String city,
            @Param("country") String country,
            @Param("status") String status,
            @Param("tag") String tag,
            Pageable pageable
    );

    @Query("SELECT c FROM Contact c WHERE c.organization.id = :organizationId " +
           "AND c.isDeleted = false " +
           "AND (:query IS NULL OR (" +
           "   LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.city) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.country) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "   LOWER(c.tags) LIKE LOWER(CONCAT('%', :query, '%'))" +
           "))")
    Page<Contact> searchAllFields(@Param("organizationId") Long organizationId, @Param("query") String query, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.organization.id = :organizationId " +
           "AND c.isDeleted = false " +
           "AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
           "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
           "AND (:company IS NULL OR LOWER(c.company) LIKE LOWER(CONCAT('%', :company, '%'))) " +
           "AND (:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%'))) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:tag IS NULL OR LOWER(c.tags) LIKE LOWER(CONCAT('%', :tag, '%'))) " +
           "AND (:importBatchId IS NULL OR c.importBatchId = :importBatchId) " +
           "AND (:emailDomain IS NULL OR LOWER(c.emailDomain) = LOWER(:emailDomain))")
    Page<Contact> filterContacts(
            @Param("organizationId") Long organizationId,
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("company") String company,
            @Param("city") String city,
            @Param("country") String country,
            @Param("status") String status,
            @Param("tag") String tag,
            @Param("importBatchId") Long importBatchId,
            @Param("emailDomain") String emailDomain,
            Pageable pageable
    );

    @Query("SELECT new com.mailally.contact.dto.DomainStatDto(c.emailDomain, COUNT(c)) " +
           "FROM Contact c WHERE c.organization.id = :organizationId AND c.isDeleted = false AND c.emailDomain IS NOT NULL " +
           "GROUP BY c.emailDomain ORDER BY COUNT(c) DESC")
    List<DomainStatDto> findDomainStatsByOrganizationId(@Param("organizationId") Long organizationId);
}
