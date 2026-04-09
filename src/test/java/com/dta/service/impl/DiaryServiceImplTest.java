package com.dta.service.impl;

import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.DiaryInsightsResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.entity.DiaryEntry;
import com.dta.exception.ResourceNotFoundException;
import com.dta.repository.DiaryEntryRepository;
import com.dta.service.AiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceImplTest {

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private DiaryServiceImpl diaryService;

    @Test
    void testCreateDiaryEntry_ReturnsResponse() {
        UUID userId = UUID.randomUUID();
        CreateDiaryEntryRequest request = new CreateDiaryEntryRequest();
        request.setUserId(userId);
        request.setAutomaticThought("I will fail");
        request.setEmotion("Anxious");
        request.setReflection("Maybe I just need to study");

        ThoughtAnalysisResponse mockAnalysis = new ThoughtAnalysisResponse();
        mockAnalysis.setDistortions(List.of("Fortune Telling", "Catastrophizing"));

        DiaryEntry savedEntry = new DiaryEntry();
        savedEntry.setId(UUID.randomUUID());
        savedEntry.setUserId(userId);
        savedEntry.setAutomaticThought("I will fail");
        savedEntry.setEmotion("Anxious");
        savedEntry.setReflection("Maybe I just need to study");
        savedEntry.setSuggestedDistortions("Fortune Telling, Catastrophizing");
        savedEntry.setCreatedAt(Instant.now());

        when(aiService.analyzeThought(anyString())).thenReturn(mockAnalysis);
        when(diaryEntryRepository.save(any(DiaryEntry.class))).thenReturn(savedEntry);

        DiaryEntryResponse response = diaryService.createDiaryEntry(request);

        assertNotNull(response);
        assertEquals("I will fail", response.getAutomaticThought());
        assertEquals(2, response.getSuggestedDistortions().size());
        assertTrue(response.getSuggestedDistortions().contains("Fortune Telling"));
        verify(aiService).analyzeThought("I will fail");
        verify(diaryEntryRepository).save(any(DiaryEntry.class));
    }

    @Test
    void testGetDiaryEntries_ReturnsList() {
        UUID userId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(UUID.randomUUID());
        entry.setUserId(userId);
        entry.setAutomaticThought("Nobody likes me");
        entry.setSuggestedDistortions("Mind Reading");

        when(diaryEntryRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(entry));

        List<DiaryEntryResponse> responses = diaryService.getDiaryEntries(userId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Nobody likes me", responses.get(0).getAutomaticThought());
        verify(diaryEntryRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void testGetDiaryEntry_ReturnsEntry() {
        UUID entryId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(entryId);
        entry.setUserId(userId);
        entry.setAutomaticThought("I messed everything up");
        entry.setSuggestedDistortions("Catastrophizing");

        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        DiaryEntryResponse response = diaryService.getDiaryEntry(entryId);

        assertNotNull(response);
        assertEquals(entryId, response.getId());
        assertEquals("I messed everything up", response.getAutomaticThought());
        assertEquals(List.of("Catastrophizing"), response.getSuggestedDistortions());
    }

    @Test
    void testGetDiaryEntry_WhenNotFound_ThrowsException() {
        UUID entryId = UUID.randomUUID();
        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> diaryService.getDiaryEntry(entryId));
    }

    @Test
    void testGetDiaryInsights_ReturnsSummaryAndTopDistortions() {
        UUID userId = UUID.randomUUID();

        DiaryEntry latest = new DiaryEntry();
        latest.setAutomaticThought("I always fail");
        latest.setSuggestedDistortions("All-or-Nothing Thinking, Catastrophizing");

        DiaryEntry older = new DiaryEntry();
        older.setAutomaticThought("Everyone is judging me");
        older.setSuggestedDistortions("Catastrophizing, Mind Reading");

        when(diaryEntryRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(latest, older));
        when(aiService.generateReframingPrompts("I always fail")).thenReturn(List.of("What facts support this thought?"));
        when(aiService.generateInsights(userId)).thenReturn("Recurring pressure themes detected.");

        DiaryInsightsResponse response = diaryService.getDiaryInsights(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(2, response.getEntryCount());
        assertEquals("Catastrophizing", response.getTopDistortions().get(0));
        assertEquals(List.of("What facts support this thought?"), response.getReframingPrompts());
        assertEquals("Recurring pressure themes detected.", response.getInsightSummary());
    }

    @Test
    void testDeleteDiaryEntry_WhenExists_DeletesSuccessfully() {
        UUID entryId = UUID.randomUUID();
        DiaryEntry entry = new DiaryEntry();
        entry.setId(entryId);

        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.of(entry));

        diaryService.deleteDiaryEntry(entryId);

        verify(diaryEntryRepository).deleteById(entryId);
    }

    @Test
    void testDeleteDiaryEntry_WhenNotFound_ThrowsException() {
        UUID entryId = UUID.randomUUID();
        when(diaryEntryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> diaryService.deleteDiaryEntry(entryId));
        verify(diaryEntryRepository, never()).deleteById(any());
    }

    @Test
    void testSuggestDistortions_ReturnsAnalysis() {
        DistortionSuggestionRequest request = new DistortionSuggestionRequest();
        request.setText("It is all my fault");

        ThoughtAnalysisResponse mockResponse = new ThoughtAnalysisResponse();
        mockResponse.setDistortions(List.of("Personalization"));

        when(aiService.analyzeThought("It is all my fault")).thenReturn(mockResponse);

        ThoughtAnalysisResponse result = diaryService.suggestDistortions(request);

        assertNotNull(result);
        assertEquals(1, result.getDistortions().size());
        assertEquals("Personalization", result.getDistortions().get(0));
        verify(aiService).analyzeThought("It is all my fault");
    }
}
