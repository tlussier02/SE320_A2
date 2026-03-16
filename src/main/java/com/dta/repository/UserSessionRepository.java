package com.dta.repository;

import com.dta.entity.UserSession;
import com.dta.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    // Supports pagination and sorting for an authenticated user's sessions
    Page<UserSession> findByUser(User user, Pageable pageable);

    // Standard list for simple lookups
    List<UserSession> findByUserId(Long userId);
    
    // JpaRepository provides save() for persistence and findById() for session lookups
}
