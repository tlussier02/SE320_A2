package com.dta.service;

import com.dta.dto.response.CopingStrategiesResponse;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.SafetyPlanResponse;

public interface CrisisService {

    CrisisResponse getCrisisOverview();

    CrisisResponse detectCrisis(DistortionSuggestionRequest request);

    CopingStrategiesResponse getCopingStrategies();

    SafetyPlanResponse getSafetyPlan(java.util.UUID userId);

    SafetyPlanResponse updateSafetyPlan(UpdateSafetyPlanRequest request);
}
