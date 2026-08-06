package com.mailally.user.repository;

import com.mailally.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);

    Optional<User> findByIdAndIsDeletedFalse(Long id);

    Optional<User> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Page<User> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.organization.id = :organizationId " +
           "AND u.isDeleted = false " +
           "AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:name IS NULL OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:role IS NULL OR u.role = :role) " +
           "AND (:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(
            @Param("organizationId") Long organizationId,
            @Param("email") String email,
            @Param("name") String name,
            @Param("role") String role,
            @Param("status") String status,
            Pageable pageable
    );
}
