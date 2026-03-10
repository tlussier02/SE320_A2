package com.dta.dto.response;

public record AuthResponse(String accessToken, String refreshToken) {
    // TODO [Josh]: Define auth payload behavior, token expiry metadata, and optional user context fields.
}
