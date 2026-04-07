package com.dta.service;

import com.dta.dto.response.ProgressResponse;
import java.util.UUID;

public interface ProgressService {

    ProgressResponse getWeeklyProgress(UUID userId);

    ProgressResponse getMonthlyProgress(UUID userId);

    ProgressResponse getBurnoutProgress(UUID userId);
}
