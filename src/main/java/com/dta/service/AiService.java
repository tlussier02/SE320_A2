package com.dta.service;

import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import java.util.List;
import java.util.UUID;

public interface AiService {

    String generateResponse(UUID userId, UUID sessionId, String message);

    ThoughtAnalysisResponse analyzeThought(String thought);

    List<String> generateReframingPrompts(String thought);

    CrisisResponse detectCrisis(String message);

    String generateInsights(UUID userId);

    String summarizeSession(UUID sessionId);
}
