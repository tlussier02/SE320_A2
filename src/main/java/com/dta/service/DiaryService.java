package com.dta.service;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;

public interface DiaryService {
    // TODO [Timmy]: Validate diary payload and persist DiaryEntry + distortions metadata.
    void createDiaryEntry(CreateDiaryEntryRequest request);
    // TODO [Timmy]: Retrieve diary entries for current user with paging/filter support.
    Object getDiaryEntries();
    // TODO [Timmy]: Delete diary entry and clean up related distortion associations.
    void deleteDiaryEntry(Long id);
    // TODO [Timmy]: Suggest distortions by invoking AI helper for text analysis.
    Object suggestDistortions(DistortionSuggestionRequest request);
}
