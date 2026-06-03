# Alternativas de Configuración de Seguridad

Este archivo contiene diferentes configuraciones que puedes usar según tus necesidades.

---

## Opción 1: Solo Swagger Público (RECOMENDADO - Ya Implementado)

**Archivo**: `SecurityConfig.java`

**Características**:
- ✅ Swagger UI completamente público
- ✅ Todos los endpoints de API protegidos
- ✅ Requiere autenticación básica para APIs

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                    // Swagger - público
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/webjars/**"
                    ).permitAll()
                    // APIs - protegidas
                    .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

    return http.build();
}
```

---

## Opción 2: Swagger + APIs Públicas (SIN AUTENTICACIÓN)

**Cuándo usarla**: Desarrollo local, APIs públicas sin datos sensibles

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/webjars/**",
                        "/v1/species",
                        "/v1/species/**",
                        "/v1/battles/**",
                        "/v1/rankings/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

    return http.build();
}
```

---

## Opción 3: Swagger Público + APIs Protegidas por API Key

**Cuándo usarla**: Producción con acceso controlado por API Key

Primero, agrega este filtro personalizado:

```java
package com.technicaltests.mv.galactictournamentapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.api-keys}")
    private String validApiKeys;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Permitir Swagger sin validación
        if (path.contains("/swagger-ui") || path.contains("/api-docs") || path.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Validar API Key para otros endpoints
        String apiKey = request.getHeader("X-API-Key");
        String[] validKeys = validApiKeys.split(",");
        
        boolean isValidKey = false;
        for (String key : validKeys) {
            if (key.trim().equals(apiKey)) {
                isValidKey = true;
                break;
            }
        }
        
        if (!isValidKey) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid or missing API Key\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
```

Luego actualiza SecurityConfig:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/webjars/**"
                    ).permitAll()
                    .anyRequest().permitAll() // El filtro se encarga de validar
            )
            .httpBasic(basicAuth -> basicAuth.disable());

    return http.build();
}
```

---

## Opción 4: Swagger Público + APIs con JWT (MÁS SEGURO)

**Cuándo usarla**: Producción con autenticación segura

Agrega dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

Crea un filtro JWT:

```java
package com.technicaltests.mv.galactictournamentapi.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${app.jwt.secret:tu-clave-secreta-muy-larga-aqui}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Permitir Swagger sin autenticación
        if (path.contains("/swagger-ui") || path.contains("/api-docs") || path.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Validar JWT para otros endpoints
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }
        
        String token = bearerToken.substring(7);
        
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
        }
    }
}
```

---

## Tabla Comparativa

| Opción | Swagger | APIs | Ideal Para | Seguridad |
|--------|---------|------|-----------|-----------|
| 1️⃣ Actual | ✅ Público | 🔐 Básica | Desarrollo | Media |
| 2️⃣ Todo Público | ✅ Público | ✅ Público | Testing | Baja |
| 3️⃣ API Key | ✅ Público | 🔑 API Key | Producción Simple | Media-Alta |
| 4️⃣ JWT | ✅ Público | 🔐 JWT | Producción | Alta |

---

## Cómo Cambiar de Configuración

1. **Realiza cambios en `SecurityConfig.java`**
2. **Ejecuta**: `mvn clean compile`
3. **Reinicia la aplicación**: `mvn spring-boot:run`
4. **Prueba la URL**: `http://localhost:8080/api/swagger-ui.html`

---

## Configuración Recomendada por Ambiente

### 🔧 Desarrollo
- Opción 2: Todas las rutas públicas
- Fácil testing sin autenticación

### 🧪 Testing/QA
- Opción 1: Solo Swagger público (ACTUAL)
- Simula seguridad real

### 🚀 Producción
- Opción 3 o 4: API Key o JWT
- Máxima seguridad
- Considéra usar HTTPS

---

## Testing de Endpoints Protegidos

### Con Autenticación Básica:
```bash
curl -u usuario:contraseña http://localhost:8080/api/v1/species
```

### Con API Key:
```bash
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v1/species
```

### Con JWT:
```bash
curl -H "Authorization: Bearer tu-token-jwt" http://localhost:8080/api/v1/species
```

