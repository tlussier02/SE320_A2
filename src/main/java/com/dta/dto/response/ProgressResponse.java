package com.dta.dto.response;

import java.util.List;
import java.util.UUID;

public class ProgressResponse {

    private UUID userId;
    private String timeframe;
    private double score;
    private int completedSessions;
    private int diaryEntries;
    private int crisisAlerts;
    private String burnoutTrend;
    private List<String> highlights;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(int completedSessions) {
        this.completedSessions = completedSessions;
    }

    public int getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(int diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    public int getCrisisAlerts() {
        return crisisAlerts;
    }

    public void setCrisisAlerts(int crisisAlerts) {
        this.crisisAlerts = crisisAlerts;
    }

    public String getBurnoutTrend() {
        return burnoutTrend;
    }

    public void setBurnoutTrend(String burnoutTrend) {
        this.burnoutTrend = burnoutTrend;
    }

    public List<String> getHighlights() {
        return highlights;
    }

    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }
}
