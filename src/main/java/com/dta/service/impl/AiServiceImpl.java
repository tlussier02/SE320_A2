package com.dta.service.impl;

import com.dta.ai.CbtKnowledgeEntry;
import com.dta.ai.CrisisAssessmentResult;
import com.dta.ai.CrisisDetector;
import com.dta.ai.KnowledgeBaseLoader;
import com.dta.ai.RagContextBuilder;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.service.AiService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final RagContextBuilder contextBuilder;
    private final KnowledgeBaseLoader knowledgeBaseLoader;
    private final CrisisDetector crisisDetector;

    public AiServiceImpl(
            RagContextBuilder contextBuilder,
            KnowledgeBaseLoader knowledgeBaseLoader,
            CrisisDetector crisisDetector) {
        this.contextBuilder = contextBuilder;
        this.knowledgeBaseLoader = knowledgeBaseLoader;
        this.crisisDetector = crisisDetector;
    }

    @Override
    public String generateResponse(UUID userId, UUID sessionId, String message) {
        List<CbtKnowledgeEntry> matchedEntries = knowledgeBaseLoader.findMatchingEntries(message);
        String context = contextBuilder.build(
                message,
                userId.toString(),
                sessionId.toString(),
                matchedEntries
        );

        if (matchedEntries.isEmpty()) {
            return "I hear that this has been difficult. Let's slow the thought down and look for "
                    + "evidence for and against it. What facts support this thought, and what "
                    + "facts challenge it? [context=" + context + "]";
        }

        CbtKnowledgeEntry primaryMatch = matchedEntries.get(0);
        String firstPrompt = primaryMatch.reframingPromptSeeds().get(0);
        return "This sounds connected to " + primaryMatch.name() + ", which can make stress feel "
                + primaryMatch.description().toLowerCase() + " " + firstPrompt
                + " [context=" + context + "]";
    }

    @Override
    public ThoughtAnalysisResponse analyzeThought(String thought) {
        List<CbtKnowledgeEntry> matchedEntries = knowledgeBaseLoader.findMatchingEntries(thought);
        List<String> distortions = matchedEntries.stream()
                .map(CbtKnowledgeEntry::name)
                .distinct()
                .toList();

        ThoughtAnalysisResponse response = new ThoughtAnalysisResponse();
        response.setDistortions(
                distortions.isEmpty() ? List.of("no strong distortion detected") : distortions
        );
        response.setReframingPrompts(generateReframingPrompts(thought));
        return response;
    }

    @Override
    public List<String> generateReframingPrompts(String thought) {
        List<String> prompts = knowledgeBaseLoader.findMatchingEntries(thought)
                .stream()
                .flatMap(entry -> entry.reframingPromptSeeds().stream())
                .distinct()
                .toList();

        if (!prompts.isEmpty()) {
            return prompts;
        }

        return List.of(
                "What facts support this thought?",
                "What facts challenge this thought?",
                "What would a balanced alternative sound like?"
        );
    }

    @Override
    public CrisisResponse detectCrisis(String message) {
        CrisisAssessmentResult assessment = crisisDetector.detect(message);
        CrisisResponse response = new CrisisResponse();
        response.setCrisis(!"none".equalsIgnoreCase(assessment.riskLevel()));
        response.setRiskLevel(assessment.riskLevel());
        response.setKeywordsDetected(assessment.matchedKeywords());
        response.setAction(assessment.recommendedAction());
        response.setReasoning(assessment.reasoning());
        return response;
    }

    @Override
    public String generateInsights(UUID userId) {
        return "Recent entries for user " + userId + " suggest recurring stress themes with "
                + "opportunities for reframing all-or-nothing thoughts and building flexible "
                + "coping steps.";
    }

    @Override
    public String summarizeSession(UUID sessionId) {
        return "Session " + sessionId + " reviewed recent stressors, identified likely cognitive "
                + "distortions, and generated concrete reframing prompts for follow-up.";
    }
}
