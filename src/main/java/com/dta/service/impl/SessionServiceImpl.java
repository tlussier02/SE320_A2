package com.dta.service.impl;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.service.AiService;
import com.dta.service.SessionService;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private final AiService aiService;

    public SessionServiceImpl(AiService aiService) {
        this.aiService = aiService;
    }

    // TODO [Josh]: Handle persistence setup and initial state for new user session.
    @Override
    public void startSession(String sessionId, StartSessionRequest request) {
        // TODO [Josh]: load/create session for user and persist start state
    }

    // TODO [Trevor]: Route chat through AI service with proper context/history retrieval.
    @Override
    public ChatResponse chat(String sessionId, ChatRequest request) {
        String response = aiService.generateResponse(request.message(), sessionId);
        return new ChatResponse(response, false);
    }

    // TODO [Josh]: Close session lifecycle, finalize history and summary.
    @Override
    public void endSession(String sessionId) {
        // TODO [Josh]: finalize session data and store summary
    }

    // TODO [Timmy]: Return current user's session list with pagination/filters.
    @Override
    public Object listSessions() {
        return java.util.Collections.emptyList();
    }
}
