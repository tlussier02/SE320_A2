package com.dta.dto.response;

public record ProgressResponse(String timeframe, double score) {
    // TODO [Timmy]: Expose structured progress metrics and aggregation window metadata.
}
