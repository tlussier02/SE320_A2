package com.dta.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @Column(nullable = false, unique = true) 
    private String username;
    
    @Column(nullable = false, unique = true) 
    private String email;
    
    @Column(nullable = false) 
    private String passwordHash;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    private List<UserSession> sessions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
    private List<DiaryEntry> diaryEntries = new ArrayList<>();
}
