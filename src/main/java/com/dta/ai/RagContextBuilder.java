package com.dta.ai;

public class RagContextBuilder {
    // TODO [Trevor]: Build session + diary context slice for retrieval-augmented generation.
    public String build(String message, String userId) {
        return "user=" + userId + ", message=" + message;
    }
}
