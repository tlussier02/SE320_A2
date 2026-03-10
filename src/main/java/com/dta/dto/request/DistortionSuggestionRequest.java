package com.dta.dto.request;

// TODO [Timmy]: Restrict payload length and sanitize user input before AI calls.
public record DistortionSuggestionRequest(String text) {}
