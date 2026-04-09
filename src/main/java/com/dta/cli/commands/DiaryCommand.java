package com.dta.cli.commands;

import com.dta.cli.CliPrompts;
import com.dta.cli.CliState;
import com.dta.cli.Command;
import com.dta.dto.request.CreateDiaryEntryRequest;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.response.DiaryEntryResponse;
import com.dta.dto.response.ThoughtAnalysisResponse;
import com.dta.service.DiaryService;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class DiaryCommand implements Command {

    private final Scanner scanner;
    private final CliState state;
    private final DiaryService diaryService;

    public DiaryCommand(Scanner scanner, CliState state, DiaryService diaryService) {
        this.scanner = scanner;
        this.state = state;
        this.diaryService = diaryService;
    }

    @Override
    public void execute() {
        if (!state.isAuthenticated()) {
            System.out.println("Please login first.");
            return;
        }

        while (true) {
            CliPrompts.printHeader("Thought Diary");
            System.out.println("1. New Entry");
            System.out.println("2. View Entries");
            System.out.println("3. View Insights");
            System.out.println("4. Suggest Distortions");
            System.out.println("5. Delete Entry");
            System.out.println("0. Back");

            String choice = CliPrompts.prompt(scanner, "Select an option: ");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            try {
                switch (choice) {
                    case "1" -> createEntry();
                    case "2" -> viewEntries();
                    case "3" -> viewInsights();
                    case "4" -> suggestDistortions();
                    case "5" -> deleteEntry();
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Diary error: " + ex.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Thought Diary";
    }

    private void createEntry() {
        CreateDiaryEntryRequest request = new CreateDiaryEntryRequest();
        request.setUserId(state.getCurrentUserId());
        request.setAutomaticThought(CliPrompts.prompt(scanner, "Automatic thought: "));
        request.setEmotion(CliPrompts.prompt(scanner, "Emotion: "));
        request.setReflection(CliPrompts.promptMultiline(scanner, "Reflection"));

        DiaryEntryResponse response = diaryService.createDiaryEntry(request);
        System.out.println("Saved diary entry " + response.getId());
        System.out.println("Suggested distortions: " + response.getSuggestedDistortions());
    }

    private void viewEntries() {
        List<DiaryEntryResponse> entries = diaryService.getDiaryEntries(state.getCurrentUserId());
        if (entries.isEmpty()) {
            System.out.println("No diary entries found.");
            return;
        }

        for (DiaryEntryResponse entry : entries) {
            System.out.println("- " + entry.getId() + " | " + entry.getEmotion() + " | " + entry.getAutomaticThought());
        }
    }

    private void viewInsights() {
        List<DiaryEntryResponse> entries = diaryService.getDiaryEntries(state.getCurrentUserId());
        if (entries.isEmpty()) {
            System.out.println("No diary entries available to analyze.");
            return;
        }

        Map<String, Integer> distortionCounts = new HashMap<>();
        for (DiaryEntryResponse entry : entries) {
            for (String distortion : entry.getSuggestedDistortions()) {
                distortionCounts.merge(distortion, 1, Integer::sum);
            }
        }

        System.out.println("Diary Insights");
        System.out.println("- Total entries: " + entries.size());
        if (distortionCounts.isEmpty()) {
            System.out.println("- No repeated distortion pattern detected yet.");
            return;
        }

        Map.Entry<String, Integer> top = distortionCounts.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();
        System.out.println("- Most common pattern: " + top.getKey() + " (" + top.getValue() + ")");
    }

    private void suggestDistortions() {
        DistortionSuggestionRequest request = new DistortionSuggestionRequest();
        request.setText(CliPrompts.prompt(scanner, "Thought to analyze: "));
        ThoughtAnalysisResponse response = diaryService.suggestDistortions(request);
        System.out.println("Detected distortions: " + response.getDistortions());
        System.out.println("Reframing prompts: " + response.getReframingPrompts());
    }

    private void deleteEntry() {
        UUID entryId = CliPrompts.promptUuid(scanner, "Entry id: ");
        if (entryId == null) {
            System.out.println("Entry id is required.");
            return;
        }
        diaryService.deleteDiaryEntry(entryId);
        System.out.println("Deleted diary entry " + entryId);
    }
}
