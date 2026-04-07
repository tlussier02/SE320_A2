package com.dta.repository;

import com.dta.entity.CognitiveDistortion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CognitiveDistortionRepository extends JpaRepository<CognitiveDistortion, Long> {
    
    /**
     * Supports the seeded lookup requirement by finding a distortion by its label.
     * This is used by the AI service to map identified thoughts to specific CBT prompts.
     */
    Optional<CognitiveDistortion> findByLabelIgnoreCase(String label);
}