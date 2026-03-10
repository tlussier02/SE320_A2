package com.dta.controller;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // TODO [Timmy]: Validate DTO, persist entry, and return 201 with location URI.
    @PostMapping("/entries")
    public ResponseEntity<Void> createEntry(@RequestBody CreateDiaryEntryRequest request) {
        diaryService.createDiaryEntry(request);
        return ResponseEntity.ok().build();
    }

    // TODO [Timmy]: Return filtered/paginated list of diary entries.
    @GetMapping("/entries")
    public ResponseEntity<?> getEntries() {
        return ResponseEntity.ok(diaryService.getDiaryEntries());
    }

    // TODO [Timmy]: Ensure authorization checks before delete.
    @DeleteMapping("/entries/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        diaryService.deleteDiaryEntry(id);
        return ResponseEntity.noContent().build();
    }

    // TODO [Timmy]: Return structured distortion suggestions from AI service.
    @PostMapping("/distortions/suggest")
    public ResponseEntity<?> suggest(@RequestBody DistortionSuggestionRequest request) {
        return ResponseEntity.ok(diaryService.suggestDistortions(request));
    }
}
