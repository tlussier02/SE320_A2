package com.dta.dto.response;

import java.util.List;

public class CopingStrategiesResponse {

    private List<String> strategies;
    private List<String> emergencyResources;
    private String note;

    public List<String> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<String> strategies) {
        this.strategies = strategies;
    }

    public List<String> getEmergencyResources() {
        return emergencyResources;
    }

    public void setEmergencyResources(List<String> emergencyResources) {
        this.emergencyResources = emergencyResources;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
