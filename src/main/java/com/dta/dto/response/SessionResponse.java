package com.dta.dto.response;

public record SessionResponse(String sessionId, String state) {
    // TODO [Josh, Timmy]: Include session lifecycle fields (status, timestamps, completion state).
}
