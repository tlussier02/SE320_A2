package com.dta.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cognitive_distortions")
public class CognitiveDistortion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "cbt_prompt_mapping", columnDefinition = "TEXT")
    private String cbtPromptMapping;

    @ManyToMany(mappedBy = "distortions")
    private List<DiaryEntry> diaryEntries = new ArrayList<>();

    // getters/setters omitted for skeleton
}

