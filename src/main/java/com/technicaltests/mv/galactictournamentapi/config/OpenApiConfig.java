package com.technicaltests.mv.galactictournamentapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Swagger UI documentation.
 *
 * Configures OpenAPI 3.0 to display an API Key security scheme in Swagger UI,
 * allowing users to enter their API Key for testing protected endpoints.
 *
 * Security scheme:
 * - Name: X-API-Key
 * - Type: API Key
 * - In: Header
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI specification with security requirements.
     *
     * This bean:
     * 1. Defines an API Key security scheme (X-API-Key header)
     * 2. Adds a Security section to Swagger UI
     * 3. Applies security to all endpoints
     *
     * Users can enter their API Key in the Swagger UI "Authorize" button.
     *
     * @return OpenAPI specification with security configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Galactic Tournament API")
                        .version("1.0")
                        .description("Tournament management microservice for Galactic competitions"))
                .addSecurityItem(new SecurityRequirement().addList("api_key"))
                .components(new Components()
                        .addSecuritySchemes("api_key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-API-Key")
                                        .description("API Key for authentication. " +
                                                "Valid keys: test-key-123, test-key-456, dev-api-key")));
    }
}

