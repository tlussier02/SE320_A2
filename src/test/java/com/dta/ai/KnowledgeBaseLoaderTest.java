package com.dta.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class KnowledgeBaseLoaderTest {

    private final KnowledgeBaseLoader loader = new KnowledgeBaseLoader();

    @Test
    void loadCoreKnowledgeReturnsExpectedCategories() {
        List<String> categories = loader.loadCoreKnowledge();

        assertEquals(
                List.of(
                        "all-or-nothing thinking",
                        "catastrophizing",
                        "mind reading",
                        "overgeneralization",
                        "should statements"
                ),
                categories
        );
    }

    @Test
    void loadKnowledgeEntriesIncludesDeterministicPromptAndKeywordData() {
        List<CbtKnowledgeEntry> entries = loader.loadKnowledgeEntries();

        assertEquals(5, entries.size());
        assertTrue(entries.stream().allMatch(entry -> !entry.triggerPhrases().isEmpty()));
        assertTrue(entries.stream().allMatch(entry -> !entry.reframingPromptSeeds().isEmpty()));
        assertTrue(entries.stream().anyMatch(entry -> entry.name().equals("catastrophizing")));
    }

    @Test
    void findMatchingEntriesReturnsRelevantDistortionsForThoughtText() {
        List<CbtKnowledgeEntry> entries = loader.findMatchingEntries(
                "This is a disaster and everything is ruined."
        );

        assertFalse(entries.isEmpty());
        assertTrue(entries.stream().anyMatch(entry -> entry.name().equals("catastrophizing")));
        assertTrue(entries.stream().anyMatch(entry -> entry.name().equals("overgeneralization")));
    }
}
