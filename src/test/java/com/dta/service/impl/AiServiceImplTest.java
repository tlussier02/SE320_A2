package com.dta.service.impl;

import com.dta.ai.*;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiServiceImplTest {

    @Mock
    private RagContextBuilder contextBuilder;

    @Mock
    private KnowledgeBaseLoader knowledgeBaseLoader;

    @Mock
    private CrisisDetector crisisDetector;

    @InjectMocks
    private AiServiceImpl aiService;

    @Test
    void testGenerateResponse_WithMatchedEntries() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        String message = "I feel like a failure.";

        CbtKnowledgeEntry entry = new CbtKnowledgeEntry(
                "Labeling", 
                "Extreme labels", 
                List.of("Try a kinder label."),
                List.of("labeling") 
        );

        when(knowledgeBaseLoader.findMatchingEntries(anyString())).thenReturn(List.of(entry));
        when(contextBuilder.build(anyString(), anyString(), anyString(), anyList())).thenReturn("mockContext");

        String response = aiService.generateResponse(userId, sessionId, message);

        // Matches "This sounds connected to Labeling"
        assertTrue(response.contains("Labeling"));
        // Matches "extreme labels" because your service calls .toLowerCase() on description
        assertTrue(response.toLowerCase().contains("extreme labels"));
    }

    @Test
    void testGenerateResponse_EmptyEntries() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        
        when(knowledgeBaseLoader.findMatchingEntries(anyString())).thenReturn(Collections.emptyList());
        when(contextBuilder.build(anyString(), anyString(), anyString(), anyList())).thenReturn("emptyContext");

        String response = aiService.generateResponse(userId, sessionId, "general chat");

        assertTrue(response.contains("evidence for and against it"));
    }

    @Test
    void testAnalyzeThought_NoDistortions() {
        when(knowledgeBaseLoader.findMatchingEntries(anyString())).thenReturn(Collections.emptyList());

        ThoughtAnalysisResponse response = aiService.analyzeThought("I am eating an apple.");

        assertEquals("no strong distortion detected", response.getDistortions().get(0));
        assertEquals(3, response.getReframingPrompts().size());
    }

    @Test
    void testDetectCrisis_Positive() {
        CrisisAssessmentResult assessment = new CrisisAssessmentResult(
                "high", 
                List.of("harm"), 
                "Call 988", 
                "Risk detected"
        );
        when(crisisDetector.detect(anyString())).thenReturn(assessment);

        CrisisResponse response = aiService.detectCrisis("I want to hurt myself");

        assertTrue(response.isCrisis());
        assertEquals("high", response.getRiskLevel());
    }

    @Test
    void testSummaries() {
        UUID id = UUID.randomUUID();
        assertNotNull(aiService.generateInsights(id));
        assertNotNull(aiService.summarizeSession(id));
    }
}