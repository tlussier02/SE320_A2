package com.dta.dto.response;

import java.util.List;

public class CrisisResponse {

    private boolean crisis;
    private String riskLevel;
    private List<String> keywordsDetected;
    private String action;
    private String reasoning;

    public boolean isCrisis() {
        return crisis;
    }

    public void setCrisis(boolean crisis) {
        this.crisis = crisis;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<String> getKeywordsDetected() {
        return keywordsDetected;
    }

    public void setKeywordsDetected(List<String> keywordsDetected) {
        this.keywordsDetected = keywordsDetected;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
