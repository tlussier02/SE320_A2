package com.dta.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DiaryEntryResponse {

    private UUID id;
    private UUID userId;
    private String automaticThought;
    private String emotion;
    private String reflection;
    private List<String> suggestedDistortions;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAutomaticThought() {
        return automaticThought;
    }

    public void setAutomaticThought(String automaticThought) {
        this.automaticThought = automaticThought;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getReflection() {
        return reflection;
    }

    public void setReflection(String reflection) {
        this.reflection = reflection;
    }

    public List<String> getSuggestedDistortions() {
        return suggestedDistortions;
    }

    public void setSuggestedDistortions(List<String> suggestedDistortions) {
        this.suggestedDistortions = suggestedDistortions;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
