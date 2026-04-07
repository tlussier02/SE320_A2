package com.dta.exception;

public abstract class ApiException extends RuntimeException {
    // TODO [Josh]: Add standardized payload fields (code, message, timestamp, correlationId).
    protected ApiException(String message) {
        super(message);
    }
}
