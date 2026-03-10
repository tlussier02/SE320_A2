package com.dta.service.impl;

import com.dta.ai.CrisisDetector;
import com.dta.ai.KnowledgeBaseLoader;
import com.dta.ai.RagContextBuilder;
import com.dta.service.AiService;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final RagContextBuilder contextBuilder;
    private final KnowledgeBaseLoader knowledgeBaseLoader;
    private final CrisisDetector crisisDetector;

    public AiServiceImpl(RagContextBuilder contextBuilder, KnowledgeBaseLoader knowledgeBaseLoader, CrisisDetector crisisDetector) {
        this.contextBuilder = contextBuilder;
        this.knowledgeBaseLoader = knowledgeBaseLoader;
        this.crisisDetector = crisisDetector;
    }

    // TODO [Trevor]: Build RAG prompt with session history + knowledge snippets and call LLM.
    @Override
    public String generateResponse(String userMessage, String userId) {
        var context = contextBuilder.build(userMessage, userId);
        var knowledge = knowledgeBaseLoader.loadCoreKnowledge();
        return "[AI Response] context=" + context + ", knowledge=" + knowledge;
    }

    // TODO [Trevor]: Return structured cognitive distortion analysis output for UI guidance.
    @Override
    public String analyzeThought(String thought) {
        return "analysis:" + thought;
    }

    // TODO [Trevor]: Generate CBT reframing prompts from thought schema and user context.
    @Override
    public String generateReframingPrompts(String thought) {
        return "reframe:" + thought;
    }

    // TODO [Trevor]: Delegate to CrisisDetector and map severity levels.
    @Override
    public String detectCrisis(String message) {
        return crisisDetector.detect(message);
    }

    // TODO [Trevor]: Aggregate insights by combining session and diary semantic signals.
    @Override
    public String generateInsights(String userId) {
        return "insights for=" + userId;
    }

    // TODO [Trevor]: Produce concise summary for counselor review and handoff.
    @Override
    public String summarizeSession(String sessionId) {
        return "summary for session=" + sessionId;
    }
}
