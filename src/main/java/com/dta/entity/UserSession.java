package com.dta.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    public enum SessionState {
        ACTIVE, ENDED, PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startedAt;

    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    private SessionState state;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    // getters/setters omitted for skeleton
}
