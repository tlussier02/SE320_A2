package com.dta.cli.commands;

import com.dta.cli.Command;
import com.dta.cli.MenuHandler;

public class ExitCommand implements Command {

    private final MenuHandler menuHandler;

    public ExitCommand(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
    }

    @Override
    public void execute() {
        System.out.println("Exiting DTA CLI.");
        menuHandler.stop();
    }

    @Override
    public String getDescription() {
        return "Exit";
    }
}
