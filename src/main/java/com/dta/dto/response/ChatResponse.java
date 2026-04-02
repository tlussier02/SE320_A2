package com.dta.dto.response;

import java.time.Instant;
import java.util.UUID;

public class ChatResponse {

    private UUID sessionId;
    private String userMessage;
    private String assistantMessage;
    private boolean crisisFlagged;
    private Instant timestamp;

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getAssistantMessage() {
        return assistantMessage;
    }

    public void setAssistantMessage(String assistantMessage) {
        this.assistantMessage = assistantMessage;
    }

    public boolean isCrisisFlagged() {
        return crisisFlagged;
    }

    public void setCrisisFlagged(boolean crisisFlagged) {
        this.crisisFlagged = crisisFlagged;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
