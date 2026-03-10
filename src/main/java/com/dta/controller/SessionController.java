package com.dta.controller;

import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // TODO [Timmy]: Return session DTO list from repository with user scope and pagination.
    @GetMapping
    public ResponseEntity<?> listSessions() {
        return ResponseEntity.ok(sessionService.listSessions());
    }

    // TODO [Timmy]: Start session endpoint should validate user/session id ownership.
    @PostMapping("/{sessionId}/start")
    public ResponseEntity<Void> startSession(@PathVariable String sessionId, @RequestBody StartSessionRequest request) {
        sessionService.startSession(sessionId, request);
        return ResponseEntity.ok().build();
    }

    // TODO [Timmy]: Apply request validation and return created chat response DTO.
    @PostMapping("/{sessionId}/chat")
    public ResponseEntity<ChatResponse> chat(@PathVariable String sessionId, @RequestBody ChatRequest request) {
        return ResponseEntity.ok(sessionService.chat(sessionId, request));
    }

    // TODO [Timmy]: End session endpoint should finalize resources and return no-content status.
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<Void> endSession(@PathVariable String sessionId) {
        sessionService.endSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
