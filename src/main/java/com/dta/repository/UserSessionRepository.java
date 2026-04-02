package com.dta.repository;

import com.dta.entity.UserSession;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {

    List<UserSession> findByUserIdOrderByStartedAtDesc(UUID userId);

    long countByUserId(UUID userId);

    long countByUserIdAndStatusIgnoreCase(UUID userId, String status);
}
