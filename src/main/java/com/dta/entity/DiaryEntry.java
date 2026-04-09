package com.dta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "diary_entries")
public class DiaryEntry {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 3000)
    private String automaticThought;

    @Column(nullable = false, length = 255)
    private String emotion;

    @Column(nullable = false, length = 4000)
    private String reflection;

    @Column(length = 2000)
    private String suggestedDistortions;

    @ManyToMany
    @JoinTable(
        name = "diary_distortions",
        joinColumns = @JoinColumn(name = "diary_entry_id"),
        inverseJoinColumns = @JoinColumn(name = "distortion_id")
    )
    private List<CognitiveDistortion> distortions;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

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

    public String getSuggestedDistortions() {
        return suggestedDistortions;
    }

    public void setSuggestedDistortions(String suggestedDistortions) {
        this.suggestedDistortions = suggestedDistortions;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<CognitiveDistortion> getDistortions() {
        return distortions;
    }

    public void setDistortions(List<CognitiveDistortion> distortions) {
        this.distortions = distortions;
    }
}