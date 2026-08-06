package com.mailally.notification.repository;

import com.mailally.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Notification} database operations.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndOrganizationIdAndUserIdAndIsDeletedFalse(Long id, Long organizationId, Long userId);

    Page<Notification> findByOrganizationIdAndUserIdAndIsDeletedFalse(Long organizationId, Long userId, Pageable pageable);

    Page<Notification> findByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(Long organizationId, Long userId, Pageable pageable);

    long countByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(Long organizationId, Long userId);

    long countByOrganizationIdAndUserIdAndIsDeletedFalse(Long organizationId, Long userId);

    long countByOrganizationIdAndUserIdAndStatusAndIsDeletedFalse(Long organizationId, Long userId, String status);

    long countByOrganizationIdAndUserIdAndPriorityAndIsDeletedFalse(Long organizationId, Long userId, String priority);

    long countByOrganizationIdAndUserIdAndTypeAndIsDeletedFalse(Long organizationId, Long userId, String type);

    @Query("SELECT n FROM Notification n WHERE n.organization.id = :organizationId " +
           "AND n.user.id = :userId AND n.isDeleted = false " +
           "AND (:type IS NULL OR n.type = :type) " +
           "AND (:priority IS NULL OR n.priority = :priority) " +
           "AND (:status IS NULL OR n.status = :status) " +
           "AND (:sourceModule IS NULL OR n.sourceModule = :sourceModule)")
    Page<Notification> filterNotifications(
            @Param("organizationId") Long organizationId,
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("priority") String priority,
            @Param("status") String status,
            @Param("sourceModule") String sourceModule,
            Pageable pageable
    );

    @Query("SELECT n FROM Notification n WHERE n.organization.id = :organizationId " +
           "AND n.user.id = :userId AND n.isDeleted = false " +
           "AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.message) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Notification> searchNotifications(
            @Param("organizationId") Long organizationId,
            @Param("userId") Long userId,
            @Param("query") String query,
            Pageable pageable
    );
}
