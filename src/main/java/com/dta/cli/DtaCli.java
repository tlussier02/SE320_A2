package com.dta.cli;

import com.dta.cli.command.StartupBanner;
import java.util.Scanner;

public class DtaCli {
    public void run() {
        StartupBanner.print();
        Scanner scanner = new Scanner(System.in);
        MenuHandler menuHandler = new MenuHandler(scanner);
        
        while (scanner.hasNext()) {
            menuHandler.displayMenu();
            menuHandler.handleInput();
        }
    }
}