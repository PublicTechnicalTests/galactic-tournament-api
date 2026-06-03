# 🚀 INSTRUCCIONES RÁPIDAS - Swagger Libre + Security

## CAMBIO PRINCIPAL
- ✅ **Swagger UI es PÚBLICO** (Acceso libre sin autenticación)
- 🔐 **Dentro de Swagger** hay un botón "Authorize" para la API Key
- 🔐 **Endpoints de API** protegidos (requieren API Key)

---

## PASOS PARA USAR

### 1. Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

### 2. Abrir Swagger UI
```
Navega a: http://localhost:8080/api/swagger-ui.html
✅ Se abre sin autenticación
```

### 3. Dentro de Swagger UI
```
1. Haz clic en el botón "Authorize" (arriba a la derecha)
2. Ingresa tu API Key: test-key-123
3. Haz clic en "Authorize"
4. ¡Listo! Ahora puedes probar los endpoints
```

### 4. Prueba los Endpoints
```
Todo los requests que hagas desde Swagger incluirán 
automáticamente el header: X-API-Key: test-key-123
```

---

## 🔑 API Keys Disponibles
```
• test-key-123
• test-key-456
• dev-api-key
```

---

## 📊 MATRIZ DE ACCESO

| Ruta | Acceso | Autenticación |
|------|--------|---------------|
| `GET /swagger-ui.html` | ✅ LIBRE | Ninguna |
| `GET /v3/api-docs` | ✅ LIBRE | Ninguna |
| `GET /v1/species` | 🔐 Dentro Swagger | API Key |
| `POST /v1/species` | 🔐 Dentro Swagger | API Key |
| `GET /v1/battles` | 🔐 Dentro Swagger | API Key |
| `POST /v1/battles` | 🔐 Dentro Swagger | API Key |

---

## 🎯 LO QUE VAS A VER

### Pantalla Principal de Swagger
```
┌──────────────────────────────────────┐
│ Galactic Tournament API              │
│ 1.0    [Authorize] [Explore] [...]   │ ← Botón Authorize aquí
├──────────────────────────────────────┤
│ Species                              │
│  POST /v1/species                    │
│  GET /v1/species                     │
│  GET /v1/species/{id}                │
│                                      │
│ Battles                              │
│  POST /v1/battles                    │
│  GET /v1/battles/{id}                │
│                                      │
│ Rankings                             │
│  GET /v1/rankings                    │
└──────────────────────────────────────┘
```

### Modal de Autorización (Al hacer clic)
```
┌─────────────────────────────┐
│ Available authorizations    │
├─────────────────────────────┤
│ api_key (apiKey)            │
│ X-API-Key: [______________] │
│                             │
│ [ Authorize ] [ Cancel ]    │
└─────────────────────────────┘
```

---

## 💻 VIA LINEA DE COMANDOS (Si prefieres)

### Ver Swagger UI (público)
```bash
curl http://localhost:8080/api/swagger-ui.html
```

### Ver OpenAPI Docs (público)
```bash
curl http://localhost:8080/api/v3/api-docs | jq .
```

### Llamar API con API Key
```bash
curl -H "X-API-Key: test-key-123" \
  http://localhost:8080/api/v1/species
```

### Listar Especies (ejemplo completo)
```bash
curl -X GET \
  -H "X-API-Key: test-key-123" \
  "http://localhost:8080/api/v1/species?page=0&size=20"
```

---

## 📁 ARCHIVOS ACTUALIZADOS

| Archivo | Cambio | Propósito |
|---------|--------|-----------|
| `ApiKeyFilter.java` | ✏️ Modificado | Ahora solo protege endpoints de API |
| `SecurityConfig.java` | ✏️ Modificado | Permite acceso libre a Swagger |
| `OpenApiConfig.java` | ✨ Nuevo | Agrega Security Scheme a Swagger UI |

---

## ✅ VERIFICACIÓN

### Test 1: Acceder a Swagger sin autenticación
```bash
curl -v http://localhost:8080/api/swagger-ui.html
→ Status: 200 (Accesible)
```

### Test 2: Acceder a API sin API Key
```bash
curl http://localhost:8080/api/v1/species
→ Status: 401 (Rechazado, falta API Key)
```

### Test 3: Acceder a API con API Key válida
```bash
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v1/species
→ Status: 200 (Funciona)
```

---

## 🎉 LISTO PARA USAR

**Estado**: ✅ TODO COMPLETADO

**Compilación**: ✅ EXITOSA

**Próximo paso**: Ejecuta `./mvnw spring-boot:run`

---

## 📖 DOCUMENTACIÓN COMPLETA

Ver archivo: `SWAGGER_LIBRE_CON_SECURITY.md`

---

¡Éxito! 🚀

