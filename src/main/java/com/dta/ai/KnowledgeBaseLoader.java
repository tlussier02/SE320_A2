package com.dta.ai;

import java.util.List;

public class KnowledgeBaseLoader {

    // TODO [Trevor]: Load CBT knowledge from files/database, including all required distortion types and techniques.
    public List<String> loadCoreKnowledge() {
        return List.of(
            "all-or-nothing thinking",
            "catastrophizing",
            "mind reading",
            "overgeneralization",
            "should statements"
        );
    }
}
