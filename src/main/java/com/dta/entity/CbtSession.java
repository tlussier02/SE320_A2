package com.dta.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cbt_sessions")
public class CbtSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "focus_area")
    private String focusArea;

    @Column(name = "session_state")
    private String state;

    @OneToMany(mappedBy = "cbtSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cbt_session_insights", joinColumns = @JoinColumn(name = "cbt_session_id"))
    @Column(name = "insight")
    private List<String> insights = new ArrayList<>();

    // getters/setters omitted for skeleton
}
