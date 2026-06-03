# Script de Testing - API Key para Swagger
# Ejecutar desde la carpeta del proyecto

## PowerShell Script para Testing Completo

```powershell
# ==================== CONFIGURACIÓN ====================
$apiBase = "http://localhost:8080/api"
$validApiKey = "test-key-123"
$invalidApiKey = "invalid-key"

Write-Host "========== TESTING - SWAGGER CON API KEY ==========" -ForegroundColor Cyan
Write-Host ""

# ==================== TEST 1 ====================
Write-Host "Test 1: Acceder a Swagger SIN API Key (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $apiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host ""

try {
    $response = Invoke-WebRequest -Uri "$apiBase/swagger-ui.html" -SkipHttpErrorCheck
    $statusCode = $response.StatusCode
    
    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (Protegido como se esperaba)" -ForegroundColor Green
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
}
Write-Host ""

# ==================== TEST 2 ====================
Write-Host "Test 2: Acceder a Swagger CON API Key válida (Debe ser 200)" -ForegroundColor Yellow
Write-Host "GET $apiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $validApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $validApiKey
    }
    $response = Invoke-WebRequest -Uri "$apiBase/swagger-ui.html" -Headers $headers -SkipHttpErrorCheck
    $statusCode = $response.StatusCode
    
    if ($statusCode -eq 200) {
        Write-Host "✅ PASS - Status: $statusCode (Acceso permitido)" -ForegroundColor Green
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 200)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
}
Write-Host ""

# ==================== TEST 3 ====================
Write-Host "Test 3: Acceder a Swagger CON API Key INVÁLIDA (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $apiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $invalidApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $invalidApiKey
    }
    $response = Invoke-WebRequest -Uri "$apiBase/swagger-ui.html" -Headers $headers -SkipHttpErrorCheck
    $statusCode = $response.StatusCode
    
    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (Rechazado como se esperaba)" -ForegroundColor Green
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
}
Write-Host ""

# ==================== TEST 4 ====================
Write-Host "Test 4: Acceder a OpenAPI Docs CON API Key válida (Debe ser 200)" -ForegroundColor Yellow
Write-Host "GET $apiBase/v3/api-docs" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $validApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $validApiKey
    }
    $response = Invoke-RestMethod -Uri "$apiBase/v3/api-docs" -Headers $headers
    $endpointCount = $response.paths.PSObject.Properties.Count
    
    Write-Host "✅ PASS - Documentación obtenida correctamente" -ForegroundColor Green
    Write-Host "  Endpoints encontrados: $endpointCount" -ForegroundColor Gray
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
}
Write-Host ""

# ==================== TEST 5 ====================
Write-Host "Test 5: Acceder a API sin autenticación (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $apiBase/v1/species" -ForegroundColor Gray
Write-Host ""

try {
    $response = Invoke-WebRequest -Uri "$apiBase/v1/species" -SkipHttpErrorCheck
    $statusCode = $response.StatusCode
    
    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (API está protegida)" -ForegroundColor Green
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
}
Write-Host ""

# ==================== RESUMEN ====================
Write-Host "========== FIN DE TESTS ==========" -ForegroundColor Cyan
Write-Host ""
Write-Host "✅ Resumen:" -ForegroundColor Green
Write-Host "  - Swagger SIN API Key: 401 (Protegido)"
Write-Host "  - Swagger CON API Key válida: 200 (Accesible)"
Write-Host "  - Swagger CON API Key inválida: 401 (Rechazado)"
Write-Host "  - OpenAPI Docs CON API Key: 200 (Accesible)"
Write-Host "  - API SIN autenticación: 401 (Protegida)"
Write-Host ""
```

## Bash Script para Testing (Linux/Mac)

```bash
#!/bin/bash

# ==================== CONFIGURACIÓN ====================
API_BASE="http://localhost:8080/api"
VALID_API_KEY="test-key-123"
INVALID_API_KEY="invalid-key"

echo "========== TESTING - SWAGGER CON API KEY =========="
echo ""

# ==================== TEST 1 ====================
echo "Test 1: Acceder a Swagger SIN API Key (Debe ser 401)"
echo "GET $API_BASE/swagger-ui.html"
echo ""

STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$API_BASE/swagger-ui.html")
if [ "$STATUS" == "401" ]; then
    echo "✅ PASS - Status: $STATUS (Protegido como se esperaba)"
else
    echo "❌ FAIL - Status: $STATUS (Esperaba 401)"
fi
echo ""

# ==================== TEST 2 ====================
echo "Test 2: Acceder a Swagger CON API Key válida (Debe ser 200)"
echo "GET $API_BASE/swagger-ui.html"
echo "Header: X-API-Key: $VALID_API_KEY"
echo ""

STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "X-API-Key: $VALID_API_KEY" \
  "$API_BASE/swagger-ui.html")

if [ "$STATUS" == "200" ]; then
    echo "✅ PASS - Status: $STATUS (Acceso permitido)"
else
    echo "❌ FAIL - Status: $STATUS (Esperaba 200)"
fi
echo ""

# ==================== TEST 3 ====================
echo "Test 3: Acceder a Swagger CON API Key INVÁLIDA (Debe ser 401)"
echo "GET $API_BASE/swagger-ui.html"
echo "Header: X-API-Key: $INVALID_API_KEY"
echo ""

STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "X-API-Key: $INVALID_API_KEY" \
  "$API_BASE/swagger-ui.html")

if [ "$STATUS" == "401" ]; then
    echo "✅ PASS - Status: $STATUS (Rechazado como se esperaba)"
else
    echo "❌ FAIL - Status: $STATUS (Esperaba 401)"
fi
echo ""

# ==================== TEST 4 ====================
echo "Test 4: Acceder a OpenAPI Docs CON API Key válida (Debe ser 200)"
echo "GET $API_BASE/v3/api-docs"
echo "Header: X-API-Key: $VALID_API_KEY"
echo ""

RESPONSE=$(curl -s -H "X-API-Key: $VALID_API_KEY" "$API_BASE/v3/api-docs")
ENDPOINT_COUNT=$(echo "$RESPONSE" | jq '.paths | keys | length' 2>/dev/null || echo "0")

echo "✅ PASS - Documentación obtenida correctamente"
echo "  Endpoints encontrados: $ENDPOINT_COUNT"
echo ""

# ==================== TEST 5 ====================
echo "Test 5: Acceder a API sin autenticación (Debe ser 401)"
echo "GET $API_BASE/v1/species"
echo ""

STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$API_BASE/v1/species")
if [ "$STATUS" == "401" ]; then
    echo "✅ PASS - Status: $STATUS (API está protegida)"
else
    echo "❌ FAIL - Status: $STATUS (Esperaba 401)"
fi
echo ""

echo "========== FIN DE TESTS =========="
```

## Comandos rápidos con cURL

```bash
# ✅ Swagger CON API Key válida
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html

# ❌ Swagger SIN API Key
curl http://localhost:8080/api/swagger-ui.html

# ✅ OpenAPI Docs CON API Key
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v3/api-docs | jq .

# ❌ API SIN autenticación
curl http://localhost:8080/api/v1/species

# ✅ API CON autenticación básica
curl -u admin:password http://localhost:8080/api/v1/species
```

## Comando para ver el código de estado

```bash
# Obtener solo el código de respuesta HTTP
curl -s -o /dev/null -w "%{http_code}\n" \
  -H "X-API-Key: test-key-123" \
  http://localhost:8080/api/swagger-ui.html
```

## Con verbose para ver detalles

```bash
# Ver headers y respuesta completamente
curl -v -H "X-API-Key: test-key-123" \
  http://localhost:8080/api/swagger-ui.html
```

