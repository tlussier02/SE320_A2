package com.dta.cli.commands;

import com.dta.cli.CliPrompts;
import com.dta.cli.CliState;
import com.dta.cli.Command;
import com.dta.dto.response.ProgressResponse;
import com.dta.service.ProgressService;
import java.util.Scanner;

public class ProgressCommand implements Command {

    private final Scanner scanner;
    private final CliState state;
    private final ProgressService progressService;

    public ProgressCommand(Scanner scanner, CliState state, ProgressService progressService) {
        this.scanner = scanner;
        this.state = state;
        this.progressService = progressService;
    }

    @Override
    public void execute() {
        if (!state.isAuthenticated()) {
            System.out.println("Please login first.");
            return;
        }

        while (true) {
            CliPrompts.printHeader("Progress Dashboard");
            System.out.println("1. Weekly Summary");
            System.out.println("2. Monthly Trends");
            System.out.println("3. Achievement Snapshot");
            System.out.println("0. Back");

            String choice = CliPrompts.prompt(scanner, "Select an option: ");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            switch (choice) {
                case "1" -> print(progressService.getWeeklyProgress(state.getCurrentUserId()));
                case "2" -> print(progressService.getMonthlyProgress(state.getCurrentUserId()));
                case "3" -> print(progressService.getBurnoutProgress(state.getCurrentUserId()));
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Progress Dashboard";
    }

    private void print(ProgressResponse response) {
        System.out.println("Timeframe: " + response.getTimeframe());
        System.out.println("Score: " + response.getScore());
        System.out.println("Completed sessions: " + response.getCompletedSessions());
        System.out.println("Diary entries: " + response.getDiaryEntries());
        System.out.println("Burnout trend: " + response.getBurnoutTrend());
        System.out.println("Highlights: " + response.getHighlights());
    }
}
