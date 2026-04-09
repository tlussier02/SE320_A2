package com.dta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI digitalTherapyAssistantOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Therapy Assistant API")
                        .version("0.1.0")
                        .description("REST API for burnout support, CBT sessions, diary entries, progress tracking, and crisis workflows.")
                        .license(new License().name("Academic Use")));
    }
}
