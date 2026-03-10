package com.dta.security;

public class JwtUtil {
    // TODO [Josh]: Replace with real JWT generation (claims, expiry, signing key, refresh metadata).
    public String issueAccessToken(String username) {
        return "token:" + username;
    }

    // TODO [Josh]: Implement token validation against signature, expiration, and blacklist/rotation state.
    public boolean validate(String token) {
        return token != null && !token.isBlank();
    }

    // TODO [Josh]: Parse JWT subject safely and throw typed error when token is malformed.
    public String getSubject(String token) {
        return token == null ? null : token.replace("token:", "");
    }
}
