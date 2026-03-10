package com.dta.controller;

import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.service.CrisisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crisis")
public class CrisisController {

    private final CrisisService crisisService;

    public CrisisController(CrisisService crisisService) {
        this.crisisService = crisisService;
    }

    // TODO [Timmy]: Return crisis status DTO based on active session risk + user data.
    @GetMapping
    public ResponseEntity<?> crisisState() {
        return ResponseEntity.ok(crisisService.getCrisisOverview());
    }

    // TODO [Trevor]: Use AI classification before returning crisis decision.
    @PostMapping("/detect")
    public ResponseEntity<?> detect(@RequestBody DistortionSuggestionRequest request) {
        return ResponseEntity.ok(crisisService.detectCrisis(request));
    }

    // TODO [Timmy]: Return user-specific safety plan object.
    @GetMapping("/safety-plan")
    public ResponseEntity<?> getSafetyPlan() {
        return ResponseEntity.ok(crisisService.getSafetyPlan());
    }

    // TODO [Timmy]: Persist updated safety plan with validation and audit trail.
    @PutMapping("/safety-plan")
    public ResponseEntity<?> updateSafetyPlan(@RequestBody UpdateSafetyPlanRequest request) {
        return ResponseEntity.ok(crisisService.updateSafetyPlan(request));
    }
}
