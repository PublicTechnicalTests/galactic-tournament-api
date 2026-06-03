# Ejemplos de Testing - Acceso a Swagger

Aquí encontrarás ejemplos prácticos para probar que Swagger UI es accesible sin autenticación.

---

## 1. Pruebas Manuales en el Navegador

### ✅ Acceso Permitido (Swagger)
```
http://localhost:8080/api/swagger-ui.html
http://localhost:8080/api/swagger-ui/index.html
http://localhost:8080/api/v3/api-docs
```

### 🔐 Acceso Bloqueado (Sin Autenticación)
```
http://localhost:8080/api/v1/species
http://localhost:8080/api/v1/battles
http://localhost:8080/api/v1/rankings
```

---

## 2. Pruebas con cURL

### Ver Documentación OpenAPI (Público)
```bash
curl http://localhost:8080/api/v3/api-docs | jq
```

### Listar Recursos Swagger (Público)
```bash
curl http://localhost:8080/api/swagger-ui.html
```

### Acceder a API sin Autenticación (Debería dar 401)
```bash
curl -v http://localhost:8080/api/v1/species
```

### Acceder a API con Autenticación Básica
```bash
curl -u user:password http://localhost:8080/api/v1/species
```

---

## 3. Pruebas con PowerShell (Windows)

### Verificar Swagger accesible sin autenticación
```powershell
$uri = "http://localhost:8080/api/swagger-ui.html"
$response = Invoke-WebRequest -Uri $uri -SkipHttpErrorCheck
Write-Host "Status Code:" $response.StatusCode
Write-Host "Es accesible:" ($response.StatusCode -eq 200)
```

### Verificar OpenAPI Docs
```powershell
$uri = "http://localhost:8080/api/v3/api-docs"
$response = Invoke-RestMethod -Uri $uri
Write-Host "Endpoints encontrados:" $response.paths.Count
$response.paths.PSObject.Properties | ForEach-Object { Write-Host "→" $_.Name }
```

### Intentar acceder a API sin autenticación (esperar 401)
```powershell
$uri = "http://localhost:8080/api/v1/species"
$response = Invoke-WebRequest -Uri $uri -SkipHttpErrorCheck
Write-Host "Status Code:" $response.StatusCode
Write-Host "Esperado: 401 (No Autorizado)"
```

### Acceder a API con autenticación básica
```powershell
$uri = "http://localhost:8080/api/v1/species"
$credentials = New-Object System.Management.Automation.PSCredential("admin", (ConvertTo-SecureString "password" -AsPlainText -Force))
$response = Invoke-RestMethod -Uri $uri -Credential $credentials
Write-Host "Response:" $response
```

---

## 4. Pruebas con Postman

### Crear una nueva solicitud en Postman:

#### 1️⃣ Solicitud: Acceder a Swagger (Debe funcionar)
```
GET http://localhost:8080/api/swagger-ui.html
```
- **Headers**: (ninguno requerido)
- **Status esperado**: 200 OK

#### 2️⃣ Solicitud: Obtener Doc OpenAPI (Debe funcionar)
```
GET http://localhost:8080/api/v3/api-docs
```
- **Headers**: (ninguno requerido)
- **Status esperado**: 200 OK
- **Response**: JSON con toda la documentación

#### 3️⃣ Solicitud: Acceder a API (Debe rechazar)
```
GET http://localhost:8080/api/v1/species
```
- **Headers**: (ninguno)
- **Status esperado**: 401 Unauthorized

#### 4️⃣ Solicitud: Acceder a API con Auth (Debe funcionar)
```
GET http://localhost:8080/api/v1/species
```
- **Authentication Tab**: Seleccionar "Basic Auth"
- **Username**: admin
- **Password**: password
- **Status esperado**: 200 OK (o error del API, pero autenticado)

---

## 5. Script Bash para Testing Completo

```bash
#!/bin/bash

echo "========== GALACTIC TOURNAMENT API - TEST DE SEGURIDAD =========="
echo ""

API_BASE="http://localhost:8080/api"

# Test 1: Swagger debe ser accesible
echo "✓ Test 1: Acceso a Swagger UI"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $API_BASE/swagger-ui.html)
if [ "$STATUS" == "200" ]; then
    echo "  ✅ PASS - Swagger es público (HTTP $STATUS)"
else
    echo "  ❌ FAIL - Swagger no accesible (HTTP $STATUS)"
fi
echo ""

# Test 2: OpenAPI Docs debe ser accesible
echo "✓ Test 2: Acceso a OpenAPI Docs"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $API_BASE/v3/api-docs)
if [ "$STATUS" == "200" ]; then
    echo "  ✅ PASS - OpenAPI es público (HTTP $STATUS)"
else
    echo "  ❌ FAIL - OpenAPI no accesible (HTTP $STATUS)"
fi
echo ""

# Test 3: API debe requerir autenticación
echo "✓ Test 3: API debe requerir autenticación"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $API_BASE/v1/species)
if [ "$STATUS" == "401" ] || [ "$STATUS" == "403" ]; then
    echo "  ✅ PASS - API está protegida (HTTP $STATUS)"
else
    echo "  ⚠️  WARNING - API status es HTTP $STATUS (esperaba 401/403)"
fi
echo ""

# Test 4: Mostrar endpoints disponibles
echo "✓ Test 4: Endpoints disponibles en la API"
curl -s $API_BASE/v3/api-docs | jq '.paths | keys[]' | head -10
echo ""

echo "========== FIN DE TESTS =========="
```

**Para ejecutar**:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

## 6. Archivo HTML para Testing Visual

Crea un archivo `test-security.html` en la carpeta del proyecto:

```html
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Galactic Tournament API - Security Test</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .test-item {
            background: white;
            margin: 15px 0;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        button {
            background: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 3px;
            cursor: pointer;
            font-size: 14px;
        }
        button:hover {
            background: #0056b3;
        }
        .result {
            margin-top: 10px;
            padding: 10px;
            border-radius: 3px;
            display: none;
        }
        .success {
            background: #d4edda;
            color: #155724;
            display: block;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            display: block;
        }
        h1 {
            color: #333;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }
        .api-key {
            background: #f0f0f0;
            padding: 10px;
            border-radius: 3px;
            font-family: monospace;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <h1>🚀 Galactic Tournament API - Security Test</h1>
    
    <div class="test-item">
        <h2>1. Swagger UI (Debe estar público)</h2>
        <button onclick="testSwagger()">Probar Acceso</button>
        <div id="swagger-result" class="result"></div>
        <p><small><a href="http://localhost:8080/api/swagger-ui.html" target="_blank">Abrir Swagger →</a></small></p>
    </div>

    <div class="test-item">
        <h2>2. OpenAPI Docs (Debe estar público)</h2>
        <button onclick="testOpenAPI()">Probar Acceso</button>
        <div id="openapi-result" class="result"></div>
    </div>

    <div class="test-item">
        <h2>3. API Endpoint (Debe estar protegido SIN autenticación)</h2>
        <button onclick="testAPINoAuth()">Probar SIN Autenticación</button>
        <div id="api-noauth-result" class="result"></div>
    </div>

    <div class="test-item">
        <h2>4. API Endpoint (Con autenticación básica)</h2>
        <input type="text" id="username" placeholder="Usuario" value="admin">
        <input type="password" id="password" placeholder="Contraseña" value="password">
        <button onclick="testAPIWithAuth()">Probar CON Autenticación</button>
        <div id="api-auth-result" class="result"></div>
    </div>

    <div class="test-item">
        <h2>📊 Resumen de Resultados</h2>
        <div id="summary">
            <p>Ejecuta las pruebas arriba para ver el resumen</p>
        </div>
    </div>

    <script>
        const API_BASE = 'http://localhost:8080/api';
        const results = {};

        async function testSwagger() {
            const div = document.getElementById('swagger-result');
            try {
                const response = await fetch(`${API_BASE}/swagger-ui.html`);
                results.swagger = response.status === 200 ? '✅ PASS' : '❌ FAIL';
                div.className = response.status === 200 ? 'result success' : 'result error';
                div.textContent = `Status: ${response.status} - ${results.swagger}`;
            } catch (e) {
                results.swagger = '❌ ERROR';
                div.className = 'result error';
                div.textContent = `Error: ${e.message}`;
            }
            updateSummary();
        }

        async function testOpenAPI() {
            const div = document.getElementById('openapi-result');
            try {
                const response = await fetch(`${API_BASE}/v3/api-docs`);
                results.openapi = response.status === 200 ? '✅ PASS' : '❌ FAIL';
                div.className = response.status === 200 ? 'result success' : 'result error';
                const data = await response.json();
                div.textContent = `Status: ${response.status} - ${results.openapi}\nEndpoints encontrados: ${Object.keys(data.paths || {}).length}`;
            } catch (e) {
                results.openapi = '❌ ERROR';
                div.className = 'result error';
                div.textContent = `Error: ${e.message}`;
            }
            updateSummary();
        }

        async function testAPINoAuth() {
            const div = document.getElementById('api-noauth-result');
            try {
                const response = await fetch(`${API_BASE}/v1/species`);
                const shouldBe401 = response.status === 401 || response.status === 403;
                results.apiNoAuth = shouldBe401 ? '✅ PASS (Protegido)' : '⚠️ WARNING';
                div.className = shouldBe401 ? 'result success' : 'result error';
                div.textContent = `Status: ${response.status} - ${results.apiNoAuth}\n${shouldBe401 ? 'API está correctamente protegido' : 'API debería estar protegido'}`;
            } catch (e) {
                results.apiNoAuth = '❌ ERROR';
                div.className = 'result error';
                div.textContent = `Error: ${e.message}`;
            }
            updateSummary();
        }

        async function testAPIWithAuth() {
            const div = document.getElementById('api-auth-result');
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            
            try {
                const auth = btoa(`${username}:${password}`);
                const response = await fetch(`${API_BASE}/v1/species`, {
                    headers: {
                        'Authorization': `Basic ${auth}`
                    }
                });
                const isSuccess = response.status >= 200 && response.status < 400;
                results.apiAuth = isSuccess ? '✅ PASS (Autenticado)' : `⚠️ Status ${response.status}`;
                div.className = isSuccess ? 'result success' : 'result error';
                div.textContent = `Status: ${response.status} - ${results.apiAuth}`;
            } catch (e) {
                results.apiAuth = '❌ ERROR';
                div.className = 'result error';
                div.textContent = `Error: ${e.message}`;
            }
            updateSummary();
        }

        function updateSummary() {
            const summary = document.getElementById('summary');
            let html = '<ul>';
            for (const [key, value] of Object.entries(results)) {
                html += `<li><strong>${key}:</strong> ${value}</li>`;
            }
            html += '</ul>';
            summary.innerHTML = html;
        }
    </script>
</body>
</html>
```

**Para usar**: Guarda como `test-security.html` y abre en tu navegador.

---

## 7. Checklist de Verificación

- [ ] Swagger UI accesible sin autenticación
- [ ] OpenAPI Docs accesible sin autenticación
- [ ] Endpoints de API requieren autenticación
- [ ] Autenticación básica funciona correctamente
- [ ] Proyecto compila sin errores
- [ ] Proyecto inicia sin errores
- [ ] CORS configurado correctamente (si aplica)
- [ ] Logs muestran inicio correcto de SecurityConfig

---

## Próximos Pasos

1. Ejecuta uno de los tests para verificar que todo funciona
2. Accede a `http://localhost:8080/api/swagger-ui.html`
3. Verifica que puedas ver toda la documentación de la API
4. Prueba los endpoints desde Swagger (requerirá autenticación)

¡Éxito! 🎉

