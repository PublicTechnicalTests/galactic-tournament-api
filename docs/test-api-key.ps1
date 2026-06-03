#!/usr/bin/env powershell
# Script de Testing - Swagger UI con API Key
# Ejecutar desde PowerShell: .\test-api-key.ps1

param(
    [string]$ApiBase = "http://localhost:8080/api",
    [string]$ValidApiKey = "test-key-123",
    [string]$InvalidApiKey = "invalid-key"
)

Clear-Host
Write-Host "╔══════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║    TESTING - SWAGGER UI CON API KEY (HEADER)        ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$results = @()

# ==================== TEST 1 ====================
Write-Host "Test 1: Acceder a Swagger SIN API Key (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $ApiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host ""

try {
    $response = Invoke-WebRequest -Uri "$ApiBase/swagger-ui.html" -SkipHttpErrorCheck -ErrorAction Stop
    $statusCode = $response.StatusCode

    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (Protegido correctamente)" -ForegroundColor Green
        $results += @{ Test = "Swagger SIN API Key"; Status = "PASS"; Code = $statusCode }
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
        $results += @{ Test = "Swagger SIN API Key"; Status = "FAIL"; Code = $statusCode }
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
    $results += @{ Test = "Swagger SIN API Key"; Status = "ERROR"; Code = "N/A" }
}
Write-Host ""
Start-Sleep -Milliseconds 500

# ==================== TEST 2 ====================
Write-Host "Test 2: Acceder a Swagger CON API Key válida (Debe ser 200)" -ForegroundColor Yellow
Write-Host "GET $ApiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $ValidApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $ValidApiKey
    }
    $response = Invoke-WebRequest -Uri "$ApiBase/swagger-ui.html" -Headers $headers -SkipHttpErrorCheck -ErrorAction Stop
    $statusCode = $response.StatusCode

    if ($statusCode -eq 200) {
        Write-Host "✅ PASS - Status: $statusCode (Acceso concedido)" -ForegroundColor Green
        Write-Host "  Tamaño del contenido: $($response.Content.Length) bytes" -ForegroundColor Gray
        $results += @{ Test = "Swagger CON API Key válida"; Status = "PASS"; Code = $statusCode }
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 200)" -ForegroundColor Red
        $results += @{ Test = "Swagger CON API Key válida"; Status = "FAIL"; Code = $statusCode }
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
    $results += @{ Test = "Swagger CON API Key válida"; Status = "ERROR"; Code = "N/A" }
}
Write-Host ""
Start-Sleep -Milliseconds 500

# ==================== TEST 3 ====================
Write-Host "Test 3: Acceder a Swagger CON API Key INVÁLIDA (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $ApiBase/swagger-ui.html" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $InvalidApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $InvalidApiKey
    }
    $response = Invoke-WebRequest -Uri "$ApiBase/swagger-ui.html" -Headers $headers -SkipHttpErrorCheck -ErrorAction Stop
    $statusCode = $response.StatusCode

    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (Rechazado correctamente)" -ForegroundColor Green
        $results += @{ Test = "Swagger CON API Key inválida"; Status = "PASS"; Code = $statusCode }
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
        $results += @{ Test = "Swagger CON API Key inválida"; Status = "FAIL"; Code = $statusCode }
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
    $results += @{ Test = "Swagger CON API Key inválida"; Status = "ERROR"; Code = "N/A" }
}
Write-Host ""
Start-Sleep -Milliseconds 500

# ==================== TEST 4 ====================
Write-Host "Test 4: Acceder a OpenAPI Docs CON API Key válida (Debe ser 200)" -ForegroundColor Yellow
Write-Host "GET $ApiBase/v3/api-docs" -ForegroundColor Gray
Write-Host "Header: X-API-Key: $ValidApiKey" -ForegroundColor Gray
Write-Host ""

try {
    $headers = @{
        "X-API-Key" = $ValidApiKey
    }
    $response = Invoke-RestMethod -Uri "$ApiBase/v3/api-docs" -Headers $headers -ErrorAction Stop

    if ($response -and $response.PSObject.Properties) {
        $endpointCount = if ($response.paths) { $response.paths.PSObject.Properties.Count } else { 0 }
        Write-Host "✅ PASS - Documentación OpenAPI obtenida correctamente" -ForegroundColor Green
        Write-Host "  Total de endpoints: $endpointCount" -ForegroundColor Gray
        Write-Host "  Versión OpenAPI: $($response.openapi)" -ForegroundColor Gray
        $results += @{ Test = "OpenAPI Docs CON API Key"; Status = "PASS"; Code = 200 }
    } else {
        Write-Host "❌ FAIL - Respuesta inválida" -ForegroundColor Red
        $results += @{ Test = "OpenAPI Docs CON API Key"; Status = "FAIL"; Code = "N/A" }
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
    $results += @{ Test = "OpenAPI Docs CON API Key"; Status = "ERROR"; Code = "N/A" }
}
Write-Host ""
Start-Sleep -Milliseconds 500

# ==================== TEST 5 ====================
Write-Host "Test 5: Acceder a API SIN autenticación (Debe ser 401)" -ForegroundColor Yellow
Write-Host "GET $ApiBase/v1/species" -ForegroundColor Gray
Write-Host ""

try {
    $response = Invoke-WebRequest -Uri "$ApiBase/v1/species" -SkipHttpErrorCheck -ErrorAction Stop
    $statusCode = $response.StatusCode

    if ($statusCode -eq 401) {
        Write-Host "✅ PASS - Status: $statusCode (API está protegida)" -ForegroundColor Green
        $results += @{ Test = "API SIN autenticación"; Status = "PASS"; Code = $statusCode }
    } else {
        Write-Host "❌ FAIL - Status: $statusCode (Esperaba 401)" -ForegroundColor Red
        $results += @{ Test = "API SIN autenticación"; Status = "FAIL"; Code = $statusCode }
    }
} catch {
    Write-Host "❌ ERROR - $_" -ForegroundColor Red
    $results += @{ Test = "API SIN autenticación"; Status = "ERROR"; Code = "N/A" }
}
Write-Host ""

# ==================== RESUMEN ====================
Write-Host "╔══════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                    RESUMEN DE TESTS                  ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$passCount = ($results | Where-Object { $_.Status -eq "PASS" }).Count
$failCount = ($results | Where-Object { $_.Status -eq "FAIL" }).Count
$errorCount = ($results | Where-Object { $_.Status -eq "ERROR" }).Count

Write-Host "📊 Resultados:" -ForegroundColor White
Write-Host "  ✅ Pasados:  $passCount" -ForegroundColor Green
Write-Host "  ❌ Fallidos: $failCount" -ForegroundColor Red
Write-Host "  ⚠️  Errores:  $errorCount" -ForegroundColor Yellow
Write-Host ""

Write-Host "📋 Detalle de Tests:" -ForegroundColor White
$results | ForEach-Object {
    $statusColor = if ($_.Status -eq "PASS") { "Green" } elseif ($_.Status -eq "FAIL") { "Red" } else { "Yellow" }
    $statusSymbol = if ($_.Status -eq "PASS") { "✅" } elseif ($_.Status -eq "FAIL") { "❌" } else { "⚠️ " }
    Write-Host "  $statusSymbol $($_.Test): $($_.Status) (HTTP $($_.Code))" -ForegroundColor $statusColor
}
Write-Host ""

# ==================== INFORMACIÓN ====================
Write-Host "ℹ️  Información de Configuración:" -ForegroundColor Cyan
Write-Host "  API Base:        $ApiBase" -ForegroundColor Gray
Write-Host "  API Key Válida:  $ValidApiKey" -ForegroundColor Gray
Write-Host "  Servidor:        $(if (Test-Connection -ComputerName localhost -Count 1 -Quiet) {'✅ Online'} else {'❌ Offline'})" -ForegroundColor Gray
Write-Host ""

# ==================== CONCLUSIÓN ====================
if ($failCount -eq 0 -and $errorCount -eq 0 -and $passCount -eq 5) {
    Write-Host "🎉 ¡TODOS LOS TESTS PASARON EXITOSAMENTE!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Acceso a Swagger UI:" -ForegroundColor Green
    Write-Host "  curl -H `"X-API-Key: $ValidApiKey`" http://localhost:8080/api/swagger-ui.html" -ForegroundColor Gray
} else {
    Write-Host "⚠️  Algunos tests fallaron o hay errores - Verifica la configuración" -ForegroundColor Yellow
}
Write-Host ""
Write-Host "═════════════════════════════════════════════════════════" -ForegroundColor Cyan

