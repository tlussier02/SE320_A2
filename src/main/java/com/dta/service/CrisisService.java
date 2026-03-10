package com.dta.service;

import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;

public interface CrisisService {
    // TODO [Josh]: Return high-level crisis state from current user/session context.
    Object getCrisisOverview();
    // TODO [Trevor]: Implement risk-classification via AI/CrisisDetector before exposing escalation flags.
    Object detectCrisis(DistortionSuggestionRequest request);
    // TODO [Timmy]: Load default or persisted safety plan for authenticated user.
    Object getSafetyPlan();
    // TODO [Timmy]: Persist safety plan updates and version history.
    Object updateSafetyPlan(UpdateSafetyPlanRequest request);
}
