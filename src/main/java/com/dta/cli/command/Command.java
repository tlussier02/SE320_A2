package com.dta.cli;

public interface Command {
    void execute();
    String getDescription();
}