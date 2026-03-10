package com.dta.service.impl;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.service.AiService;
import com.dta.service.DiaryService;
import org.springframework.stereotype.Service;

@Service
public class DiaryServiceImpl implements DiaryService {

    private final AiService aiService;

    public DiaryServiceImpl(AiService aiService) {
        this.aiService = aiService;
    }

    // TODO [Timmy]: Validate diary text and persist entry + derived distortion links.
    @Override
    public void createDiaryEntry(CreateDiaryEntryRequest request) {
        // TODO [Timmy]: map request to entity and persist
    }

    // TODO [Timmy]: Implement paged retrieval and sorting for user diary entries.
    @Override
    public Object getDiaryEntries() {
        return java.util.Collections.emptyList();
    }

    // TODO [Timmy]: Implement find-and-delete with authorization checks.
    @Override
    public void deleteDiaryEntry(Long id) {
        // TODO [Timmy]: locate and delete diary entry by id
    }

    // TODO [Trevor]: Run distortion analysis pipeline and return candidate distortions.
    @Override
    public Object suggestDistortions(DistortionSuggestionRequest request) {
        return aiService.analyzeThought(request.text());
    }
}
