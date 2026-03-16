package com.dta.service;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryService {
    
    // Validates payload and saves the entry with its distortion links
    void createDiaryEntry(CreateDiaryEntryRequest request);

    // Retrieves entries for the current user with paging and filtering
    Page<Object> getDiaryEntries(Pageable pageable);

    // Deletes an entry and cleans up associations after owner verification
    void deleteDiaryEntry(Long id);

    // Invokes the AI helper to analyze text and suggest distortions
    Object suggestDistortions(DistortionSuggestionRequest request);
}
