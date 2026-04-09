package com.dta.cli;

import java.io.Console;
import java.util.Scanner;
import java.util.UUID;

public final class CliPrompts {

    private CliPrompts() {
    }

    public static String prompt(Scanner scanner, String label) {
        System.out.print(label);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public static String promptMultiline(Scanner scanner, String label) {
        System.out.println(label);
        System.out.println("Finish by entering a single line with END");
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("END".equalsIgnoreCase(line.trim())) {
                break;
            }
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(line);
        }
        return builder.toString().trim();
    }

    public static String promptPassword(Scanner scanner, String label) {
        Console console = System.console();
        if (console != null) {
            char[] password = console.readPassword("%s", label);
            return password == null ? null : new String(password);
        }
        return prompt(scanner, label);
    }

    public static UUID promptUuid(Scanner scanner, String label) {
        String value = prompt(scanner, label);
        if (value == null || value.isBlank()) {
            return null;
        }
        return UUID.fromString(value);
    }

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("== " + title + " ==");
    }
}
