package com.unibuc.book_app.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Book app API",
                version = "0.0.1",
                description = "API for managing books"
        )
)

@Configuration
public class OpenApiConfig {
}