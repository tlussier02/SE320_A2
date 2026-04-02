package com.dta.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dta.ai.CrisisDetector;
import com.dta.ai.KnowledgeBaseLoader;
import com.dta.ai.RagContextBuilder;
import com.dta.dto.request.ChatRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.entity.UserSession;
import com.dta.repository.UserSessionRepository;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SessionServiceImplTest {

    private AiServiceImpl createAiService() {
        return new AiServiceImpl(
                new RagContextBuilder(),
                new KnowledgeBaseLoader(),
                new CrisisDetector()
        );
    }

    @Test
    void chatSetsCrisisFlaggedTrueForCrisisLanguage() {
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserSession session = createSession(sessionId, userId);
        UserSessionRepository repository = repositoryReturning(sessionId, session);

        SessionServiceImpl service = new SessionServiceImpl(repository, createAiService());
        ChatRequest request = new ChatRequest();
        request.setMessage("I want to kill myself because nothing matters.");

        ChatResponse response = service.chat(sessionId, request);

        assertTrue(response.isCrisisFlagged());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void chatSetsCrisisFlaggedFalseForNonCrisisLanguage() {
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserSession session = createSession(sessionId, userId);
        UserSessionRepository repository = repositoryReturning(sessionId, session);

        SessionServiceImpl service = new SessionServiceImpl(repository, createAiService());
        ChatRequest request = new ChatRequest();
        request.setMessage("I had a rough day and want help reframing this.");

        ChatResponse response = service.chat(sessionId, request);

        assertFalse(response.isCrisisFlagged());
        assertNotNull(response.getAssistantMessage());
    }

    private UserSession createSession(UUID sessionId, UUID userId) {
        UserSession session = new UserSession();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setTitle("Test Session");
        session.setStatus("ACTIVE");
        return session;
    }

    private UserSessionRepository repositoryReturning(UUID expectedSessionId, UserSession session) {
        return (UserSessionRepository) Proxy.newProxyInstance(
                UserSessionRepository.class.getClassLoader(),
                new Class<?>[]{UserSessionRepository.class},
                (proxy, method, args) -> {
                    if ("findById".equals(method.getName())) {
                        UUID requestedId = (UUID) args[0];
                        return expectedSessionId.equals(requestedId)
                                ? Optional.of(session)
                                : Optional.empty();
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("toString".equals(method.getName())) {
                        return "SessionRepositoryTestProxy";
                    }
                    throw new UnsupportedOperationException(
                            "Method not needed in this test: " + method.getName()
                    );
                }
        );
    }
}
