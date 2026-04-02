package com.dta.ai;

import java.util.List;

public record CbtKnowledgeEntry(
        String name,
        String description,
        List<String> triggerPhrases,
        List<String> reframingPromptSeeds) {
}
