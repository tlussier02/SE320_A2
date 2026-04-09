package com.dta.controller;

import com.dta.dto.response.AchievementsResponse;
import com.dta.dto.response.ProgressResponse;
import com.dta.service.ProgressService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/weekly")
    public ResponseEntity<ProgressResponse> weekly(@RequestParam UUID userId) {
        return ResponseEntity.ok(progressService.getWeeklyProgress(userId));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ProgressResponse> monthly(@RequestParam UUID userId) {
        return ResponseEntity.ok(progressService.getMonthlyProgress(userId));
    }

    @GetMapping("/burnout")
    public ResponseEntity<ProgressResponse> burnout(@RequestParam UUID userId) {
        return ResponseEntity.ok(progressService.getBurnoutProgress(userId));
    }

    @GetMapping("/achievements")
    public ResponseEntity<AchievementsResponse> achievements(@RequestParam UUID userId) {
        return ResponseEntity.ok(progressService.getAchievements(userId));
    }
}
