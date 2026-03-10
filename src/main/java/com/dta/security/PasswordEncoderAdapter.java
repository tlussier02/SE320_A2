package com.dta.security;

public class PasswordEncoderAdapter {
    // TODO [Josh]: Wire Spring Security PasswordEncoder (BCrypt) and salt/hash workflow.
    public String hash(String raw) {
        return raw;
    }

    // TODO [Josh]: Compare raw password to stored hash using constant-time comparison.
    public boolean matches(String raw, String hashed) {
        return raw.equals(hashed);
    }
}
