package com.dta.ai;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RagContextBuilder {

    public String build(
            String message,
            String userId,
            String sessionId,
            List<CbtKnowledgeEntry> matchedEntries) {
        String matched = matchedEntries.stream()
                .limit(2)
                .map(entry -> entry.name() + ": " + entry.description())
                .reduce((left, right) -> left + " | " + right)
                .orElse("no-strong-distortion-match");

        return "user=" + userId
                + ", session=" + sessionId
                + ", message=" + message
                + ", matched=" + matched;
    }
}
