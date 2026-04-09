package com.dta.cli;

import java.util.UUID;

public class CliState {

    private UUID currentUserId;
    private String currentUserEmail;
    private String currentRefreshToken;
    private UUID activeSessionId;

    public UUID getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(UUID currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public String getCurrentRefreshToken() {
        return currentRefreshToken;
    }

    public void setCurrentRefreshToken(String currentRefreshToken) {
        this.currentRefreshToken = currentRefreshToken;
    }

    public UUID getActiveSessionId() {
        return activeSessionId;
    }

    public void setActiveSessionId(UUID activeSessionId) {
        this.activeSessionId = activeSessionId;
    }

    public boolean isAuthenticated() {
        return currentUserId != null;
    }

    public void clearAuthentication() {
        currentUserId = null;
        currentUserEmail = null;
        currentRefreshToken = null;
        activeSessionId = null;
    }
}
