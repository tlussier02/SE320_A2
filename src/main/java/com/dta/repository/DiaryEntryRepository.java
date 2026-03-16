package com.dta.repository;

import com.dta.entity.DiaryEntry;
import com.dta.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    // Returns entries per user with paging and sorting support [cite: 109, 120]
    Page<DiaryEntry> findByUser(User user, Pageable pageable);

    // Filter by user and a date range [cite: 109]
    Page<DiaryEntry> findByUserIdAndCreatedAtBetween(Long userId, Instant start, Instant end, Pageable pageable);

    // Enforce owner checks by finding by ID and User [cite: 109]
    Optional<DiaryEntry> findByIdAndUserId(Long id, Long userId);
}
