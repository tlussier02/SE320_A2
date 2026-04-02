package com.dta.service;

import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);

    void logout(RefreshRequest request);
}
