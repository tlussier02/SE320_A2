package com.dta.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserSession {
    // TODO [Timmy]: Map one-to-many relationship to ChatMessage and session status enum.
    private Long id;
    private Instant startedAt;
    private Instant endedAt;
    private String state;
    private List<ChatMessage> messages = new ArrayList<>();

    // getters/setters omitted for skeleton
}
