package com.dta;

import com.dta.cli.DtaCli;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DigitalTherapyAssistantApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(DigitalTherapyAssistantApplication.class, args);
        startCliIfEnabled(context);
    }

    private static void startCliIfEnabled(ConfigurableApplicationContext context) {
        boolean cliEnabled = context.getEnvironment().getProperty("app.cli.enabled", Boolean.class, true);
        if (!cliEnabled) {
            return;
        }

        Thread cliThread = new Thread(() -> {
            try {
                context.getBean(DtaCli.class).run();
            } catch (Exception ex) {
                System.err.println("DTA CLI stopped: " + ex.getMessage());
            }
        }, "dta-cli");
        cliThread.setDaemon(true);
        cliThread.start();
    }
}
