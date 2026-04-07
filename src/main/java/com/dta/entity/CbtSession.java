package com.dta.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cbt_sessions")
public class CbtSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the system user who owns this session
    @Column(nullable = false)
    private UUID userId;

    @Column(name = "focus_area", length = 255)
    private String focusArea;

    @Column(name = "session_state", nullable = false, length = 50)
    private String state;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant endedAt;

    // Relationship to chat messages for history tracking
    @OneToMany(mappedBy = "cbtSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    // Collection for AI-generated insights during the session
    @ElementCollection
    @CollectionTable(name = "cbt_session_insights", joinColumns = @JoinColumn(name = "cbt_session_id"))
    @Column(name = "insight")
    private List<String> insights = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = Instant.now();
        }
        if (state == null) {
            state = "ACTIVE";
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getFocusArea() { return focusArea; }
    public void setFocusArea(String focusArea) { this.focusArea = focusArea; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }

    public List<ChatMessage> getMessages() { return messages; }
    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }
    
    // Helper method to maintain bi-directional relationship
    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setCbtSession(this);
    }
}