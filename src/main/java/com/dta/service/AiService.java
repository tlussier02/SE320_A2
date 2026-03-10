package com.dta.service;

public interface AiService {
    // TODO [Trevor]: Orchestrate RAG context + CBT guidance prompt and return therapeutic response.
    String generateResponse(String userMessage, String userId);
    // TODO [Trevor]: Add cognitive distortion detection and classification logic.
    String analyzeThought(String thought);
    // TODO [Trevor]: Generate CBT reframing prompts based on detected thought patterns.
    String generateReframingPrompts(String thought);
    // TODO [Trevor]: Implement crisis signal scoring and escalation rules.
    String detectCrisis(String message);
    // TODO [Trevor]: Summarize historical session learnings/insights for continuity.
    String generateInsights(String userId);
    // TODO [Trevor]: Build session-level summary after chat end for therapist-style handoff.
    String summarizeSession(String sessionId);
}
