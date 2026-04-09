package com.dta.controller;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.DiaryInsightsResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.service.DiaryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.CREATED)
    public DiaryEntryResponse createEntry(@Valid @RequestBody CreateDiaryEntryRequest request) {
        return diaryService.createDiaryEntry(request);
    }

    @GetMapping("/entries")
    public ResponseEntity<List<DiaryEntryResponse>> getEntries(@RequestParam UUID userId) {
        return ResponseEntity.ok(diaryService.getDiaryEntries(userId));
    }

    @GetMapping("/entries/{entryId}")
    public ResponseEntity<DiaryEntryResponse> getEntry(@PathVariable UUID entryId) {
        return ResponseEntity.ok(diaryService.getDiaryEntry(entryId));
    }

    @GetMapping("/insights")
    public ResponseEntity<DiaryInsightsResponse> getInsights(@RequestParam UUID userId) {
        return ResponseEntity.ok(diaryService.getDiaryInsights(userId));
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable UUID entryId) {
        diaryService.deleteDiaryEntry(entryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/distortions/suggest")
    public ResponseEntity<ThoughtAnalysisResponse> suggest(
            @Valid @RequestBody DistortionSuggestionRequest request) {
        return ResponseEntity.ok(diaryService.suggestDistortions(request));
    }
}
