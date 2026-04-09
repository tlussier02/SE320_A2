package com.dta.dto.response;

import java.util.List;
import java.util.UUID;

public class DiaryInsightsResponse {

    private UUID userId;
    private int entryCount;
    private List<String> topDistortions;
    private List<String> reframingPrompts;
    private String insightSummary;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

    public List<String> getTopDistortions() {
        return topDistortions;
    }

    public void setTopDistortions(List<String> topDistortions) {
        this.topDistortions = topDistortions;
    }

    public List<String> getReframingPrompts() {
        return reframingPrompts;
    }

    public void setReframingPrompts(List<String> reframingPrompts) {
        this.reframingPrompts = reframingPrompts;
    }

    public String getInsightSummary() {
        return insightSummary;
    }

    public void setInsightSummary(String insightSummary) {
        this.insightSummary = insightSummary;
    }
}
