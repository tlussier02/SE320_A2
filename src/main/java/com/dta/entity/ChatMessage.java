package com.dta.entity;

import java.time.Instant;

public class ChatMessage {
    // TODO [Timmy]: Add role/content constraints and map to UserSession with timestamp indexing.
    private Long id;
    private String role;
    private String content;
    private Instant sentAt;

    // getters/setters omitted for skeleton
}
