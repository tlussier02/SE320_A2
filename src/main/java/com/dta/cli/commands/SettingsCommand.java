package com.dta.cli.commands;

import com.dta.cli.CliState;
import com.dta.cli.Command;

public class SettingsCommand implements Command {

    private final CliState state;

    public SettingsCommand(CliState state) {
        this.state = state;
    }

    @Override
    public void execute() {
        System.out.println("Application URLs");
        System.out.println("- Swagger UI: http://localhost:8080/swagger-ui/index.html");
        System.out.println("- H2 Console: http://localhost:8080/h2-console/login.jsp");
        System.out.println("- API Docs: http://localhost:8080/v3/api-docs");
        if (state.isAuthenticated()) {
            System.out.println("Current user: " + state.getCurrentUserEmail());
        } else {
            System.out.println("Current user: not logged in");
        }
    }

    @Override
    public String getDescription() {
        return "Settings";
    }
}
