package com.dta.cli;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuHandler {
    private final Map<Integer, Command> commands = new LinkedHashMap<>();
    private final Scanner scanner;
    private boolean running = true;

    public MenuHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void registerCommand(int option, Command command) {
        commands.put(option, command);
    }

    public void displayMenu() {
        System.out.println("\n--- Digital Therapy Assistant ---");
        commands.forEach((key, cmd) -> System.out.println(key + ". " + cmd.getDescription()));
        System.out.print("Choose an option: ");
    }

    public boolean handleInput() {
        if (!scanner.hasNextLine()) {
            return false;
        }

        String rawChoice = scanner.nextLine().trim();
        if (rawChoice.isEmpty()) {
            System.out.println("Invalid input. Please enter a number.");
            return running;
        }

        try {
            int choice = Integer.parseInt(rawChoice);
            Command cmd = commands.get(choice);
            if (cmd != null) {
                cmd.execute();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input. Please enter a number.");
        }

        return running;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
    }
}
