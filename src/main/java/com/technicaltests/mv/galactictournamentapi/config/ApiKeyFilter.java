package com.technicaltests.mv.galactictournamentapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter to validate API Key for API endpoints.
 *
 * This filter intercepts requests to API endpoints (/v1/*), validating that a valid
 * API Key is provided in the X-API-Key header.
 *
 * Swagger UI and documentation endpoints are NOT protected by this filter - they are
 * publicly accessible. API Key configuration is done via Swagger UI's security section.
 *
 * Valid API Keys are configured in application.yaml under app.api-keys property.
 *
 * @author Backend Team
 * @version 2.0
 * @since 2026
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String ERROR_RESPONSE = "{\"error\": \"Invalid or missing API Key. Provide X-API-Key header. Valid keys: test-key-123, test-key-456, dev-api-key\"}";

    @Value("${app.api-keys:}")
    private String validApiKeys;

    /**
     * Checks if the request path requires API Key validation.
     * Only API endpoints (/v1/*) are protected.
     * Swagger UI and documentation are public.
     */
    private boolean isProtectedPath(String path) {
        return path.startsWith("/api/v1/") || path.startsWith("/v1/");
    }

    /**
     * Validates if the provided API key is valid
     */
    private boolean isValidApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty() || validApiKeys == null) {
            return false;
        }

        Set<String> validKeys = new HashSet<>(Arrays.asList(validApiKeys.split(",")));
        return validKeys.stream()
                .anyMatch(key -> key.trim().equals(apiKey.trim()));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // If it's a protected path (Swagger, docs), validate API Key
        if (isProtectedPath(path)) {
            String apiKey = request.getHeader(API_KEY_HEADER);

            if (!isValidApiKey(apiKey)) {
                log.warn("Access denied to {} - Invalid or missing API Key", path);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(ERROR_RESPONSE);
                return;
            }

            log.debug("Valid API Key provided for path: {}", path);
        }

        filterChain.doFilter(request, response);
    }
}

