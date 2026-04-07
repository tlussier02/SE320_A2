package com.dta.dto.response;

import java.util.UUID;

public class SafetyPlanResponse {

    private UUID userId;
    private String safetyPlan;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getSafetyPlan() {
        return safetyPlan;
    }

    public void setSafetyPlan(String safetyPlan) {
        this.safetyPlan = safetyPlan;
    }
}
