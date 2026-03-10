package com.dta.service;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;

public interface SessionService {
    // TODO [Josh]: Create/open user session entity and initialize state for CBT flow.
    void startSession(String sessionId, StartSessionRequest request);
    // TODO [Josh/Trevor]: Route chat messages through conversation memory and AI pipeline, then persist output.
    ChatResponse chat(String sessionId, ChatRequest request);
    // TODO [Josh]: Mark session end, persist transcript summary references, and close state.
    void endSession(String sessionId);
    // TODO [Timmy]: Return session list filtered by authenticated user + pagination DTO.
    Object listSessions();
}
