package com.dta.dto.request;

// TODO [Timmy]: Add field validation constraints and serialization rules.
public record CreateDiaryEntryRequest(String title, String content, String mood) {}
