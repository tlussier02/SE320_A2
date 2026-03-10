package com.dta.controller;

import com.dta.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // TODO [Timmy]: Return strongly typed weekly progress response DTO.
    @GetMapping("/weekly")
    public ResponseEntity<?> weekly() {
        return ResponseEntity.ok(progressService.getWeeklyProgress());
    }

    // TODO [Timmy]: Return strongly typed monthly progress response DTO.
    @GetMapping("/monthly")
    public ResponseEntity<?> monthly() {
        return ResponseEntity.ok(progressService.getMonthlyProgress());
    }

    // TODO [Timmy]: Return burnout endpoint with trend data and recommendations.
    @GetMapping("/burnout")
    public ResponseEntity<?> burnout() {
        return ResponseEntity.ok(progressService.getBurnoutProgress());
    }
}
