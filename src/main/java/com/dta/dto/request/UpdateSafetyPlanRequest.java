package com.dta.dto.request;

public record UpdateSafetyPlanRequest(String safetyPlan) {
    // TODO [Timmy]: Validate and persist updated user safety-plan text via crisis workflow.
}
