package com.dta.dto.response;

import java.util.List;

public class ThoughtAnalysisResponse {

    private List<String> distortions;
    private List<String> reframingPrompts;

    public List<String> getDistortions() {
        return distortions;
    }

    public void setDistortions(List<String> distortions) {
        this.distortions = distortions;
    }

    public List<String> getReframingPrompts() {
        return reframingPrompts;
    }

    public void setReframingPrompts(List<String> reframingPrompts) {
        this.reframingPrompts = reframingPrompts;
    }
}
