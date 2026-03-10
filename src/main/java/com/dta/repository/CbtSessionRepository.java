package com.dta.repository;

import com.dta.entity.CbtSession;
import java.util.List;

public interface CbtSessionRepository {
    // TODO [Timmy]: Persist CBT plan/session metadata for analytics and reporting.
    List<CbtSession> findAll();
    // TODO [Timmy]: Save/update CBT flow nodes, milestones, and summary snapshots.
    CbtSession save(CbtSession session);
}
