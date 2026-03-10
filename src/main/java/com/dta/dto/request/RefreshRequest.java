package com.dta.dto.request;

// TODO [Timmy]: Validate refresh token presence and format before service dispatch.
public record RefreshRequest(String refreshToken) {}
