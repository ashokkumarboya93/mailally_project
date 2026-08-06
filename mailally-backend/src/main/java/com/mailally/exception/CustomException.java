package com.mailally.exception;

/**
 * Base custom runtime exception class for application-specific exceptions.
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
