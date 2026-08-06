package com.mailally.config;

import com.mailally.auth.entity.Auth;
import com.mailally.auth.repository.AuthRepository;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.subscription.entity.Subscription;
import com.mailally.subscription.repository.SubscriptionRepository;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DefaultDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultDataInitializer.class);

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultDataInitializer(UserRepository userRepository,
                                  AuthRepository authRepository,
                                  OrganizationRepository organizationRepository,
                                  SubscriptionRepository subscriptionRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.organizationRepository = organizationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            // 1. Ensure Default Subscription Plan
            Subscription subscription = subscriptionRepository.findAll().stream().findFirst()
                    .orElseGet(() -> subscriptionRepository.save(
                            Subscription.builder()
                                    .name("Enterprise Unlimited")
                                    .code("ENTERPRISE")
                                    .price(new BigDecimal("99.00"))
                                    .currency("USD")
                                    .maxContacts(1000000)
                                    .maxEmailsPerMonth(5000000)
                                    .maxUsers(50)
                                    .status("ACTIVE")
                                    .isDeleted(false)
                                    .build()
                    ));

            // 2. Ensure Default Organization
            Organization organization = organizationRepository.findAll().stream().findFirst()
                    .orElseGet(() -> organizationRepository.save(
                            Organization.builder()
                                    .subscription(subscription)
                                    .name("MailAlly Enterprise Technologies")
                                    .slug("mailally-enterprise")
                                    .status("ACTIVE")
                                    .isDeleted(false)
                                    .build()
                    ));

            // 3. Seed Default Admin User: admin@mailally.com / password123
            seedUserIfMissing("admin@mailally.com", "System", "Admin", "password123", "ADMIN", organization);

            // 4. Seed Secondary Test User: ashok@mailally.com / password123
            seedUserIfMissing("ashok@mailally.com", "Ashok", "Kumar", "password123", "ADMIN", organization);

            log.info("Successfully initialized default authentication credentials in database.");
        } catch (Exception e) {
            log.error("Failed to seed default authentication credentials: {}", e.getMessage(), e);
        }
    }

    private void seedUserIfMissing(String email, String firstName, String lastName, String rawPassword, String role, Organization org) {
        String cleanEmail = email.trim().toLowerCase();
        User user = userRepository.findByEmailAndIsDeletedFalse(cleanEmail).orElse(null);

        if (user == null) {
            user = User.builder()
                    .organization(org)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(cleanEmail)
                    .role(role)
                    .status("ACTIVE")
                    .isDeleted(false)
                    .build();
            user = userRepository.save(user);

            Auth auth = Auth.builder()
                    .user(user)
                    .passwordHash(passwordEncoder.encode(rawPassword))
                    .mfaEnabled(false)
                    .build();
            authRepository.save(auth);
            log.info("Seeded user credentials: {} / {}", cleanEmail, rawPassword);
        } else {
            // Update password hash to guarantee login works with password123
            Auth auth = authRepository.findByUser(user).orElse(null);
            if (auth == null) {
                auth = Auth.builder()
                        .user(user)
                        .passwordHash(passwordEncoder.encode(rawPassword))
                        .mfaEnabled(false)
                        .build();
                authRepository.save(auth);
            } else {
                auth.setPasswordHash(passwordEncoder.encode(rawPassword));
                authRepository.save(auth);
            }
            user.setStatus("ACTIVE");
            user.setIsDeleted(false);
            userRepository.save(user);
            log.info("Ensured active credentials for existing user: {}", cleanEmail);
        }
    }
}
