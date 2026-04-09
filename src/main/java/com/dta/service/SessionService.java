package com.dta.service;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.SessionResponse;
import java.util.List;
import java.util.UUID;

public interface SessionService {

    SessionResponse getSession(UUID sessionId);

    SessionResponse startSession(UUID sessionTemplateId, StartSessionRequest request);

    ChatResponse chat(UUID sessionId, ChatRequest request);

    SessionResponse endSession(UUID sessionId, String reason);

    List<SessionResponse> listSessions(UUID userId);
}
