package com.dta.dto.response;

import java.util.List;
import java.util.UUID;

public class AchievementsResponse {

    private UUID userId;
    private int completedSessions;
    private int diaryEntries;
    private List<String> achievements;
    private String nextMilestone;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    public String getNextMilestone() {
        return nextMilestone;
    }

    public void setNextMilestone(String nextMilestone) {
        this.nextMilestone = nextMilestone;
    }
}
