package com.dta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class UpdateSafetyPlanRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 5000)
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
