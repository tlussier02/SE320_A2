package com.dta.service;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import java.util.List;
import java.util.UUID;

public interface DiaryService {

    DiaryEntryResponse createDiaryEntry(CreateDiaryEntryRequest request);

    List<DiaryEntryResponse> getDiaryEntries(UUID userId);

    void deleteDiaryEntry(UUID id);

    ThoughtAnalysisResponse suggestDistortions(DistortionSuggestionRequest request);
}
