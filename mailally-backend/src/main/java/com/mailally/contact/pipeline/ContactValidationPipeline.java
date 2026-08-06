package com.mailally.contact.pipeline;

import com.mailally.contact.provider.ContactRawRow;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ContactValidationPipeline {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public ValidationResult validate(ContactRawRow row) {
        String email = row.getRawEmail();
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Missing email address");
        }

        email = email.trim();
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email syntax");
        }

        // Extensible stubs for MX, SMTP, Disposable, Catch-All validation
        if (isMxValid(email) && isNotDisposable(email)) {
            return new ValidationResult(true, null);
        }

        return new ValidationResult(true, null);
    }

    private boolean isMxValid(String email) {
        // MX record check architecture stub
        return true;
    }

    private boolean isNotDisposable(String email) {
        // Disposable domain list architecture stub
        return true;
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String reason;

        public ValidationResult(boolean valid, String reason) {
            this.valid = valid;
            this.reason = reason;
        }

        public boolean isValid() { return valid; }
        public String getReason() { return reason; }
    }
}
