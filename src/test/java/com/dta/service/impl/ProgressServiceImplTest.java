package com.dta.service.impl;

import com.dta.dto.response.ProgressResponse;
import com.dta.repository.DiaryEntryRepository;
import com.dta.repository.UserSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock
    private UserSessionRepository userSessionRepository;

    @Mock
    private DiaryEntryRepository diaryEntryRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    @Test
    void testGetWeeklyProgress_ImprovingTrend() {
        UUID userId = UUID.randomUUID();
        
        // 3 completed sessions means trend should be "improving"
        when(userSessionRepository.countByUserIdAndStatusIgnoreCase(userId, "COMPLETED")).thenReturn(4L);
        when(diaryEntryRepository.countByUserId(userId)).thenReturn(5L);

        ProgressResponse response = progressService.getWeeklyProgress(userId);

        assertNotNull(response);
        assertEquals("weekly", response.getTimeframe());
        assertEquals(4, response.getCompletedSessions());
        assertEquals(5, response.getDiaryEntries());
        assertEquals("improving", response.getBurnoutTrend());
        
        // Base score for weekly is 0.10. 
        // 4 sessions * 0.05 = 0.20
        // 5 diaries * 0.03 = 0.15
        // Total = 0.45
        assertEquals(0.45, response.getScore(), 0.001);
    }

    @Test
    void testGetMonthlyProgress_EarlyStageTrend() {
        UUID userId = UUID.randomUUID();
        
        // Only 2 completed sessions means trend should be "early-stage"
        when(userSessionRepository.countByUserIdAndStatusIgnoreCase(userId, "COMPLETED")).thenReturn(2L);
        when(diaryEntryRepository.countByUserId(userId)).thenReturn(1L);

        ProgressResponse response = progressService.getMonthlyProgress(userId);

        assertNotNull(response);
        assertEquals("monthly", response.getTimeframe());
        assertEquals("early-stage", response.getBurnoutTrend());
        
        // Base score for monthly is 0.20
        // 2 sessions * 0.05 = 0.10
        // 1 diary * 0.03 = 0.03
        // Total = 0.33
        assertEquals(0.33, response.getScore(), 0.001);
    }

    @Test
    void testGetBurnoutProgress_ScoreCappedAtOne() {
        UUID userId = UUID.randomUUID();
        
        // Huge numbers to force the score over 1.0
        when(userSessionRepository.countByUserIdAndStatusIgnoreCase(userId, "COMPLETED")).thenReturn(50L);
        when(diaryEntryRepository.countByUserId(userId)).thenReturn(100L);

        ProgressResponse response = progressService.getBurnoutProgress(userId);

        assertNotNull(response);
        assertEquals("burnout", response.getTimeframe());
        
        // The score calculation would be way over 1.0, but it should cap exactly at 1.0
        assertEquals(1.0, response.getScore(), 0.001);
    }
}