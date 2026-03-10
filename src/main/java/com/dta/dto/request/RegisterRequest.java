package com.dta.dto.request;

// TODO [Timmy]: Validate format/strength rules and password policy.
public record RegisterRequest(String username, String email, String password) {}
