package com.dta.controller;

import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.AchievementsResponse;
import com.dta.dto.response.AuthResponse;
import com.dta.dto.response.ChatResponse;
import com.dta.dto.response.CopingStrategiesResponse;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.DiaryInsightsResponse;
import com.dta.dto.response.ProgressResponse;
import com.dta.dto.response.SafetyPlanResponse;
import com.dta.dto.response.SessionResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.exception.BadRequestException;
import com.dta.exception.ConflictException;
import com.dta.exception.GlobalExceptionHandler;
import com.dta.exception.ResourceNotFoundException;
import com.dta.exception.UnauthorizedException;
import com.dta.security.filter.JwtAuthFilter;
import com.dta.service.AuthService;
import com.dta.service.CrisisService;
import com.dta.service.DiaryService;
import com.dta.service.ProgressService;
import com.dta.service.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AuthController.class,
        SessionController.class,
        DiaryController.class,
        ProgressController.class,
        CrisisController.class
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ApiControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private ProgressService progressService;

    @MockBean
    private CrisisService crisisService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void registerReturnsCreatedResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthResponse response = authResponse(userId);
        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Trevor Lussier",
                                  "email": "trevor@example.com",
                                  "password": "Password123!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("trevor@example.com"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void registerValidationFailureReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "",
                                  "email": "not-an-email",
                                  "password": "short"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.details").isArray());
    }

    @Test
    void registerConflictReturnsConflict() throws Exception {
        when(authService.register(any()))
                .thenThrow(new ConflictException("Account already exists."));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Trevor Lussier",
                                  "email": "trevor@example.com",
                                  "password": "Password123!"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("CONFLICT"));
    }

    @Test
    void loginReturnsOk() throws Exception {
        when(authService.login(any())).thenReturn(authResponse(UUID.randomUUID()));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "trevor@example.com",
                                  "password": "Password123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }

    @Test
    void refreshUnauthorizedReturnsUnauthorized() throws Exception {
        when(authService.refresh(any()))
                .thenThrow(new UnauthorizedException("Refresh token is invalid."));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "bad-token"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    @Test
    void logoutReturnsNoContent() throws Exception {
        doNothing().when(authService).logout(any(RefreshRequest.class));

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "refreshToken": "refresh-token"
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    void getSessionReturnsSession() throws Exception {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.getSession(sessionId)).thenReturn(sessionResponse(sessionId));

        mockMvc.perform(get("/sessions/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId.toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getSessionNotFoundReturnsNotFound() throws Exception {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.getSession(sessionId))
                .thenThrow(new ResourceNotFoundException("Session not found."));

        mockMvc.perform(get("/sessions/{sessionId}", sessionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    void listSessionsReturnsArray() throws Exception {
        UUID userId = UUID.randomUUID();
        when(sessionService.listSessions(userId))
                .thenReturn(List.of(sessionResponse(UUID.randomUUID())));

        mockMvc.perform(get("/sessions").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Burnout Recovery Session"));
    }

    @Test
    void startSessionReturnsSession() throws Exception {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.startSession(eq(sessionId), any())).thenReturn(sessionResponse(sessionId));

        mockMvc.perform(post("/sessions/{sessionId}/start", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "title": "Burnout Recovery Session",
                                  "mode": "guided"
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId.toString()));
    }

    @Test
    void chatBadRequestReturnsBadRequest() throws Exception {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.chat(eq(sessionId), any()))
                .thenThrow(new BadRequestException("Message cannot be blank."));

        mockMvc.perform(post("/sessions/{sessionId}/chat", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "message": "I feel stuck."
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void endSessionReturnsSummary() throws Exception {
        UUID sessionId = UUID.randomUUID();
        SessionResponse response = sessionResponse(sessionId);
        response.setStatus("COMPLETED");
        response.setSummary("Worked through stress triggers.");
        when(sessionService.endSession(sessionId, "completed")).thenReturn(response);

        mockMvc.perform(post("/sessions/{sessionId}/end", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.summary").value("Worked through stress triggers."));
    }

    @Test
    void createDiaryEntryReturnsCreated() throws Exception {
        UUID entryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(diaryService.createDiaryEntry(any())).thenReturn(diaryEntry(entryId, userId));

        mockMvc.perform(post("/diary/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "automaticThought": "I always fail",
                                  "emotion": "anxious",
                                  "reflection": "I can challenge this thought."
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(entryId.toString()))
                .andExpect(jsonPath("$.suggestedDistortions[0]").value("All-or-Nothing Thinking"));
    }

    @Test
    void listDiaryEntriesReturnsArray() throws Exception {
        UUID userId = UUID.randomUUID();
        when(diaryService.getDiaryEntries(userId))
                .thenReturn(List.of(diaryEntry(UUID.randomUUID(), userId)));

        mockMvc.perform(get("/diary/entries").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].emotion").value("anxious"));
    }

    @Test
    void getDiaryEntryReturnsEntry() throws Exception {
        UUID entryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(diaryService.getDiaryEntry(entryId)).thenReturn(diaryEntry(entryId, userId));

        mockMvc.perform(get("/diary/entries/{entryId}", entryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entryId.toString()))
                .andExpect(jsonPath("$.automaticThought").value("I always fail"));
    }

    @Test
    void getDiaryInsightsReturnsInsights() throws Exception {
        UUID userId = UUID.randomUUID();
        when(diaryService.getDiaryInsights(userId)).thenReturn(diaryInsights(userId));

        mockMvc.perform(get("/diary/insights").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entryCount").value(3))
                .andExpect(jsonPath("$.topDistortions[0]").value("Catastrophizing"));
    }

    @Test
    void deleteDiaryEntryReturnsNoContent() throws Exception {
        UUID entryId = UUID.randomUUID();
        doNothing().when(diaryService).deleteDiaryEntry(entryId);

        mockMvc.perform(delete("/diary/entries/{entryId}", entryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void suggestDistortionsReturnsAnalysis() throws Exception {
        ThoughtAnalysisResponse response = new ThoughtAnalysisResponse();
        response.setDistortions(List.of("Mind Reading"));
        response.setReframingPrompts(List.of("What evidence supports that belief?"));
        when(diaryService.suggestDistortions(any())).thenReturn(response);

        mockMvc.perform(post("/diary/distortions/suggest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "Everyone thinks I'm failing."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distortions[0]").value("Mind Reading"));
    }

    @Test
    void weeklyProgressReturnsProgress() throws Exception {
        UUID userId = UUID.randomUUID();
        when(progressService.getWeeklyProgress(userId)).thenReturn(progressResponse(userId, "weekly"));

        mockMvc.perform(get("/progress/weekly").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timeframe").value("weekly"));
    }

    @Test
    void monthlyProgressUnhandledErrorReturnsInternalServerError() throws Exception {
        UUID userId = UUID.randomUUID();
        when(progressService.getMonthlyProgress(userId))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/progress/monthly").queryParam("userId", userId.toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error.code").value("INTERNAL_ERROR"));
    }

    @Test
    void burnoutProgressReturnsProgress() throws Exception {
        UUID userId = UUID.randomUUID();
        when(progressService.getBurnoutProgress(userId)).thenReturn(progressResponse(userId, "burnout"));

        mockMvc.perform(get("/progress/burnout").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.burnoutTrend").value("improving"));
    }

    @Test
    void achievementsReturnsAchievements() throws Exception {
        UUID userId = UUID.randomUUID();
        when(progressService.getAchievements(userId)).thenReturn(achievements(userId));

        mockMvc.perform(get("/progress/achievements").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.achievements[0]").value("Completed your first guided CBT session"));
    }

    @Test
    void crisisOverviewReturnsOverview() throws Exception {
        when(crisisService.getCrisisOverview()).thenReturn(crisisOverview());

        mockMvc.perform(get("/crisis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("none"));
    }

    @Test
    void crisisDetectReturnsAssessment() throws Exception {
        CrisisResponse response = new CrisisResponse();
        response.setCrisis(true);
        response.setRiskLevel("medium");
        response.setKeywordsDetected(List.of("hopeless"));
        response.setAction("show_crisis_hub");
        response.setReasoning("Elevated distress language detected.");
        when(crisisService.detectCrisis(any())).thenReturn(response);

        mockMvc.perform(post("/crisis/detect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "I feel hopeless."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crisis").value(true))
                .andExpect(jsonPath("$.action").value("show_crisis_hub"));
    }

    @Test
    void copingStrategiesReturnsStrategies() throws Exception {
        when(crisisService.getCopingStrategies()).thenReturn(copingStrategies());

        mockMvc.perform(get("/crisis/coping-strategies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategies[0]").value("Use a paced breathing cycle for one minute."));
    }

    @Test
    void getSafetyPlanReturnsPlan() throws Exception {
        UUID userId = UUID.randomUUID();
        when(crisisService.getSafetyPlan(userId)).thenReturn(safetyPlan(userId));

        mockMvc.perform(get("/crisis/safety-plan").queryParam("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.safetyPlan").exists());
    }

    @Test
    void updateSafetyPlanReturnsUpdatedPlan() throws Exception {
        UUID userId = UUID.randomUUID();
        SafetyPlanResponse response = new SafetyPlanResponse();
        response.setUserId(userId);
        response.setSafetyPlan("1. Call support.");
        when(crisisService.updateSafetyPlan(any(UpdateSafetyPlanRequest.class))).thenReturn(response);

        mockMvc.perform(put("/crisis/safety-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "safetyPlan": "1. Call support."
                                }
                                """.formatted(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.safetyPlan").value("1. Call support."));
    }

    private AuthResponse authResponse(UUID userId) {
        AuthResponse response = new AuthResponse();
        response.setUserId(userId);
        response.setFullName("Trevor Lussier");
        response.setEmail("trevor@example.com");
        response.setAccessToken("access-token");
        response.setRefreshToken("refresh-token");
        response.setTokenType("Bearer");
        return response;
    }

    private SessionResponse sessionResponse(UUID sessionId) {
        SessionResponse response = new SessionResponse();
        response.setSessionId(sessionId);
        response.setUserId(UUID.randomUUID());
        response.setTitle("Burnout Recovery Session");
        response.setStatus("ACTIVE");
        response.setStartedAt(Instant.parse("2026-04-08T18:00:00Z"));
        return response;
    }

    private DiaryEntryResponse diaryEntry(UUID entryId, UUID userId) {
        DiaryEntryResponse response = new DiaryEntryResponse();
        response.setId(entryId);
        response.setUserId(userId);
        response.setAutomaticThought("I always fail");
        response.setEmotion("anxious");
        response.setReflection("I can challenge this thought.");
        response.setSuggestedDistortions(List.of("All-or-Nothing Thinking", "Catastrophizing"));
        response.setCreatedAt(Instant.parse("2026-04-08T18:00:00Z"));
        return response;
    }

    private DiaryInsightsResponse diaryInsights(UUID userId) {
        DiaryInsightsResponse response = new DiaryInsightsResponse();
        response.setUserId(userId);
        response.setEntryCount(3);
        response.setTopDistortions(List.of("Catastrophizing", "Mind Reading"));
        response.setReframingPrompts(List.of("What facts support this thought?"));
        response.setInsightSummary("Recurring stress themes detected.");
        return response;
    }

    private ProgressResponse progressResponse(UUID userId, String timeframe) {
        ProgressResponse response = new ProgressResponse();
        response.setUserId(userId);
        response.setTimeframe(timeframe);
        response.setScore(0.62);
        response.setCompletedSessions(4);
        response.setDiaryEntries(5);
        response.setCrisisAlerts(0);
        response.setBurnoutTrend("improving");
        response.setHighlights(List.of("Momentum is improving."));
        return response;
    }

    private AchievementsResponse achievements(UUID userId) {
        AchievementsResponse response = new AchievementsResponse();
        response.setUserId(userId);
        response.setCompletedSessions(4);
        response.setDiaryEntries(5);
        response.setAchievements(List.of(
                "Completed your first guided CBT session",
                "Maintained a strong reflection habit"));
        response.setNextMilestone("Keep stacking sessions and reflections to sustain recovery progress.");
        return response;
    }

    private CrisisResponse crisisOverview() {
        CrisisResponse response = new CrisisResponse();
        response.setCrisis(false);
        response.setRiskLevel("none");
        response.setKeywordsDetected(List.of());
        response.setAction("none");
        response.setReasoning("No active crisis assessment.");
        return response;
    }

    private CopingStrategiesResponse copingStrategies() {
        CopingStrategiesResponse response = new CopingStrategiesResponse();
        response.setStrategies(List.of(
                "Use a paced breathing cycle for one minute.",
                "Name five things you can see, four you can touch, three you can hear."));
        response.setEmergencyResources(List.of("Call or text 988 for the Suicide & Crisis Lifeline."));
        response.setNote("Escalate immediately if safety risk increases.");
        return response;
    }

    private SafetyPlanResponse safetyPlan(UUID userId) {
        SafetyPlanResponse response = new SafetyPlanResponse();
        response.setUserId(userId);
        response.setSafetyPlan("1. Pause and breathe.\n2. Contact support.");
        return response;
    }
}
