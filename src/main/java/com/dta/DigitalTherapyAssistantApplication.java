package com.dta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalTherapyAssistantApplication {
    // TODO [Josh]: Keep this as the Spring Boot bootstrap entry and align startup sequence for CLI/API/autowired services.
    public static void main(String[] args) {
        SpringApplication.run(DigitalTherapyAssistantApplication.class, args);
    }
}
