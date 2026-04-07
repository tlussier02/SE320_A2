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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    void testStartSession_ReturnsSessionResponse() {
        UUID userId = UUID.randomUUID();
        UUID templateId = UUID.randomUUID();
        StartSessionRequest request = new StartSessionRequest();
        request.setUserId(userId);
        request.setTitle("Burnout Recovery");

        UserSession savedSession = new UserSession();
        savedSession.setId(UUID.randomUUID());
        savedSession.setUserId(userId);
        savedSession.setTitle("Burnout Recovery");
        savedSession.setStatus("ACTIVE");

        when(userSessionRepository.save(any(UserSession.class))).thenReturn(savedSession);

        SessionResponse response = sessionService.startSession(templateId, request);

        assertNotNull(response);
        assertEquals("ACTIVE", response.getStatus());
        assertEquals("Burnout Recovery", response.getTitle());
        verify(userSessionRepository).save(any(UserSession.class));
    }

    @Test
    void testChat_WithValidMessage_ReturnsChatResponse() {
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ChatRequest request = new ChatRequest();
        request.setMessage("I feel overwhelmed today.");

        UserSession session = new UserSession();
        session.setId(sessionId);
        session.setUserId(userId);

        CrisisResponse mockCrisis = new CrisisResponse();
        mockCrisis.setCrisis(false);

        when(userSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(aiService.detectCrisis(anyString())).thenReturn(mockCrisis);
        when(aiService.generateResponse(eq(userId), eq(sessionId), anyString()))
                .thenReturn("It is completely normal to feel overwhelmed. Let's break it down.");

        ChatResponse response = sessionService.chat(sessionId, request);

        assertNotNull(response);
        assertEquals("I feel overwhelmed today.", response.getUserMessage());
        assertEquals("It is completely normal to feel overwhelmed. Let's break it down.", response.getAssistantMessage());
        assertFalse(response.isCrisisFlagged());
    }

    @Test
    void testChat_WithBlankMessage_ThrowsBadRequestException() {
        UUID sessionId = UUID.randomUUID();
        ChatRequest request = new ChatRequest();
        request.setMessage("   ");

        assertThrows(BadRequestException.class, () -> sessionService.chat(sessionId, request));
        verify(userSessionRepository, never()).findById(any());
    }

    @Test
    void testEndSession_UpdatesStatusAndSummary() {
        UUID sessionId = UUID.randomUUID();
        UserSession session = new UserSession();
        session.setId(sessionId);
        session.setStatus("ACTIVE");

        when(userSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(aiService.summarizeSession(sessionId)).thenReturn("User discussed burnout triggers.");
        when(userSessionRepository.save(any(UserSession.class))).thenReturn(session);

        SessionResponse response = sessionService.endSession(sessionId, "User requested stop");

        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
        assertEquals("User discussed burnout triggers.", response.getSummary());
        assertEquals("User requested stop", response.getEndReason());
        verify(userSessionRepository).save(session);
    }

    @Test
    void testEndSession_NotFound_ThrowsException() {
        UUID sessionId = UUID.randomUUID();
        when(userSessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sessionService.endSession(sessionId, "Done"));
    }

    @Test
    void testListSessions_ReturnsListOfResponses() {
        UUID userId = UUID.randomUUID();
        UserSession session1 = new UserSession();
        session1.setId(UUID.randomUUID());
        session1.setStartedAt(Instant.now());

        UserSession session2 = new UserSession();
        session2.setId(UUID.randomUUID());
        session2.setStartedAt(Instant.now().minusSeconds(3600));

        when(userSessionRepository.findByUserIdOrderByStartedAtDesc(userId)).thenReturn(List.of(session1, session2));

        List<SessionResponse> responses = sessionService.listSessions(userId);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(userSessionRepository).findByUserIdOrderByStartedAtDesc(userId);
    }
}