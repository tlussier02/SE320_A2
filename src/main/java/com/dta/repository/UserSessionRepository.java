package com.dta.repository;

import com.dta.entity.UserSession;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository {
    // TODO [Timmy]: Return sessions by authenticated user; support sorting and pagination.
    List<UserSession> findAll();
    // TODO [Timmy]: Fetch by session ID with full message lazy/eager loading.
    Optional<UserSession> findById(String id);
    // TODO [Timmy]: Persist session lifecycle transitions and timestamps.
    UserSession save(UserSession session);
}
