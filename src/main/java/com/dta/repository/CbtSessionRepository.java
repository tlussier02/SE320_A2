package com.dta.repository;

import com.dta.entity.CbtSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CbtSessionRepository extends JpaRepository<CbtSession, Long> {
    // Standard methods like save(), findAll(), and findById() are now inherited automatically.
}