package com.dta.service.impl;

import com.dta.dto.response.ProgressResponse;
import com.dta.repository.DiaryEntryRepository;
import com.dta.repository.UserSessionRepository;
import com.dta.service.ProgressService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final UserSessionRepository userSessionRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    public ProgressServiceImpl(
            UserSessionRepository userSessionRepository,
            DiaryEntryRepository diaryEntryRepository) {
        this.userSessionRepository = userSessionRepository;
        this.diaryEntryRepository = diaryEntryRepository;
    }

    @Override
    public ProgressResponse getWeeklyProgress(UUID userId) {
        return buildProgress(userId, "weekly", 0.45);
    }

    @Override
    public ProgressResponse getMonthlyProgress(UUID userId) {
        return buildProgress(userId, "monthly", 0.62);
    }

    @Override
    public ProgressResponse getBurnoutProgress(UUID userId) {
        return buildProgress(userId, "burnout", 0.58);
    }

    private ProgressResponse buildProgress(UUID userId, String timeframe, double baseScore) {
        int completedSessions = (int) userSessionRepository
                .countByUserIdAndStatusIgnoreCase(userId, "COMPLETED");
        int diaryEntries = (int) diaryEntryRepository.countByUserId(userId);

        ProgressResponse response = new ProgressResponse();
        response.setUserId(userId);
        response.setTimeframe(timeframe);
        response.setScore(Math.min(1.0, baseScore + (completedSessions * 0.05) + (diaryEntries * 0.03)));
        response.setCompletedSessions(completedSessions);
        response.setDiaryEntries(diaryEntries);
        response.setCrisisAlerts(0);
        response.setBurnoutTrend(completedSessions >= 3 ? "improving" : "early-stage");
        response.setHighlights(List.of(
                "Session consistency is improving.",
                "Thought diary activity is being tracked.",
                "Continue weekly reflection and recovery planning."
        ));
        return response;
    }
}
