package com.dta.ai;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class CrisisDetector {

    public CrisisAssessmentResult detect(String message) {
        String normalized = message == null ? "" : message.toLowerCase(Locale.ROOT);

        List<String> criticalMatches = findMatches(
                normalized,
                List.of("suicide", "kill myself", "end it all", "want to die", "hurt myself")
        );
        if (!criticalMatches.isEmpty()) {
            return new CrisisAssessmentResult(
                    "critical",
                    criticalMatches,
                    "immediate_intervention",
                    "Direct self-harm or suicide intent language detected."
            );
        }

        List<String> mediumMatches = findMatches(
                normalized,
                List.of("hopeless", "can't go on", "burden", "no point", "trapped")
        );
        if (!mediumMatches.isEmpty()) {
            return new CrisisAssessmentResult(
                    "medium",
                    mediumMatches,
                    "show_crisis_hub",
                    "Elevated distress language detected."
            );
        }

        List<String> lowMatches = findMatches(
                normalized,
                List.of("panic", "overwhelmed", "breaking down", "falling apart")
        );
        if (!lowMatches.isEmpty()) {
            return new CrisisAssessmentResult(
                    "low",
                    lowMatches,
                    "offer_grounding_support",
                    "Stress language detected without direct crisis intent."
            );
        }

        return new CrisisAssessmentResult(
                "none",
                List.of(),
                "none",
                "No crisis indicators detected."
        );
    }

    private List<String> findMatches(String normalizedMessage, List<String> phrases) {
        return phrases.stream()
                .filter(normalizedMessage::contains)
                .toList();
    }
}
