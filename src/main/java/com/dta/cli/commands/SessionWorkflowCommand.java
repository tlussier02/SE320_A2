package com.dta.cli.commands;

import com.dta.cli.CliPrompts;
import com.dta.cli.CliState;
import com.dta.cli.Command;
import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.SessionResponse;
import com.dta.service.SessionService;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class SessionWorkflowCommand implements Command {

    private final Scanner scanner;
    private final CliState state;
    private final SessionService sessionService;

    public SessionWorkflowCommand(Scanner scanner, CliState state, SessionService sessionService) {
        this.scanner = scanner;
        this.state = state;
        this.sessionService = sessionService;
    }

    @Override
    public void execute() {
        if (!requireAuthentication()) {
            return;
        }

        while (true) {
            CliPrompts.printHeader("CBT Sessions");
            System.out.println("1. View Session History");
            System.out.println("2. Start New Session");
            System.out.println("3. Continue Session Chat");
            System.out.println("4. End Session");
            System.out.println("0. Back");

            String choice = CliPrompts.prompt(scanner, "Select an option: ");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            try {
                switch (choice) {
                    case "1" -> viewHistory();
                    case "2" -> startSession();
                    case "3" -> continueChat();
                    case "4" -> endSession();
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Session error: " + ex.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "CBT Sessions";
    }

    private boolean requireAuthentication() {
        if (!state.isAuthenticated()) {
            System.out.println("Please login first.");
            return false;
        }
        return true;
    }

    private void viewHistory() {
        List<SessionResponse> sessions = sessionService.listSessions(state.getCurrentUserId());
        if (sessions.isEmpty()) {
            System.out.println("No sessions found.");
            return;
        }

        System.out.println("Session History");
        for (SessionResponse session : sessions) {
            System.out.println("- " + session.getSessionId() + " | " + session.getTitle() + " | " + session.getStatus());
        }
    }

    private void startSession() {
        StartSessionRequest request = new StartSessionRequest();
        request.setUserId(state.getCurrentUserId());
        request.setTitle(CliPrompts.prompt(scanner, "Session title: "));
        request.setMood(CliPrompts.prompt(scanner, "Current mood: "));
        request.setMode(CliPrompts.prompt(scanner, "Session mode: "));

        SessionResponse response = sessionService.startSession(UUID.randomUUID(), request);
        state.setActiveSessionId(response.getSessionId());
        System.out.println("Started session " + response.getSessionId() + " (" + response.getTitle() + ")");
    }

    private void continueChat() {
        UUID sessionId = resolveSessionId();
        if (sessionId == null) {
            System.out.println("A session id is required.");
            return;
        }

        ChatRequest request = new ChatRequest();
        request.setMessage(CliPrompts.prompt(scanner, "Your message: "));
        ChatResponse response = sessionService.chat(sessionId, request);
        state.setActiveSessionId(sessionId);
        System.out.println("Assistant: " + response.getAssistantMessage());
        if (response.isCrisisFlagged()) {
            System.out.println("Crisis flag detected. Review crisis support immediately.");
        }
    }

    private void endSession() {
        UUID sessionId = resolveSessionId();
        if (sessionId == null) {
            System.out.println("A session id is required.");
            return;
        }

        String reason = CliPrompts.prompt(scanner, "End reason: ");
        SessionResponse response = sessionService.endSession(sessionId, reason);
        if (sessionId.equals(state.getActiveSessionId())) {
            state.setActiveSessionId(null);
        }
        System.out.println("Ended session " + response.getSessionId() + " with status " + response.getStatus());
        if (response.getSummary() != null && !response.getSummary().isBlank()) {
            System.out.println("Summary: " + response.getSummary());
        }
    }

    private UUID resolveSessionId() {
        String input = CliPrompts.prompt(
                scanner,
                "Session id (leave blank to use active session): ");
        if (input == null) {
            return null;
        }
        if (input.isBlank()) {
            return state.getActiveSessionId();
        }
        return UUID.fromString(input);
    }
}
