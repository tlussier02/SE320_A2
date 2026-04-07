package com.dta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    // TODO [Josh]: Centralize environment and bean-level app properties (CORS, profiles, feature toggles).
    @Bean
    public String environmentProfile() {
        return "dev";
    }
}
