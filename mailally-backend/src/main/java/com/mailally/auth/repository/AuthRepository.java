package com.mailally.auth.repository;

import com.mailally.auth.entity.Auth;
import com.mailally.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Auth} credentials database operations.
 */
@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUser(User user);

    Optional<Auth> findByUserId(Long userId);

    Optional<Auth> findByPasswordResetToken(String token);
}

