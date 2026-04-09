package com.dta.cli.commands;

import com.dta.cli.CliPrompts;
import com.dta.cli.CliState;
import com.dta.cli.Command;
import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.response.AuthResponse;
import com.dta.service.AuthService;
import java.util.Scanner;

public class AuthenticationCommand implements Command {

    private final Scanner scanner;
    private final CliState state;
    private final AuthService authService;

    public AuthenticationCommand(Scanner scanner, CliState state, AuthService authService) {
        this.scanner = scanner;
        this.state = state;
        this.authService = authService;
    }

    @Override
    public void execute() {
        while (true) {
            CliPrompts.printHeader("Authentication");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("0. Back");

            String choice = CliPrompts.prompt(scanner, "Select an option: ");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            try {
                switch (choice) {
                    case "1" -> register();
                    case "2" -> login();
                    case "3" -> logout();
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Authentication error: " + ex.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return "Authentication";
    }

    private void register() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName(CliPrompts.prompt(scanner, "Full name: "));
        request.setEmail(CliPrompts.prompt(scanner, "Email: "));
        request.setPassword(CliPrompts.promptPassword(scanner, "Password: "));

        AuthResponse response = authService.register(request);
        state.setCurrentUserId(response.getUserId());
        state.setCurrentUserEmail(response.getEmail());
        state.setCurrentRefreshToken(response.getRefreshToken());
        System.out.println("Registered and logged in as " + response.getEmail());
    }

    private void login() {
        LoginRequest request = new LoginRequest();
        request.setEmail(CliPrompts.prompt(scanner, "Email: "));
        request.setPassword(CliPrompts.promptPassword(scanner, "Password: "));

        AuthResponse response = authService.login(request);
        state.setCurrentUserId(response.getUserId());
        state.setCurrentUserEmail(response.getEmail());
        state.setCurrentRefreshToken(response.getRefreshToken());
        System.out.println("Logged in as " + response.getEmail());
    }

    private void logout() {
        if (state.getCurrentRefreshToken() == null || state.getCurrentRefreshToken().isBlank()) {
            System.out.println("No active login session was found.");
            state.clearAuthentication();
            return;
        }

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken(state.getCurrentRefreshToken());
        authService.logout(request);
        state.clearAuthentication();
        System.out.println("Logged out.");
    }
}
