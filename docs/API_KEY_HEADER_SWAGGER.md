# 🔐 API Key para Swagger UI - Guía de Implementación

## ¿Qué Cambió?

Se ha implementado validación de **API Key en header** para acceder a Swagger UI y la documentación OpenAPI.

---

## 📋 Configuración Actualizada

### Archivos Modificados
- **`SecurityConfig.java`** - Ahora usa el filtro personalizado
- **`ApiKeyFilter.java`** - Nuevo filtro para validar API Key

### Cómo Funciona

1. **Filtro personalizado (ApiKeyFilter)** intercepta todas las solicitudes
2. **Si la ruta es Swagger/docs**: Valida que el header `X-API-Key` sea válido
3. **Si la ruta es API**: Requiere autenticación básica HTTP
4. **Si no es válido**: Devuelve error 401 Unauthorized

---

## 🔑 API Keys Disponibles

Las API Keys están configuradas en `src/main/resources/application.yaml`:

```yaml
app:
  api-keys: test-key-123,test-key-456,dev-api-key
```

**Claves válidas disponibles**:
- ✅ `test-key-123`
- ✅ `test-key-456`
- ✅ `dev-api-key`

---

## 📍 Rutas y Protección

| Ruta | Protección | Header Requerido |
|------|-----------|------------------|
| `/api/swagger-ui.html` | 🔐 API Key | `X-API-Key: <clave>` |
| `/api/swagger-ui/**` | 🔐 API Key | `X-API-Key: <clave>` |
| `/api/v3/api-docs` | 🔐 API Key | `X-API-Key: <clave>` |
| `/api/api-docs/**` | 🔐 API Key | `X-API-Key: <clave>` |
| `/api/webjars/**` | 🔐 API Key | `X-API-Key: <clave>` |
| `/api/v1/species/**` | 🔐 Básica HTTP | `Authorization: Basic <encoded>` |
| `/api/v1/battles/**` | 🔐 Básica HTTP | `Authorization: Basic <encoded>` |
| `/api/v1/rankings/**` | 🔐 Básica HTTP | `Authorization: Basic <encoded>` |

---

## 🚀 Cómo Usar

### 1. Compilar el Proyecto
```bash
./mvnw clean compile
```

### 2. Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

### 3. Acceder a Swagger UI

#### Opción A: Con cURL (PowerShell)
```powershell
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html
```

#### Opción B: Con Postman
```
GET http://localhost:8080/api/swagger-ui.html
Headers:
  X-API-Key: test-key-123
```

#### Opción C: Con navegador (herramientas de desarrollo)
1. Abre la consola del navegador (F12)
2. Ejecuta en la consola:
```javascript
fetch('http://localhost:8080/api/swagger-ui.html', {
  headers: {
    'X-API-Key': 'test-key-123'
  }
}).then(r => r.text()).then(console.log)
```

#### Opción D: Script PowerShell Automatizado
```powershell
$apiKey = "test-key-123"
$uri = "http://localhost:8080/api/swagger-ui"

$headers = @{
    "X-API-Key" = $apiKey
}

$response = Invoke-WebRequest -Uri $uri -Headers $headers
Write-Host "Status Code:" $response.StatusCode
Write-Host "Content Length:" $response.Content.Length
```

---

## 🧪 Ejemplos de Testing

### Test 1: Swagger SIN API Key (Debe dar 401)
```bash
curl -w "\nStatus: %{http_code}\n" http://localhost:8080/api/swagger-ui.html
```
**Resultado esperado**: `401 Unauthorized`

### Test 2: Swagger CON API Key válida (Debe dar 200)
```bash
curl -w "\nStatus: %{http_code}\n" \
  -H "X-API-Key: test-key-123" \
  http://localhost:8080/api/swagger-ui.html
```
**Resultado esperado**: `200 OK`

### Test 3: Swagger CON API Key INVÁLIDA (Debe dar 401)
```bash
curl -w "\nStatus: %{http_code}\n" \
  -H "X-API-Key: invalid-key" \
  http://localhost:8080/api/swagger-ui.html
```
**Resultado esperado**: `401 Unauthorized`

### Test 4: OpenAPI Docs (Same API Key validation)
```bash
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v3/api-docs | jq . | head -30
```

---

## 🔍 Mensaje de Error

Si proporcionar una API Key inválida o faltante, recibirás:

```json
{
  "error": "Invalid or missing API Key. Provide X-API-Key header."
}
```

---

## 📊 Flujo de Validación

```
Solicitud HTTP
    ↓
¿Es ruta de Swagger/Docs?
    ├─ SÍ → ApiKeyFilter valida X-API-Key
    │       ├─ ¿API Key válida?
    │       │   ├─ SÍ → Permitir acceso
    │       │   └─ NO → 401 Unauthorized
    └─ NO → Pasar a SecurityConfig
            ├─ AuthorizeHttpRequests
            └─ Requiere HTTP Basic Auth (para APIs)
```

---

## 💡 Cambiar o Agregar API Keys

### Método 1: Variable de Entorno
```bash
set API_KEYS=mi-clave-1,mi-clave-2
./mvnw spring-boot:run
```

### Método 2: Editar application.yaml
```yaml
app:
  api-keys: mi-clave-1,mi-clave-2,mi-clave-3
```

### Método 3: Parámetro en línea de comandos
```bash
./mvnw spring-boot:run "-Dapp.api-keys=mi-clave-1,mi-clave-2"
```

---

## 🔐 Arquitectura de Seguridad Actualizada

```
┌─────────────────────────────────────────────────────┐
│               Cliente (Navegador/cURL)              │
└────────────────────┬────────────────────────────────┘
                     │ Solicitud con X-API-Key header
                     ↓
┌─────────────────────────────────────────────────────┐
│              ApiKeyFilter (OncePerRequestFilter)    │
├─────────────────────────────────────────────────────┤
│ 1. Chequea si es ruta protegida (/swagger-ui, etc) │
│ 2. Valida header X-API-Key                          │
│ 3. Compara con lista de keys válidas                │
│    ├─ Si es válida → Permite pasar                  │
│    └─ Si es inválida → 401 Unauthorized             │
└─────────────────────┬────────────────────────────────┘
                      │ Solicitud permitida
                      ↓
┌─────────────────────────────────────────────────────┐
│           SecurityFilterChain (Spring Security)     │
├─────────────────────────────────────────────────────┤
│ Swagger/Docs: permitAll (filtro ya validó)         │
│ APIs (/v1/*): requieren HTTP Basic Auth            │
└─────────────────────┬────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────┐
│              Dispatcher a Controllers                │
├─────────────────────────────────────────────────────┤
│ GET /swagger-ui.html → Interfaz de Swagger UI      │
│ GET /v3/api-docs → OpenAPI JSON                    │
│ GET /v1/species → Requiere HTTP Basic Auth         │
└─────────────────────────────────────────────────────┘
```

---

## 📝 Logs Esperados

Cuando inicia la aplicación, deberías ver:

```
[INFO] com.technicaltests.mv.galactictournamentapi.GalacticTournamentApiApplication : 
   Started GalacticTournamentApiApplication in X seconds

[INFO] org.springframework.security.web.FilterChainProxy : 
   Will secure any request with [SecurityContextPersistenceFilter, ...]

[DEBUG] com.technicaltests.mv.galactictournamentapi.config.ApiKeyFilter : 
   Filter initialized
```

Cuando accedes al Swagger:
```
[DEBUG] com.technicaltests.mv.galactictournamentapi.config.ApiKeyFilter : 
   Valid API Key provided for path: /api/swagger-ui.html
```

Cuando API Key es inválida:
```
[WARN] com.technicaltests.mv.galactictournamentapi.config.ApiKeyFilter : 
   Access denied to /api/swagger-ui.html - Invalid or missing API Key
```

---

## ✅ Checklist de Validación

- [x] ApiKeyFilter creado y compilado
- [x] SecurityConfig actualizado
- [x] Proyecto compila sin errores
- [ ] Ejecutar aplicación
- [ ] Probar Swagger SIN API Key (debe fallar)
- [ ] Probar Swagger CON API Key válida (debe funcionar)
- [ ] Probar Swagger CON API Key inválida (debe fallar)
- [ ] Probar OpenAPI Docs con API Key
- [ ] Probar APIs sin autenticación (debe fallar)

---

## 🎯 Resumen

| Endpoint | Antes | Ahora |
|----------|-------|-------|
| Swagger UI | ✅ Público | 🔐 X-API-Key requerido |
| OpenAPI Docs | ✅ Público | 🔐 X-API-Key requerido |
| API Endpoints | 🔐 HTTP Basic | 🔐 HTTP Basic (sin cambios) |

**API Key válida de prueba**: `test-key-123`

---

## 🚨 Solución de Problemas

### Error 401 al acceder a Swagger
**Causa**: API Key faltante o inválida  
**Solución**: Agregar header `X-API-Key: test-key-123`

### Error 400 en solicitudes
**Causa**: Formato de header incorrecto  
**Solución**: Verificar que no hay espacios extras: `X-API-Key: valor` (sin espacios)

### Compilación falla
**Causa**: Imports faltantes o problemas de resolución  
**Solución**: Ejecutar `./mvnw clean install`

---

## 📚 Archivos Relacionados

Ver también:
- `ANALISIS_SWAGGER_LIBRE.md` - Análisis inicial
- `ALTERNATIVAS_CONFIGURACION.md` - Otras opciones de seguridad
- `EJEMPLOS_TESTING.md` - Más ejemplos de testing

---

## 🎉 ¡Implementación Completada!

Tu Swagger UI ahora está protegido con API Key en header.

**Próximo acceso**:
```bash
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html
```

