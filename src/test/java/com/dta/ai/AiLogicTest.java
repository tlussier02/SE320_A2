package com.dta.ai;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AiLogicTest {
    @Test
    void testRealAiLogic() {
        // Test real detector
        CrisisDetector detector = new CrisisDetector();
        var result = detector.detect("I am safe");
        assertEquals("none", result.riskLevel());

        // Test real knowledge base
        KnowledgeBaseLoader loader = new KnowledgeBaseLoader();
        var matches = loader.findMatchingEntries("I always fail");
        assertFalse(matches.isEmpty());

        // Test context builder
        RagContextBuilder builder = new RagContextBuilder();
        String context = builder.build("msg", "u1", "s1", matches);
        assertNotNull(context);
    }
}