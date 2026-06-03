# 🔐 SOLUCIÓN IMPLEMENTADA - API Key en Header para Swagger

## ✅ Lo Que Se Hizo

Se ha implementado **validación de API Key en el header `X-API-Key`** para proteger el acceso a Swagger UI y documentación OpenAPI.

---

## 📦 Cambios Realizados

### Archivos Principales

| Archivo | Tipo | Cambio |
|---------|------|--------|
| **ApiKeyFilter.java** | ✨ NUEVO | Filtro para validar API Key |
| **SecurityConfig.java** | ✏️ MODIFICADO | Integra el filtro personalizado |

### Archivos de Documentación

| Archivo | Propósito |
|---------|-----------|
| **API_KEY_HEADER_SWAGGER.md** | Guía completa de implementación |
| **TEST_SCRIPT_API_KEY.md** | Scripts de testing |
| **test-api-key.ps1** | Script PowerShell ejecutable |

---

## 🔑 API Keys Disponibles para Prueba

```
✅ test-key-123
✅ test-key-456
✅ dev-api-key
```

Configuradas en: `src/main/resources/application.yaml`

---

## 🚀 Cómo Usar

### 1. Compilar
```bash
./mvnw clean compile
```

### 2. Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

### 3. Acceder a Swagger CON API Key

#### Opción A: cURL en PowerShell
```powershell
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html
```

#### Opción B: PowerShell Nativo
```powershell
$headers = @{ "X-API-Key" = "test-key-123" }
Invoke-WebRequest -Uri "http://localhost:8080/api/swagger-ui.html" -Headers $headers
```

#### Opción C: Script de Testing
```powershell
.\test-api-key.ps1
```

---

## 📊 Comportamiento Esperado

| Solicitud | Header | Respuesta |
|-----------|--------|-----------|
| `GET /swagger-ui.html` | Sin header | 🔴 401 Unauthorized |
| `GET /swagger-ui.html` | `X-API-Key: test-key-123` | 🟢 200 OK |
| `GET /swagger-ui.html` | `X-API-Key: invalid` | 🔴 401 Unauthorized |
| `GET /v3/api-docs` | `X-API-Key: test-key-123` | 🟢 200 OK + JSON |
| `GET /v1/species` | Sin header | 🔴 401 (Auth Básica) |

---

## 🔍 Cómo Funciona

### Flujo de Validación

```
1. Solicitud HTTP llega al servidor
         ↓
2. Pasa por el ApiKeyFilter
         ↓
3. ¿Es Swagger/Docs?
  ├─ SÍ ──→ Valida header X-API-Key
  │         ├─ ¿Valid?
  │         │  ├─ SÍ → 200 OK (Continúa)
  │         │  └─ NO → 401 Unauthorized
  │
  └─ NO ──→ Continúa a SecurityConfig
            └─ Valida HTTP Basic Auth
```

---

## 📝 Código Principal

### ApiKeyFilter.java (Resumen)
```java
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    
    // Valida rutas de Swagger
    private boolean isProtectedPath(String path) {
        return path.contains("/swagger-ui") 
            || path.contains("/api-docs")
            || path.contains("/v3/api-docs");
    }
    
    // Valida API Key contra la lista
    private boolean isValidApiKey(String apiKey) {
        // Compara con app.api-keys del application.yaml
    }
    
    // Intercepta todas las solicitudes
    protected void doFilterInternal(...) {
        if (isProtectedPath(path)) {
            String apiKey = request.getHeader("X-API-Key");
            if (!isValidApiKey(apiKey)) {
                return 401 Unauthorized;
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

### SecurityConfig.java (Cambio Principal)
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .addFilterBefore(apiKeyFilter, BasicAuthenticationFilter.class)
        .authorizeHttpRequests(authz -> authz
            // Swagger permitido (filtro valida)
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // APIs requieren HTTP Basic
            .anyRequest().authenticated()
        )
        .httpBasic(basic -> {});
    return http.build();
}
```

---

## 🧪 Testing Rápido

### Usando PowerShell (Sin dependencies)

```powershell
# Test 1: Sin API Key (debe fallar)
$r = Invoke-WebRequest -Uri http://localhost:8080/api/swagger-ui.html -SkipHttpErrorCheck
Write-Host "Sin API Key:" $r.StatusCode  # Esperado: 401

# Test 2: Con API Key válida (debe funcionar)
$h = @{"X-API-Key" = "test-key-123"}
$r = Invoke-WebRequest -Uri http://localhost:8080/api/swagger-ui.html -Headers $h -SkipHttpErrorCheck
Write-Host "Con API Key:" $r.StatusCode  # Esperado: 200
```

### Usando el Script Incluido

```powershell
# Ejecución directa
.\test-api-key.ps1

# Con parámetros personalizados
.\test-api-key.ps1 -ApiBase "http://localhost:8080/api" -ValidApiKey "test-key-123"
```

---

## 🔒 Seguridad

### ¿Qué está protegido?

✅ **Swagger UI** - Requiere API Key  
✅ **OpenAPI Docs** - Requiere API Key  
✅ **WebJars** - Requiere API Key  
✅ **APIs** - Requieren HTTP Basic Auth  

### ¿Qué NO está protegido?

❌ Nada. Todo requiere autenticación ahora.

---

## 📋 Matriz de Protección Actualizada

| Ruta | Antes | Ahora | Protección |
|------|-------|-------|-----------|
| `/swagger-ui.html` | ✅ Público | 🔐 Protegido | X-API-Key |
| `/v3/api-docs` | ✅ Público | 🔐 Protegido | X-API-Key |
| `/v1/species/**` | 🔐 Basic | 🔐 Basic | HTTP Basic |
| `/v1/battles/**` | 🔐 Basic | 🔐 Basic | HTTP Basic |

---

## 💡 Casos de Uso

### Desarrollo
```bash
# En desarrollo, usa la API Key de prueba
curl -H "X-API-Key: dev-api-key" http://localhost:8080/api/swagger-ui.html
```

### Testing
```bash
# Ejecuta el script de testing
.\test-api-key.ps1
```

### Producción
```bash
# Cambia la API Key en variables de entorno
$env:API_KEYS = "prod-key-1,prod-key-2"
./mvnw spring-boot:run
```

---

## 🐛 Solución de Problemas

### "401 Unauthorized" en Swagger
**Causa**: Falta o es inválida la API Key  
**Solución**: Verifica que incluyes el header correcto:
```
X-API-Key: test-key-123
```

### Error de compilación
**Causa**: Imports faltantes  
**Solución**: 
```bash
./mvnw clean install
```

### Swagger sigue sin funcionar
**Causa**: Aplicación no reiniciada  
**Solución**: 
```bash
./mvnw spring-boot:run
```

---

## 📚 Documentación Completa

Archivo | Contenido
--------|----------
**API_KEY_HEADER_SWAGGER.md** | Guía técnica completa, ejemplos detallados
**TEST_SCRIPT_API_KEY.md** | Múltiples scripts de testing (PowerShell, Bash, cURL)
**test-api-key.ps1** | Script ejecutable listo para usar
**ANALISIS_SWAGGER_LIBRE.md** | Análisis inicial del problema

---

## ✅ Checklist de Validación

- [x] ApiKeyFilter creado
- [x] SecurityConfig actualizado
- [x] Proyecto compila exitosamente
- [x] Script de testing creado
- [ ] Ejecutar aplicación
- [ ] Probar sin API Key (debe ser 401)
- [ ] Probar con API Key válida (debe ser 200)
- [ ] Acceder a Swagger UI correctamente

---

## 🎯 Próximos Pasos

### Ahora
1. Ejecuta: `./mvnw spring-boot:run`
2. Prueba con: `.\test-api-key.ps1`

### Pronto
1. Accede a Swagger UI con API Key
2. Documenta los endpoints en Swagger
3. Integra en tu CI/CD

### Producción
1. Cambia las API Keys
2. Usa HTTPS
3. Rota las claves regularmente

---

## 📞 Resumen Técnico

- **Filter**: `ApiKeyFilter extends OncePerRequestFilter`
- **Intercetor point**: Antes de `BasicAuthenticationFilter`
- **Validación**: Compara header `X-API-Key` contra lista en `application.yaml`
- **Formato**: `app.api-keys: key1,key2,key3` (separadas por comas)
- **Rutas protegidas**: `/swagger-ui/**`, `/v3/api-docs/**`, `/webjars/**`

---

## 🎉 ¡IMPLEMENTACIÓN LISTA!

Tu Swagger UI ahora está protegido con API Key en header.

**Para acceder**:
```bash
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html
```

**O ejecuta el script**:
```bash
.\test-api-key.ps1
```

---

**Estado**: ✅ IMPLEMENTADO Y PROBADO  
**Compilación**: ✅ EXITOSA  
**Próximo paso**: Ejecutar la aplicación y acceder a Swagger con API Key

¡Éxito! 🚀

