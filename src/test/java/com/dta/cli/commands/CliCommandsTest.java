package com.dta.cli.commands;

import com.dta.cli.CliState;
import com.dta.cli.MenuHandler;
import com.dta.dto.request.ChatRequest;
import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.request.StartSessionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.AuthResponse;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.ProgressResponse;
import com.dta.dto.response.SafetyPlanResponse;
import com.dta.dto.response.SessionResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.service.AuthService;
import com.dta.service.CrisisService;
import com.dta.service.DiaryService;
import com.dta.service.ProgressService;
import com.dta.service.SessionService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CliCommandsTest {

    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID SESSION_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID ENTRY_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");

    @Mock
    private AuthService authService;

    @Mock
    private SessionService sessionService;

    @Mock
    private DiaryService diaryService;

    @Mock
    private ProgressService progressService;

    @Mock
    private CrisisService crisisService;

    @Mock
    private MenuHandler menuHandler;

    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void authenticationCommand_registerAndLogoutFlow() {
        CliState state = new CliState();
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse("jane@example.com", "refresh-123"));

        AuthenticationCommand command =
                new AuthenticationCommand(scanner("1\nJane Doe\njane@example.com\nsecret123\n3\n0\n"), state, authService);

        assertEquals("Authentication", command.getDescription());
        command.execute();

        ArgumentCaptor<RegisterRequest> registerCaptor = ArgumentCaptor.forClass(RegisterRequest.class);
        verify(authService).register(registerCaptor.capture());
        assertEquals("Jane Doe", registerCaptor.getValue().getFullName());
        assertEquals("jane@example.com", registerCaptor.getValue().getEmail());
        assertEquals("secret123", registerCaptor.getValue().getPassword());

        ArgumentCaptor<RefreshRequest> refreshCaptor = ArgumentCaptor.forClass(RefreshRequest.class);
        verify(authService).logout(refreshCaptor.capture());
        assertEquals("refresh-123", refreshCaptor.getValue().getRefreshToken());
        assertFalse(state.isAuthenticated());
        assertTrue(output().contains("Registered and logged in as jane@example.com"));
        assertTrue(output().contains("Logged out."));
    }

    @Test
    void authenticationCommand_loginHandlesInvalidOption() {
        CliState state = new CliState();
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(authResponse("jane@example.com", "refresh-456"));

        AuthenticationCommand command =
                new AuthenticationCommand(scanner("9\n2\njane@example.com\nsecret123\n0\n"), state, authService);

        command.execute();

        ArgumentCaptor<LoginRequest> loginCaptor = ArgumentCaptor.forClass(LoginRequest.class);
        verify(authService).login(loginCaptor.capture());
        assertEquals("jane@example.com", loginCaptor.getValue().getEmail());
        assertEquals("secret123", loginCaptor.getValue().getPassword());
        assertTrue(state.isAuthenticated());
        assertEquals("jane@example.com", state.getCurrentUserEmail());
        assertTrue(output().contains("Invalid option. Please try again."));
        assertTrue(output().contains("Logged in as jane@example.com"));
    }

    @Test
    void authenticationCommand_handlesMissingTokenAndServiceError() {
        CliState state = authenticatedState();
        state.setCurrentRefreshToken(" ");
        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("bad credentials"));

        AuthenticationCommand command =
                new AuthenticationCommand(scanner("3\n2\njane@example.com\nwrongpass\n0\n"), state, authService);

        command.execute();

        verify(authService, never()).logout(any(RefreshRequest.class));
        assertFalse(state.isAuthenticated());
        assertTrue(output().contains("No active login session was found."));
        assertTrue(output().contains("Authentication error: bad credentials"));
    }

    @Test
    void sessionWorkflowCommand_requiresAuthentication() {
        SessionWorkflowCommand command =
                new SessionWorkflowCommand(scanner(""), new CliState(), sessionService);

        assertEquals("CBT Sessions", command.getDescription());
        command.execute();

        assertTrue(output().contains("Please login first."));
    }

    @Test
    void sessionWorkflowCommand_executesFullFlow() {
        CliState state = authenticatedState();
        when(sessionService.listSessions(USER_ID))
                .thenReturn(List.of(sessionResponse(SESSION_ID, "Past Session", "COMPLETED", "Helpful summary")));
        when(sessionService.startSession(any(UUID.class), any(StartSessionRequest.class)))
                .thenReturn(sessionResponse(SESSION_ID, "Focused Session", "ACTIVE", null));
        when(sessionService.chat(eq(SESSION_ID), any(ChatRequest.class)))
                .thenReturn(chatResponse("Let's unpack that.", true));
        when(sessionService.endSession(eq(SESSION_ID), eq("Need a break")))
                .thenReturn(sessionResponse(SESSION_ID, "Focused Session", "COMPLETED", "Key insight"));

        SessionWorkflowCommand command = new SessionWorkflowCommand(
                scanner("9\n1\n2\nFocused Session\nAnxious\nguided\n3\n\nI feel stuck\n4\n\nNeed a break\n0\n"),
                state,
                sessionService);

        command.execute();

        ArgumentCaptor<StartSessionRequest> startCaptor = ArgumentCaptor.forClass(StartSessionRequest.class);
        verify(sessionService).startSession(any(UUID.class), startCaptor.capture());
        assertEquals(USER_ID, startCaptor.getValue().getUserId());
        assertEquals("Focused Session", startCaptor.getValue().getTitle());
        assertEquals("Anxious", startCaptor.getValue().getMood());
        assertEquals("guided", startCaptor.getValue().getMode());

        ArgumentCaptor<ChatRequest> chatCaptor = ArgumentCaptor.forClass(ChatRequest.class);
        verify(sessionService).chat(eq(SESSION_ID), chatCaptor.capture());
        assertEquals("I feel stuck", chatCaptor.getValue().getMessage());

        verify(sessionService).listSessions(USER_ID);
        verify(sessionService).endSession(SESSION_ID, "Need a break");
        assertNull(state.getActiveSessionId());
        assertTrue(output().contains("Session History"));
        assertTrue(output().contains("Started session " + SESSION_ID));
        assertTrue(output().contains("Assistant: Let's unpack that."));
        assertTrue(output().contains("Crisis flag detected. Review crisis support immediately."));
        assertTrue(output().contains("Summary: Key insight"));
        assertTrue(output().contains("Invalid option. Please try again."));
    }

    @Test
    void sessionWorkflowCommand_handlesMissingSessionIdAndServiceError() {
        CliState state = authenticatedState();
        when(sessionService.startSession(any(UUID.class), any(StartSessionRequest.class)))
                .thenThrow(new RuntimeException("service unavailable"));

        SessionWorkflowCommand command = new SessionWorkflowCommand(
                scanner("3\n\n2\nFocus\nTense\ncoach\n0\n"),
                state,
                sessionService);

        command.execute();

        assertTrue(output().contains("A session id is required."));
        assertTrue(output().contains("Session error: service unavailable"));
    }

    @Test
    void diaryCommand_requiresAuthentication() {
        DiaryCommand command = new DiaryCommand(scanner(""), new CliState(), diaryService);

        assertEquals("Thought Diary", command.getDescription());
        command.execute();

        assertTrue(output().contains("Please login first."));
    }

    @Test
    void diaryCommand_executesFullFlow() {
        CliState state = authenticatedState();
        DiaryEntryResponse entry = diaryEntryResponse(ENTRY_ID, "I always fail", "sad", List.of("All-or-nothing thinking"));
        when(diaryService.createDiaryEntry(any(CreateDiaryEntryRequest.class))).thenReturn(entry);
        when(diaryService.getDiaryEntries(USER_ID)).thenReturn(List.of(entry));
        when(diaryService.suggestDistortions(any(DistortionSuggestionRequest.class)))
                .thenReturn(analysisResponse(List.of("Catastrophizing"), List.of("What evidence supports this?")));

        DiaryCommand command = new DiaryCommand(
                scanner("9\n1\nI always fail\nsad\nLong reflection line\nEND\n2\n3\n4\nI always mess up\n5\n"
                        + ENTRY_ID + "\n0\n"),
                state,
                diaryService);

        command.execute();

        ArgumentCaptor<CreateDiaryEntryRequest> createCaptor = ArgumentCaptor.forClass(CreateDiaryEntryRequest.class);
        verify(diaryService).createDiaryEntry(createCaptor.capture());
        assertEquals(USER_ID, createCaptor.getValue().getUserId());
        assertEquals("I always fail", createCaptor.getValue().getAutomaticThought());
        assertEquals("sad", createCaptor.getValue().getEmotion());
        assertEquals("Long reflection line", createCaptor.getValue().getReflection());

        ArgumentCaptor<DistortionSuggestionRequest> suggestionCaptor =
                ArgumentCaptor.forClass(DistortionSuggestionRequest.class);
        verify(diaryService).suggestDistortions(suggestionCaptor.capture());
        assertEquals("I always mess up", suggestionCaptor.getValue().getText());

        verify(diaryService, times(2)).getDiaryEntries(USER_ID);
        verify(diaryService).deleteDiaryEntry(ENTRY_ID);
        assertTrue(output().contains("Saved diary entry " + ENTRY_ID));
        assertTrue(output().contains("Most common pattern: All-or-nothing thinking (1)"));
        assertTrue(output().contains("Detected distortions: [Catastrophizing]"));
        assertTrue(output().contains("Deleted diary entry " + ENTRY_ID));
        assertTrue(output().contains("Invalid option. Please try again."));
    }

    @Test
    void diaryCommand_handlesEmptyStatesMissingIdAndErrors() {
        CliState state = authenticatedState();
        when(diaryService.getDiaryEntries(USER_ID)).thenReturn(List.of());
        when(diaryService.createDiaryEntry(any(CreateDiaryEntryRequest.class)))
                .thenThrow(new RuntimeException("save failed"));

        DiaryCommand command = new DiaryCommand(
                scanner("2\n3\n5\n\n1\nThought\nsad\nSome reflection\nEND\n0\n"),
                state,
                diaryService);

        command.execute();

        assertTrue(output().contains("No diary entries found."));
        assertTrue(output().contains("No diary entries available to analyze."));
        assertTrue(output().contains("Entry id is required."));
        assertTrue(output().contains("Diary error: save failed"));
    }

    @Test
    void progressCommand_requiresAuthentication() {
        ProgressCommand command = new ProgressCommand(scanner(""), new CliState(), progressService);

        assertEquals("Progress Dashboard", command.getDescription());
        command.execute();

        assertTrue(output().contains("Please login first."));
    }

    @Test
    void progressCommand_executesMenuOptions() {
        CliState state = authenticatedState();
        when(progressService.getWeeklyProgress(USER_ID)).thenReturn(progressResponse("weekly"));
        when(progressService.getMonthlyProgress(USER_ID)).thenReturn(progressResponse("monthly"));
        when(progressService.getBurnoutProgress(USER_ID)).thenReturn(progressResponse("burnout"));

        ProgressCommand command =
                new ProgressCommand(scanner("1\n2\n3\n9\n0\n"), state, progressService);

        command.execute();

        verify(progressService).getWeeklyProgress(USER_ID);
        verify(progressService).getMonthlyProgress(USER_ID);
        verify(progressService).getBurnoutProgress(USER_ID);
        assertTrue(output().contains("Timeframe: weekly"));
        assertTrue(output().contains("Timeframe: monthly"));
        assertTrue(output().contains("Timeframe: burnout"));
        assertTrue(output().contains("Invalid option. Please try again."));
    }

    @Test
    void crisisSupportCommand_requiresAuthentication() {
        CrisisSupportCommand command = new CrisisSupportCommand(scanner(""), new CliState(), crisisService);

        assertEquals("Crisis Support", command.getDescription());
        command.execute();

        assertTrue(output().contains("Please login first."));
    }

    @Test
    void crisisSupportCommand_executesFullFlow() {
        CliState state = authenticatedState();
        when(crisisService.detectCrisis(any(DistortionSuggestionRequest.class)))
                .thenReturn(crisisResponse(true, "HIGH", List.of("hopelessness"), "Call 988", "Escalating distress"));
        when(crisisService.getCrisisOverview())
                .thenReturn(crisisResponse(false, "MODERATE", List.of(), "Reach out", "Monitor closely"));
        when(crisisService.getSafetyPlan(USER_ID)).thenReturn(safetyPlanResponse("Stay with family"));
        when(crisisService.updateSafetyPlan(any(UpdateSafetyPlanRequest.class)))
                .thenReturn(safetyPlanResponse("Call my brother"));

        CrisisSupportCommand command = new CrisisSupportCommand(
                scanner("9\n1\nI feel overwhelmed\n2\n3\n4\nCall my brother\nEND\n0\n"),
                state,
                crisisService);

        command.execute();

        ArgumentCaptor<DistortionSuggestionRequest> detectCaptor =
                ArgumentCaptor.forClass(DistortionSuggestionRequest.class);
        verify(crisisService).detectCrisis(detectCaptor.capture());
        assertEquals("I feel overwhelmed", detectCaptor.getValue().getText());

        ArgumentCaptor<UpdateSafetyPlanRequest> safetyCaptor =
                ArgumentCaptor.forClass(UpdateSafetyPlanRequest.class);
        verify(crisisService).updateSafetyPlan(safetyCaptor.capture());
        assertEquals(USER_ID, safetyCaptor.getValue().getUserId());
        assertEquals("Call my brother", safetyCaptor.getValue().getSafetyPlan());

        verify(crisisService).getCrisisOverview();
        verify(crisisService).getSafetyPlan(USER_ID);
        assertTrue(output().contains("Risk level: HIGH"));
        assertTrue(output().contains("Detected keywords: [hopelessness]"));
        assertTrue(output().contains("Emergency Resources"));
        assertTrue(output().contains("Current safety plan"));
        assertTrue(output().contains("Saved safety plan for user " + USER_ID));
        assertTrue(output().contains("Invalid option. Please try again."));
    }

    @Test
    void crisisSupportCommand_handlesServiceError() {
        CliState state = authenticatedState();
        when(crisisService.detectCrisis(any(DistortionSuggestionRequest.class)))
                .thenThrow(new RuntimeException("assessment unavailable"));

        CrisisSupportCommand command =
                new CrisisSupportCommand(scanner("1\npanic\n0\n"), state, crisisService);

        command.execute();

        assertTrue(output().contains("Crisis support error: assessment unavailable"));
    }

    @Test
    void settingsCommand_printsAuthenticatedUser() {
        CliState state = authenticatedState();
        SettingsCommand command = new SettingsCommand(state);

        assertEquals("Settings", command.getDescription());
        command.execute();

        assertTrue(output().contains("Swagger UI"));
        assertTrue(output().contains("Current user: user@example.com"));
    }

    @Test
    void settingsCommand_printsLoggedOutUser() {
        SettingsCommand command = new SettingsCommand(new CliState());

        command.execute();

        assertTrue(output().contains("Current user: not logged in"));
    }

    @Test
    void exitCommand_stopsMenuHandler() {
        ExitCommand command = new ExitCommand(menuHandler);

        assertEquals("Exit", command.getDescription());
        command.execute();

        verify(menuHandler).stop();
        assertTrue(output().contains("Exiting DTA CLI."));
    }

    private Scanner scanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    private String output() {
        return out.toString(StandardCharsets.UTF_8);
    }

    private CliState authenticatedState() {
        CliState state = new CliState();
        state.setCurrentUserId(USER_ID);
        state.setCurrentUserEmail("user@example.com");
        state.setCurrentRefreshToken("existing-refresh-token");
        return state;
    }

    private AuthResponse authResponse(String email, String refreshToken) {
        AuthResponse response = new AuthResponse();
        response.setUserId(USER_ID);
        response.setFullName("Jane Doe");
        response.setEmail(email);
        response.setAccessToken("access-token");
        response.setRefreshToken(refreshToken);
        return response;
    }

    private SessionResponse sessionResponse(UUID sessionId, String title, String status, String summary) {
        SessionResponse response = new SessionResponse();
        response.setSessionId(sessionId);
        response.setUserId(USER_ID);
        response.setTitle(title);
        response.setStatus(status);
        response.setStartedAt(Instant.parse("2026-04-08T12:00:00Z"));
        response.setEndedAt(Instant.parse("2026-04-08T12:30:00Z"));
        response.setSummary(summary);
        response.setEndReason("user requested");
        return response;
    }

    private ChatResponse chatResponse(String assistantMessage, boolean crisisFlagged) {
        ChatResponse response = new ChatResponse();
        response.setSessionId(SESSION_ID);
        response.setUserMessage("I feel stuck");
        response.setAssistantMessage(assistantMessage);
        response.setCrisisFlagged(crisisFlagged);
        response.setTimestamp(Instant.parse("2026-04-08T12:05:00Z"));
        return response;
    }

    private DiaryEntryResponse diaryEntryResponse(UUID entryId, String thought, String emotion, List<String> distortions) {
        DiaryEntryResponse response = new DiaryEntryResponse();
        response.setId(entryId);
        response.setUserId(USER_ID);
        response.setAutomaticThought(thought);
        response.setEmotion(emotion);
        response.setReflection("Reflection");
        response.setSuggestedDistortions(distortions);
        response.setCreatedAt(Instant.parse("2026-04-08T13:00:00Z"));
        return response;
    }

    private ThoughtAnalysisResponse analysisResponse(List<String> distortions, List<String> prompts) {
        ThoughtAnalysisResponse response = new ThoughtAnalysisResponse();
        response.setDistortions(distortions);
        response.setReframingPrompts(prompts);
        return response;
    }

    private ProgressResponse progressResponse(String timeframe) {
        ProgressResponse response = new ProgressResponse();
        response.setUserId(USER_ID);
        response.setTimeframe(timeframe);
        response.setScore(82.5);
        response.setCompletedSessions(4);
        response.setDiaryEntries(6);
        response.setCrisisAlerts(0);
        response.setBurnoutTrend("Improving");
        response.setHighlights(List.of("Maintained a steady journaling streak"));
        return response;
    }

    private CrisisResponse crisisResponse(
            boolean crisis,
            String riskLevel,
            List<String> keywordsDetected,
            String action,
            String reasoning) {
        CrisisResponse response = new CrisisResponse();
        response.setCrisis(crisis);
        response.setRiskLevel(riskLevel);
        response.setKeywordsDetected(keywordsDetected);
        response.setAction(action);
        response.setReasoning(reasoning);
        return response;
    }

    private SafetyPlanResponse safetyPlanResponse(String safetyPlan) {
        SafetyPlanResponse response = new SafetyPlanResponse();
        response.setUserId(USER_ID);
        response.setSafetyPlan(safetyPlan);
        return response;
    }
}
