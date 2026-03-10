package com.dta.repository;

import com.dta.entity.ChatMessage;
import java.util.List;

public interface ChatMessageRepository {
    // TODO [Timmy]: Query chat history for session timeline and ordering by sentAt.
    List<ChatMessage> findBySessionId(String sessionId);
}
