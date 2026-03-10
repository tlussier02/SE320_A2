package com.dta.service;

public interface ProgressService {
    // TODO [Timmy]: Return weekly metrics from session and diary data.
    Object getWeeklyProgress();
    // TODO [Timmy]: Return monthly trend metrics from persistent progress snapshots.
    Object getMonthlyProgress();
    // TODO [Timmy]: Return burnout indicators and scorecard for reporting endpoint.
    Object getBurnoutProgress();
}
