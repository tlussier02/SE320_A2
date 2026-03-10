package com.dta.cli;

import com.dta.cli.command.StartupBanner;

public class DtaCli {
    // TODO [Timmy]: Tie CLI startup output to application state and optional debug flags.
    public void run() {
        StartupBanner.print();
    }
}
