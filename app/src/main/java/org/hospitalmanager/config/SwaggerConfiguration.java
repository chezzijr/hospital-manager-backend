package org.hospitalmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Hospital Manager API")
                .description("Backend API for authentication, patient management, and more.")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                .description("Hospital Manager Documentation")
                .url("https://github.com/chezzijr/hospital-manager-backend/blob/main/README.md"));
    }
}
