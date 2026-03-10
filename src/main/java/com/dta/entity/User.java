package com.dta.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    // TODO [Timmy]: Add JPA annotations for entity/table and define User <-> Session/Diary relationships.
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private List<UserSession> sessions = new ArrayList<>();
    private List<DiaryEntry> diaryEntries = new ArrayList<>();

    // getters/setters omitted for skeleton
}
