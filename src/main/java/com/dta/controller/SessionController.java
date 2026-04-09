package com.dta.controller;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.SessionResponse;
import com.dta.service.SessionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(sessionService.getSession(sessionId));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> listSessions(@RequestParam UUID userId) {
        return ResponseEntity.ok(sessionService.listSessions(userId));
    }

    @PostMapping("/{sessionId}/start")
    public ResponseEntity<SessionResponse> startSession(
            @PathVariable UUID sessionId,
            @Valid @RequestBody StartSessionRequest request) {
        return ResponseEntity.ok(sessionService.startSession(sessionId, request));
    }

    @PostMapping("/{sessionId}/chat")
    public ResponseEntity<ChatResponse> chat(
            @PathVariable UUID sessionId,
            @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(sessionService.chat(sessionId, request));
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<SessionResponse> endSession(
            @PathVariable UUID sessionId,
            @RequestParam(defaultValue = "completed") String reason) {
        return ResponseEntity.ok(sessionService.endSession(sessionId, reason));
    }
}
