package com.dta.dto.request;

// TODO [Timmy]: Add not-empty constraints for username/password before calling AuthService.
public record LoginRequest(String username, String password) {}
