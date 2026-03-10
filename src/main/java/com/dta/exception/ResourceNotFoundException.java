package com.dta.exception;

public class ResourceNotFoundException extends ApiException {
    // TODO [Timmy]: Thrown when requested user/session/entry resource cannot be found for authenticated user.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
