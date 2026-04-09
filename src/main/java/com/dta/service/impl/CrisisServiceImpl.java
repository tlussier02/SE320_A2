package com.dta.service.impl;

import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.CopingStrategiesResponse;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.SafetyPlanResponse;
import com.dta.service.AiService;
import com.dta.service.CrisisService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CrisisServiceImpl implements CrisisService {

    private static final String DEFAULT_SAFETY_PLAN =
            "1. Pause and breathe.\n2. Contact a trusted person.\n3. Use crisis resources if risk escalates.";

    private final AiService aiService;
    private final Map<UUID, String> safetyPlans = new ConcurrentHashMap<>();

    public CrisisServiceImpl(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public CrisisResponse getCrisisOverview() {
        CrisisResponse response = new CrisisResponse();
        response.setCrisis(false);
        response.setRiskLevel("none");
        response.setKeywordsDetected(List.of());
        response.setAction("none");
        response.setReasoning("No active crisis assessment has been triggered.");
        return response;
    }

    @Override
    public CrisisResponse detectCrisis(DistortionSuggestionRequest request) {
        return aiService.detectCrisis(request.getText());
    }

    @Override
    public CopingStrategiesResponse getCopingStrategies() {
        CopingStrategiesResponse response = new CopingStrategiesResponse();
        response.setStrategies(List.of(
                "Use a paced breathing cycle for one minute.",
                "Name five things you can see, four you can touch, three you can hear.",
                "Step away from the stressor and message a trusted contact.",
                "Write one balanced alternative thought before taking the next action."
        ));
        response.setEmergencyResources(List.of(
                "Call or text 988 for the Suicide & Crisis Lifeline.",
                "Call 911 or go to the nearest emergency room if you are in immediate danger."
        ));
        response.setNote("Use these grounding steps for rising distress. Escalate immediately if safety risk increases.");
        return response;
    }

    @Override
    public SafetyPlanResponse getSafetyPlan(UUID userId) {
        SafetyPlanResponse response = new SafetyPlanResponse();
        response.setUserId(userId);
        response.setSafetyPlan(safetyPlans.getOrDefault(userId, DEFAULT_SAFETY_PLAN));
        return response;
    }

    @Override
    public SafetyPlanResponse updateSafetyPlan(UpdateSafetyPlanRequest request) {
        safetyPlans.put(request.getUserId(), request.getSafetyPlan());
        SafetyPlanResponse response = new SafetyPlanResponse();
        response.setUserId(request.getUserId());
        response.setSafetyPlan(request.getSafetyPlan());
        return response;
    }
}
