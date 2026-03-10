package com.dta.service.impl;

import com.dta.service.ProgressService;
import org.springframework.stereotype.Service;

@Service
public class ProgressServiceImpl implements ProgressService {

    // TODO [Timmy]: Query persisted session/diary indicators and return weekly aggregate.
    @Override
    public Object getWeeklyProgress() {
        return java.util.Map.of("timeframe", "weekly", "score", 0.0);
    }

    // TODO [Timmy]: Query persisted indicators and return monthly aggregate view model.
    @Override
    public Object getMonthlyProgress() {
        return java.util.Map.of("timeframe", "monthly", "score", 0.0);
    }

    // TODO [Timmy]: Compute burnout score from session frequency, distress signals, and mood trend.
    @Override
    public Object getBurnoutProgress() {
        return java.util.Map.of("timeframe", "burnout", "score", 0.0);
    }
}
