package com.dta.exception;

public class ValidationException extends ApiException {
    // TODO [Timmy]: Use for request/body validation failures in API layer.
    public ValidationException(String message) {
        super(message);
    }
}
