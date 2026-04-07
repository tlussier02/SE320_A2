package com.dta.service.impl;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.entity.DiaryEntry;
import com.dta.exception.ResourceNotFoundException;
import com.dta.repository.DiaryEntryRepository;
import com.dta.service.AiService;
import com.dta.service.DiaryService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DiaryServiceImpl implements DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final AiService aiService;

    public DiaryServiceImpl(DiaryEntryRepository diaryEntryRepository, AiService aiService) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.aiService = aiService;
    }

    @Override
    @Transactional
    public DiaryEntryResponse createDiaryEntry(CreateDiaryEntryRequest request) {
        ThoughtAnalysisResponse analysis = aiService.analyzeThought(request.getAutomaticThought());

        DiaryEntry entry = new DiaryEntry();
        entry.setUserId(request.getUserId());
        entry.setAutomaticThought(request.getAutomaticThought());
        entry.setEmotion(request.getEmotion());
        entry.setReflection(request.getReflection());
        entry.setSuggestedDistortions(String.join(",", analysis.getDistortions()));

        return map(diaryEntryRepository.save(entry));
    }

    @Override
    public List<DiaryEntryResponse> getDiaryEntries(UUID userId) {
        return diaryEntryRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    @Transactional
    public void deleteDiaryEntry(UUID id) {
        diaryEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diary entry not found."));
        diaryEntryRepository.deleteById(id);
    }

    @Override
    public ThoughtAnalysisResponse suggestDistortions(DistortionSuggestionRequest request) {
        return aiService.analyzeThought(request.getText());
    }

    private DiaryEntryResponse map(DiaryEntry entry) {
        DiaryEntryResponse response = new DiaryEntryResponse();
        response.setId(entry.getId());
        response.setUserId(entry.getUserId());
        response.setAutomaticThought(entry.getAutomaticThought());
        response.setEmotion(entry.getEmotion());
        response.setReflection(entry.getReflection());
        response.setCreatedAt(entry.getCreatedAt());
        response.setSuggestedDistortions(split(entry.getSuggestedDistortions()));
        return response;
    }

    private List<String> split(String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
