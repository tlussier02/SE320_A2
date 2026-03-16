package com.dta.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trusted_contacts")
public class TrustedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // PII: Use a converter here later to encrypt sensitive phone numbers
    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 100)
    private String relation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // getters/setters omitted for skeleton
}
