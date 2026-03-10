package com.dta.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DiaryEntry {
    // TODO [Timmy]: Add entity mapping, content validations, and many-to-many distortions mapping table.
    private Long id;
    private String title;
    private String content;
    private Instant createdAt;
    private List<CognitiveDistortion> distortions = new ArrayList<>();

    // getters/setters omitted for skeleton
}
