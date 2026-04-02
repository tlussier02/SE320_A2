package com.dta.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dta.ai.CrisisDetector;
import com.dta.ai.KnowledgeBaseLoader;
import com.dta.ai.RagContextBuilder;
import com.dta.dto.response.ThoughtAnalysisResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AiServiceImplTest {

    private final AiServiceImpl aiService = new AiServiceImpl(
            new RagContextBuilder(),
            new KnowledgeBaseLoader(),
            new CrisisDetector()
    );

    @Test
    void analyzeThoughtMapsAbsoluteLanguageToAllOrNothingThinking() {
        ThoughtAnalysisResponse response = aiService.analyzeThought(
                "I always fail and I never do enough."
        );

        assertTrue(response.getDistortions().contains("all-or-nothing thinking"));
    }

    @Test
    void analyzeThoughtMapsCatastrophizingLanguage() {
        ThoughtAnalysisResponse response = aiService.analyzeThought(
                "This is the worst disaster and everything is ruined."
        );

        assertTrue(response.getDistortions().contains("catastrophizing"));
    }

    @Test
    void analyzeThoughtReturnsFallbackWhenNoStrongMatchExists() {
        ThoughtAnalysisResponse response = aiService.analyzeThought(
                "I want to understand why this meeting felt hard."
        );

        assertEquals(List.of("no strong distortion detected"), response.getDistortions());
    }

    @Test
    void generateReframingPromptsUsesMatchedDistortionPrompts() {
        List<String> prompts = aiService.generateReframingPrompts(
                "This is the worst possible outcome."
        );

        assertFalse(prompts.isEmpty());
        assertTrue(prompts.stream().anyMatch(prompt -> prompt.contains("most likely outcome")));
    }

    @Test
    void generateResponseUsesMatchedKnowledgeAndChangesByInput() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        String catastrophizing = aiService.generateResponse(
                userId,
                sessionId,
                "This is the worst disaster."
        );
        String shouldStatements = aiService.generateResponse(
                userId,
                sessionId,
                "I should be perfect all the time."
        );

        assertTrue(catastrophizing.contains("catastrophizing"));
        assertTrue(shouldStatements.contains("should statements"));
        assertNotEquals(catastrophizing, shouldStatements);
    }
}
