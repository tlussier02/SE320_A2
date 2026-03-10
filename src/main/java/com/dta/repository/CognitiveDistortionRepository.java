package com.dta.repository;

import com.dta.entity.CognitiveDistortion;
import java.util.List;

public interface CognitiveDistortionRepository {
    // TODO [Timmy]: Add seeded lookup + fuzzy search over distortion catalog and related CBT prompts.
    List<CognitiveDistortion> findAll();
}
