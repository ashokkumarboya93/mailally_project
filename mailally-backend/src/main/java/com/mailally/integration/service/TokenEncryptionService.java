package com.mailally.integration.service;

import com.mailally.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TokenEncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey secretKey;

    public TokenEncryptionService(@Value("${google.oauth.encryption-secret:}") String secretProperty) {
        if (secretProperty == null || secretProperty.trim().isEmpty()) {
            // Secret not configured: system cannot safely encrypt tokens
            this.secretKey = null;
        } else {
            try {
                byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(secretProperty.getBytes(StandardCharsets.UTF_8));
                this.secretKey = new SecretKeySpec(keyBytes, "AES");
            } catch (Exception e) {
                throw new CustomException("Failed to initialize token encryption service: " + e.getMessage());
            }
        }
    }

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return null;
        }
        if (secretKey == null) {
            throw new CustomException("GOOGLE_TOKEN_ENCRYPTION_SECRET environment variable is missing. Cannot encrypt OAuth tokens.");
        }
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new CustomException("Failed to encrypt token: " + e.getMessage());
        }
    }

    public String decrypt(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return null;
        }
        if (secretKey == null) {
            throw new CustomException("GOOGLE_TOKEN_ENCRYPTION_SECRET environment variable is missing. Cannot decrypt OAuth tokens.");
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);

            if (decoded.length < IV_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted payload size");
            }

            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);

            byte[] cipherText = new byte[decoded.length - IV_LENGTH];
            System.arraycopy(decoded, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainTextBytes = cipher.doFinal(cipherText);
            return new String(plainTextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomException("Failed to decrypt token: " + e.getMessage());
        }
    }
}
