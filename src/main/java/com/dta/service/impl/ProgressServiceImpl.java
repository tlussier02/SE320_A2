package com.dta.service.impl;

import com.dta.dto.response.AchievementsResponse;
import com.dta.dto.response.ProgressResponse;
import com.dta.repository.DiaryEntryRepository;
import com.dta.repository.UserSessionRepository;
import com.dta.service.ProgressService;
import java.util.ArrayList;
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
        return buildProgress(userId, "weekly", 0.10);
    }

    @Override
    public ProgressResponse getMonthlyProgress(UUID userId) {
        return buildProgress(userId, "monthly", 0.20);
    }

    @Override
    public ProgressResponse getBurnoutProgress(UUID userId) {
        return buildProgress(userId, "burnout", 0.30);
    }

    @Override
    public AchievementsResponse getAchievements(UUID userId) {
        int completedSessions = (int) userSessionRepository
                .countByUserIdAndStatusIgnoreCase(userId, "COMPLETED");
        int diaryEntries = (int) diaryEntryRepository.countByUserId(userId);

        List<String> achievements = new ArrayList<>();
        if (completedSessions >= 1) {
            achievements.add("Completed your first guided CBT session");
        }
        if (diaryEntries >= 1) {
            achievements.add("Captured your first thought diary entry");
        }
        if (completedSessions >= 3) {
            achievements.add("Built steady session momentum");
        }
        if (diaryEntries >= 5) {
            achievements.add("Maintained a strong reflection habit");
        }
        if (achievements.isEmpty()) {
            achievements.add("Start with one session or one diary entry to unlock progress milestones");
        }

        AchievementsResponse response = new AchievementsResponse();
        response.setUserId(userId);
        response.setCompletedSessions(completedSessions);
        response.setDiaryEntries(diaryEntries);
        response.setAchievements(achievements);
        response.setNextMilestone(buildNextMilestone(completedSessions, diaryEntries));
        return response;
    }

    private ProgressResponse buildProgress(UUID userId, String timeframe, double baseScore) {
        // Replace hardcoded counts with real database queries
        int completedSessions = (int) userSessionRepository
                .countByUserIdAndStatusIgnoreCase(userId, "COMPLETED");
        int diaryEntries = (int) diaryEntryRepository.countByUserId(userId);

        ProgressResponse response = new ProgressResponse();
        response.setUserId(userId);
        response.setTimeframe(timeframe);
        
        // Calculate a dynamic score capped at 1.0 (100%)
        double calculatedScore = baseScore + (completedSessions * 0.05) + (diaryEntries * 0.03);
        response.setScore(Math.min(1.0, calculatedScore));
        
        response.setCompletedSessions(completedSessions);
        response.setDiaryEntries(diaryEntries);
        response.setCrisisAlerts(0); // This can be updated once CrisisService tracking is added
        
        response.setBurnoutTrend(completedSessions >= 3 ? "improving" : "early-stage");
        response.setHighlights(List.of(
                "Session consistency is improving.",
                "Thought diary activity is being tracked.",
                "Continue weekly reflection and recovery planning."
        ));
        return response;
    }

    private String buildNextMilestone(int completedSessions, int diaryEntries) {
        if (completedSessions < 3) {
            return "Complete " + (3 - completedSessions) + " more session(s) to unlock momentum.";
        }
        if (diaryEntries < 5) {
            return "Add " + (5 - diaryEntries) + " more diary entr" + (diaryEntries == 4 ? "y" : "ies")
                    + " to unlock the reflection habit milestone.";
        }
        return "Keep stacking sessions and reflections to sustain recovery progress.";
    }
}
