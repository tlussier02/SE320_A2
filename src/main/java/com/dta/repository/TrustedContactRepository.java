package com.dta.repository;

import com.dta.entity.TrustedContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrustedContactRepository extends JpaRepository<TrustedContact, Long> {
    
    // Finds all trusted contacts associated with a specific user
    List<TrustedContact> findByUserId(UUID userId);
}