package com.dta.controller;

import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.SafetyPlanResponse;
import com.dta.service.CrisisService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crisis")
public class CrisisController {

    private final CrisisService crisisService;

    public CrisisController(CrisisService crisisService) {
        this.crisisService = crisisService;
    }

    @GetMapping
    public ResponseEntity<CrisisResponse> crisisState() {
        return ResponseEntity.ok(crisisService.getCrisisOverview());
    }

    @PostMapping("/detect")
    public ResponseEntity<CrisisResponse> detect(
            @Valid @RequestBody DistortionSuggestionRequest request) {
        return ResponseEntity.ok(crisisService.detectCrisis(request));
    }

    @GetMapping("/safety-plan")
    public ResponseEntity<SafetyPlanResponse> getSafetyPlan(@RequestParam UUID userId) {
        return ResponseEntity.ok(crisisService.getSafetyPlan(userId));
    }

    @PutMapping("/safety-plan")
    public ResponseEntity<SafetyPlanResponse> updateSafetyPlan(
            @Valid @RequestBody UpdateSafetyPlanRequest request) {
        return ResponseEntity.ok(crisisService.updateSafetyPlan(request));
    }
}
