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

    @Column(nullable = false, unique = true, length = 100)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "cbt_prompt_mapping", columnDefinition = "TEXT")
    private String cbtPromptMapping;

    // Many-to-Many relationship with DiaryEntry as defined in your ER diagram
    @ManyToMany(mappedBy = "distortions")
    private List<DiaryEntry> diaryEntries = new ArrayList<>();

    // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCbtPromptMapping() { return cbtPromptMapping; }
    public void setCbtPromptMapping(String cbtPromptMapping) { this.cbtPromptMapping = cbtPromptMapping; }

    public List<DiaryEntry> getDiaryEntries() { return diaryEntries; }
    public void setDiaryEntries(List<DiaryEntry> diaryEntries) { this.diaryEntries = diaryEntries; }
}