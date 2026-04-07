package com.dta.ai;

import java.util.List;

public record CrisisAssessmentResult(
        String riskLevel,
        List<String> matchedKeywords,
        String recommendedAction,
        String reasoning) {
}
