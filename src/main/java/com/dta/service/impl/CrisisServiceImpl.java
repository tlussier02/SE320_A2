package com.dta.service.impl;

import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.service.CrisisService;
import com.dta.service.AiService;
import org.springframework.stereotype.Service;

@Service
public class CrisisServiceImpl implements CrisisService {

    private final AiService aiService;

    public CrisisServiceImpl(AiService aiService) {
        this.aiService = aiService;
    }

    // TODO [Josh]: Return crisis flag state for active authenticated user.
    @Override
    public Object getCrisisOverview() {
        return java.util.Map.of("active", false);
    }

    // TODO [Trevor]: Use AI-driven crisis detection and severity tagging before returning result.
    @Override
    public Object detectCrisis(DistortionSuggestionRequest request) {
        String risk = aiService.detectCrisis(request.text());
        return java.util.Map.of("crisis", risk);
    }

    // TODO [Timmy]: Load persisted safety-plan record and defaults.
    @Override
    public Object getSafetyPlan() {
        return java.util.Map.of("safetyPlan", "default plan");
    }

    // TODO [Timmy]: Persist safety plan updates and include audit metadata.
    @Override
    public Object updateSafetyPlan(UpdateSafetyPlanRequest request) {
        return java.util.Map.of("updated", true, "safetyPlan", request.safetyPlan());
    }
}
