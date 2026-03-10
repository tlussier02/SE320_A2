package com.dta.service.impl;

import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.response.AuthResponse;
import com.dta.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    // TODO [Josh]: Implement register with validation, password hashing, duplicate-user checks, and persistence.
    @Override
    public AuthResponse register(RegisterRequest request) {
        return new AuthResponse("access-token", "refresh-token");
    }

    // TODO [Josh]: Implement login auth against hashed credentials and token issuance.
    @Override
    public AuthResponse login(LoginRequest request) {
        return new AuthResponse("access-token", "refresh-token");
    }

    // TODO [Josh]: Validate refresh token, rotate tokens, and persist token state.
    @Override
    public AuthResponse refresh(RefreshRequest request) {
        return new AuthResponse("access-token", request.refreshToken());
    }

    // TODO [Josh]: Invalidate refresh/access tokens and clear token/session cache.
    @Override
    public void logout() {
        // TODO [Josh]: revoke refresh token / invalidate token store
    }
}
