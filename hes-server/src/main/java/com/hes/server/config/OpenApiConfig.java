package com.hes.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("HES Agent Platform API")
                        .description("Backend APIs for home energy storage Agents: registry, telemetry, command & ops")
                        .version("0.1.0")
                        .contact(new Contact().name("HES Platform").email("dev@example.com")));
    }
}
