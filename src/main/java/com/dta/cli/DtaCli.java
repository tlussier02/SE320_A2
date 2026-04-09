package com.dta.cli;

import com.dta.cli.command.StartupBanner;
import com.dta.cli.commands.AuthenticationCommand;
import com.dta.cli.commands.CrisisSupportCommand;
import com.dta.cli.commands.DiaryCommand;
import com.dta.cli.commands.ExitCommand;
import com.dta.cli.commands.ProgressCommand;
import com.dta.cli.commands.SessionWorkflowCommand;
import com.dta.cli.commands.SettingsCommand;
import com.dta.service.AuthService;
import com.dta.service.CrisisService;
import com.dta.service.DiaryService;
import com.dta.service.ProgressService;
import com.dta.service.SessionService;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class DtaCli {
    private final AuthService authService;
    private final SessionService sessionService;
    private final DiaryService diaryService;
    private final ProgressService progressService;
    private final CrisisService crisisService;

    public DtaCli(
            AuthService authService,
            SessionService sessionService,
            DiaryService diaryService,
            ProgressService progressService,
            CrisisService crisisService) {
        this.authService = authService;
        this.sessionService = sessionService;
        this.diaryService = diaryService;
        this.progressService = progressService;
        this.crisisService = crisisService;
    }

    public void run() {
        StartupBanner.print();
        Scanner scanner = new Scanner(System.in);
        CliState state = new CliState();
        MenuHandler menuHandler = new MenuHandler(scanner);
        registerCommands(menuHandler, scanner, state);

        while (menuHandler.isRunning()) {
            menuHandler.displayMenu();
            if (!menuHandler.handleInput()) {
                break;
            }
        }
    }

    private void registerCommands(MenuHandler menuHandler, Scanner scanner, CliState state) {
        menuHandler.registerCommand(1, new AuthenticationCommand(scanner, state, authService));
        menuHandler.registerCommand(2, new SessionWorkflowCommand(scanner, state, sessionService));
        menuHandler.registerCommand(3, new DiaryCommand(scanner, state, diaryService));
        menuHandler.registerCommand(4, new ProgressCommand(scanner, state, progressService));
        menuHandler.registerCommand(5, new CrisisSupportCommand(scanner, state, crisisService));
        menuHandler.registerCommand(6, new SettingsCommand(state));
        menuHandler.registerCommand(7, new ExitCommand(menuHandler));
    }
}
