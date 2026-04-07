package com.dta.service.impl;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.SessionResponse;
import com.dta.entity.UserSession;
import com.dta.exception.BadRequestException;
import com.dta.exception.ResourceNotFoundException;
import com.dta.repository.UserSessionRepository;
import com.dta.service.AiService;
import com.dta.service.SessionService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SessionServiceImpl implements SessionService {

    private final UserSessionRepository userSessionRepository;
    private final AiService aiService;

    public SessionServiceImpl(UserSessionRepository userSessionRepository, AiService aiService) {
        this.userSessionRepository = userSessionRepository;
        this.aiService = aiService;
    }

    @Override
    @Transactional
    public SessionResponse startSession(UUID sessionTemplateId, StartSessionRequest request) {
        UserSession session = new UserSession();
        session.setUserId(request.getUserId());
        session.setTitle(resolveTitle(sessionTemplateId, request));
        session.setStatus("ACTIVE");

        return map(userSessionRepository.save(session), null);
    }

    @Override
    @Transactional
    public ChatResponse chat(UUID sessionId, ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new BadRequestException("Message cannot be blank.");
        }

        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found."));
        CrisisResponse assessment = aiService.detectCrisis(request.getMessage());
        String aiReply = aiService.generateResponse(
                session.getUserId(),
                sessionId,
                request.getMessage()
        );

        ChatResponse response = new ChatResponse();
        response.setSessionId(sessionId);
        response.setUserMessage(request.getMessage());
        response.setAssistantMessage(aiReply);
        response.setCrisisFlagged(assessment.isCrisis());
        response.setTimestamp(Instant.now());
        return response;
    }

    @Override
    @Transactional
    public SessionResponse endSession(UUID sessionId, String reason) {
        UserSession session = userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found."));
        session.setStatus("COMPLETED");
        session.setEndedAt(Instant.now());
        session.setSummary(aiService.summarizeSession(sessionId));
        return map(userSessionRepository.save(session), reason);
    }

    @Override
    public List<SessionResponse> listSessions(UUID userId) {
        return userSessionRepository.findByUserIdOrderByStartedAtDesc(userId)
                .stream()
                .map(session -> map(session, null))
                .toList();
    }

    private String resolveTitle(UUID sessionTemplateId, StartSessionRequest request) {
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            return request.getTitle();
        }
        if (request.getMode() != null && !request.getMode().isBlank()) {
            return "DTA " + request.getMode() + " Session";
        }
        return "DTA Guided Session " + sessionTemplateId;
    }

    private SessionResponse map(UserSession session, String endReason) {
        SessionResponse response = new SessionResponse();
        response.setSessionId(session.getId());
        response.setUserId(session.getUserId());
        response.setTitle(session.getTitle());
        response.setStatus(session.getStatus());
        response.setStartedAt(session.getStartedAt());
        response.setEndedAt(session.getEndedAt());
        response.setSummary(session.getSummary());
        response.setEndReason(endReason);
        return response;
    }
}
