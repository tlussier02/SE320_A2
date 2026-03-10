package com.dta.dto.request;

public record StartSessionRequest(String mood, String mode) {
    // TODO [Josh, Timmy]: Validate session intent payload and map default CBT mode.
}
