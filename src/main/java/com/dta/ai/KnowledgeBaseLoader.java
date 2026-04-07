package com.dta.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeBaseLoader {

    private static final List<CbtKnowledgeEntry> KNOWLEDGE = List.of(
            new CbtKnowledgeEntry(
                    "all-or-nothing thinking",
                    "Viewing outcomes as total success or total failure.",
                    List.of("always", "never", "completely", "totally"),
                    List.of(
                            "What evidence shows this situation is not entirely one-sided?",
                            "Where is the middle ground between total failure and total success?"
                    )),
            new CbtKnowledgeEntry(
                    "catastrophizing",
                    "Assuming the worst possible outcome is certain.",
                    List.of("worst", "ruined", "disaster", "terrible", "awful"),
                    List.of(
                            "What is the most likely outcome instead of the worst-case one?",
                            "If the hard outcome happened, how would you cope with it?"
                    )),
            new CbtKnowledgeEntry(
                    "mind reading",
                    "Assuming you know what other people think without clear evidence.",
                    List.of("they think", "everyone thinks", "people think", "they must think"),
                    List.of(
                            "What direct evidence do you have about what others think?",
                            "What is another explanation for their behavior?"
                    )),
            new CbtKnowledgeEntry(
                    "overgeneralization",
                    "Using one event as proof that everything will keep going the same way.",
                    List.of("everything", "nothing ever", "every time", "all the time"),
                    List.of(
                            "Is this one event, or does it truly represent every situation?",
                            "Can you name one exception to this pattern?"
                    )),
            new CbtKnowledgeEntry(
                    "should statements",
                    "Using rigid rules about how you or others must behave.",
                    List.of("should", "must", "ought", "have to"),
                    List.of(
                            "What would a more flexible expectation sound like?",
                            "What is kinder and more realistic than this rule?"
                    ))
    );

    public List<String> loadCoreKnowledge() {
        return KNOWLEDGE.stream()
                .map(CbtKnowledgeEntry::name)
                .toList();
    }

    public List<CbtKnowledgeEntry> loadKnowledgeEntries() {
        return KNOWLEDGE;
    }

    public List<CbtKnowledgeEntry> findMatchingEntries(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String normalized = text.toLowerCase(Locale.ROOT);
        return KNOWLEDGE.stream()
                .filter(entry -> entry.triggerPhrases().stream().anyMatch(normalized::contains))
                .toList();
    }

    public List<String> tokenize(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return Arrays.stream(text.toLowerCase(Locale.ROOT).split("[^a-zA-Z']+"))
                .filter(token -> !token.isBlank())
                .toList();
    }
}
