package com.dta.entity;

import java.util.ArrayList;
import java.util.List;

public class CbtSession {
    // TODO [Timmy]: Add JPA mapping for CBT session state, focus area, and linked messages/insights.
    private Long id;
    private String focusArea;
    private List<ChatMessage> messages = new ArrayList<>();

    // getters/setters omitted for skeleton
}
