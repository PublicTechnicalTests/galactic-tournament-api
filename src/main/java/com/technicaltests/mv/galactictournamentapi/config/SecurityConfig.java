package com.technicaltests.mv.galactictournamentapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Security configuration for the Galactic Tournament API.
 *
 * Configures Spring Security to:
 * - Allow public access to Swagger/OpenAPI documentation
 * - Protect API endpoints with API Key validation via ApiKeyFilter
 * - Disable CSRF protection for development/testing
 *
 * @author Backend Team
 * @version 3.0
 * @since 2026
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    /**
     * Configures the security filter chain.
     *
     * - Swagger/Documentation: Public access (no authentication required)
     * - API endpoints (/v1/*): Protected by ApiKeyFilter (validates X-API-Key header)
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Add the ApiKeyFilter before BasicAuthenticationFilter
                .addFilterBefore(apiKeyFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        // Swagger/OpenAPI endpoints - PUBLIC ACCESS
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // All other endpoints - protected by ApiKeyFilter
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}

