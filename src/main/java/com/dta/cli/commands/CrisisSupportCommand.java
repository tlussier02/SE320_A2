package com.dta.cli.commands;

import com.dta.cli.CliPrompts;
import com.dta.cli.CliState;
import com.dta.cli.Command;
import com.dta.dto.request.DistortionSuggestionRequest;
import com.dta.dto.request.UpdateSafetyPlanRequest;
import com.dta.dto.response.CrisisResponse;
import com.dta.dto.response.SafetyPlanResponse;
import com.dta.service.CrisisService;
import java.util.Scanner;

public class CrisisSupportCommand implements Command {

    private final Scanner scanner;
    private final CliState state;
    private final CrisisService crisisService;

    public CrisisSupportCommand(Scanner scanner, CliState state, CrisisService crisisService) {
        this.scanner = scanner;
        this.state = state;
        this.crisisService = crisisService;
    }

    @Override
    public void execute() {
        if (!state.isAuthenticated()) {
            System.out.println("Please login first.");
            return;
        }

        while (true) {
            CliPrompts.printHeader("Crisis Support");
            System.out.println("1. Detect Crisis Risk");
            System.out.println("2. Emergency Resources");
            System.out.println("3. View Safety Plan");
            System.out.println("4. Update Safety Plan");
            System.out.println("0. Back");

            String choice = CliPrompts.prompt(scanner, "Select an option: ");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            try {
                switch (choice) {
                    case "1" -> detectCrisis();
                    case "2" -> emergencyResources();
                    case "3" -> viewSafetyPlan();
                    case "4" -> updateSafetyPlan();
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Crisis support error: " + ex.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Crisis Support";
    }

    private void detectCrisis() {
        DistortionSuggestionRequest request = new DistortionSuggestionRequest();
        request.setText(CliPrompts.prompt(scanner, "Describe how you are feeling: "));
        CrisisResponse response = crisisService.detectCrisis(request);
        System.out.println("Crisis: " + response.isCrisis());
        System.out.println("Risk level: " + response.getRiskLevel());
        System.out.println("Action: " + response.getAction());
        System.out.println("Reasoning: " + response.getReasoning());
        if (response.getKeywordsDetected() != null && !response.getKeywordsDetected().isEmpty()) {
            System.out.println("Detected keywords: " + response.getKeywordsDetected());
        }
    }

    private void emergencyResources() {
        CrisisResponse overview = crisisService.getCrisisOverview();
        System.out.println("Emergency Resources");
        System.out.println("- Call or text 988 if immediate support is needed.");
        System.out.println("- Contact a trusted person or therapist.");
        System.out.println("- Current crisis state: " + overview.getRiskLevel());
    }

    private void viewSafetyPlan() {
        SafetyPlanResponse response = crisisService.getSafetyPlan(state.getCurrentUserId());
        System.out.println("Current safety plan");
        System.out.println(response.getSafetyPlan());
    }

    private void updateSafetyPlan() {
        UpdateSafetyPlanRequest request = new UpdateSafetyPlanRequest();
        request.setUserId(state.getCurrentUserId());
        request.setSafetyPlan(CliPrompts.promptMultiline(scanner, "Enter the updated safety plan"));
        SafetyPlanResponse response = crisisService.updateSafetyPlan(request);
        System.out.println("Saved safety plan for user " + response.getUserId());
    }
}
