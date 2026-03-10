package com.dta.service;

import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.response.AuthResponse;

public interface AuthService {
    // TODO [Josh]: Implement registration with validation, hashing, duplicate checks, and persistence.
    AuthResponse register(RegisterRequest request);
    // TODO [Josh]: Implement login flow and JWT access/refresh issuance.
    AuthResponse login(LoginRequest request);
    // TODO [Josh]: Implement refresh-token rotation and replay protection.
    AuthResponse refresh(RefreshRequest request);
    // TODO [Josh]: Implement token revocation/blacklist cleanup and session invalidation.
    void logout();
}
