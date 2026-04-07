package com.dta.repository;

import com.dta.entity.ChatMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Query chat history for a specific session ordered by time using UUID
    List<ChatMessage> findBySessionId(UUID sessionId);
}