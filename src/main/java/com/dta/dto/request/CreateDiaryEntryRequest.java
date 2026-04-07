package com.dta.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class CreateDiaryEntryRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 3000)
    @JsonAlias({"title", "thought"})
    private String automaticThought;

    @NotBlank
    @Size(max = 255)
    @JsonAlias("mood")
    private String emotion;

    @NotBlank
    @Size(max = 4000)
    @JsonAlias("content")
    private String reflection;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAutomaticThought() {
        return automaticThought;
    }

    public void setAutomaticThought(String automaticThought) {
        this.automaticThought = automaticThought;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getReflection() {
        return reflection;
    }

    public void setReflection(String reflection) {
        this.reflection = reflection;
    }
}
