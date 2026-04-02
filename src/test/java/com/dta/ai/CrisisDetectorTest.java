package com.dta.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CrisisDetectorTest {

    private final CrisisDetector detector = new CrisisDetector();

    @Test
    void detectReturnsCriticalForDirectSelfHarmLanguage() {
        CrisisAssessmentResult result = detector.detect("I want to kill myself tonight.");

        assertEquals("critical", result.riskLevel());
        assertEquals("immediate_intervention", result.recommendedAction());
        assertTrue(result.matchedKeywords().contains("kill myself"));
    }

    @Test
    void detectReturnsMediumForHopelessDistressLanguage() {
        CrisisAssessmentResult result = detector.detect(
                "I feel hopeless and like I am a burden."
        );

        assertEquals("medium", result.riskLevel());
        assertEquals("show_crisis_hub", result.recommendedAction());
        assertTrue(result.matchedKeywords().contains("hopeless"));
    }

    @Test
    void detectReturnsNoneForNeutralLanguage() {
        CrisisAssessmentResult result = detector.detect(
                "I had a stressful meeting and want to think more clearly."
        );

        assertEquals("none", result.riskLevel());
        assertEquals("none", result.recommendedAction());
        assertTrue(result.matchedKeywords().isEmpty());
    }
}
