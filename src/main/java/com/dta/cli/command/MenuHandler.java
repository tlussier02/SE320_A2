package com.dta.cli;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuHandler {
    private final Map<Integer, Command> commands = new LinkedHashMap<>();
    private final Scanner scanner;

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

    public void handleInput() {
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            
            Command cmd = commands.get(choice);
            if (cmd != null) {
                cmd.execute();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } else {
            System.out.println("Invalid input. Please enter a number.");
            if (scanner.hasNext()) {
                scanner.next(); // Clear the bad input
            }
        }
    }
}