# 🚀 Galactic Tournament API

Una API REST moderna y segura para gestionar torneos galácticos, con soporte para especies, combates y rankings. Construida con Spring Boot 4.0.6 y Java 25.

---

## 📋 Descripción del Sistema

**Galactic Tournament API** es una aplicación backend que permite:

- **Gestión de Especies**: Crear, listar y gestionar diferentes especies galácticas
- **Registro de Combates**: Registrar enfrentamientos entre especies con resultados
- **Rankings**: Visualizar rankings dinámicos basados en los resultados de combates
- **Documentación Interactiva**: Acceso a Swagger UI para explorar y probar la API
- **Seguridad**: Protección mediante API Keys (para documentación) y HTTP Basic Auth (para APIs)

### Características Principales

✅ **API RESTful** - Endpoints bien diseñados siguiendo estándares REST  
✅ **Base de Datos PostgreSQL** - Persistencia confiable con PostgreSQL 16  
✅ **Swagger/OpenAPI** - Documentación interactiva y auto-generada  
✅ **Autenticación Segura** - API Keys en headers + HTTP Basic Auth  
✅ **Docker Ready** - Docker Compose para deployments rápidos  
✅ **Tests Unitarios** - Suite completa de pruebas incluida  
✅ **Java 25 LTS** - Última versión LTS de Java  

---

## 🔧 Requisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

### Opción 1: Ejecución Local

| Requisito | Versión | Descripción |
|-----------|---------|-------------|
| **Java** | 25+ | OpenJDK 25 o superior (LTS) |
| **Maven** | 3.8.9+ | Gestor de dependencias |
| **PostgreSQL** | 16+ | Base de datos |
| **Git** | (Recomendado) | Control de versiones |

### Opción 2: Ejecución con Docker (Recomendado)

| Requisito | Versión |
|-----------|---------|
| **Docker** | 20.10+ |
| **Docker Compose** | 2.0+ |

#### Instalación de Dependencias

**Windows:**
```powershell
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar PostgreSQL (si ejecutas localmente)
psql --version

# Verificar Docker
docker --version
docker-compose --version
```

**Mac/Linux:**
```bash
java -version
mvn -version
psql --version
docker --version
docker-compose --version
```

---

## 📦 Instalación y Ejecución

### Opción 1: Ejecución Rápida con Docker Compose (⭐ RECOMENDADO)

La forma más fácil de ejecutar toda la aplicación incluyendo la base de datos:

#### Paso 1: Clonar o descargar el proyecto
```bash
# Si tienes Git
git clone <tu-repositorio>
cd galactic-tournament-api

# O simplemente navega al directorio del proyecto
```

#### Paso 2: Levantar los servicios
```bash
docker-compose up -d
```

Esto iniciará:
- 🐘 **PostgreSQL 16** en `localhost:5432`
- 🚀 **API REST** en `http://localhost:8080`

#### Paso 3: Verificar que está funcionando
```powershell
# Esperar 30-40 segundos para que todo inicie
# Luego probar:
$headers = @{"X-API-Key" = "test-key-123"}
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/swagger-ui.html" -Headers $headers -SkipHttpErrorCheck
$response.StatusCode  # Debería mostrar: 200
```

#### Acceder a la API
```
🌐 Swagger UI: http://localhost:8080/api/swagger-ui.html
📚 OpenAPI Docs: http://localhost:8080/api/v3/api-docs
```

**API Key de prueba**: `test-key-123`

#### Detener los servicios
```bash
docker-compose down
```

---

### Opción 2: Ejecución Local (Java + Maven + PostgreSQL)

Para desarrollo local con mayor control.

#### Paso 1: Configurar la Base de Datos

**En Windows (PostgreSQL local):**
```powershell
# Conectar a PostgreSQL
psql -U postgres

# En la terminal psql:
CREATE DATABASE galactic_tournament ENCODING UTF8;
ALTER DATABASE galactic_tournament SET client_encoding = 'UTF8';
\q
```

**Cargar el schema:**
```bash
psql -U postgres -d galactic_tournament -f ddl/01-init.sql
```

#### Paso 2: Crear el archivo de configuración

Crea `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: galactic-tournament-api
  
  datasource:
    url: jdbc:postgresql://localhost:5432/galactic_tournament
    username: postgres
    password: tournament_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
    properties:
      hibernate:
        format_sql: true
  
  security:
    user:
      name: admin
      password: admin123

app:
  api-keys: test-key-123,test-key-456,dev-api-key

server:
  servlet:
    context-path: /api
  port: 8080

logging:
  level:
    root: INFO
    com:
      technicaltests:
        mv: DEBUG
```

#### Paso 3: Compilar la aplicación
```bash
cd galactic-tournament-api

# Limpiar y compilar
./mvnw clean compile

# O en Windows
mvnw.cmd clean compile
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
```

#### Paso 4: Ejecutar la aplicación
```bash
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run
```

**Espera a ver:**
```
Started GalacticTournamentApiApplication in X seconds
```

#### Paso 5: Acceder a la API
```
🌐 Swagger UI: http://localhost:8080/api/swagger-ui.html
📚 OpenAPI Docs: http://localhost:8080/api/v3/api-docs
```

**Credenciales de prueba en Swagger:**
- **API Key**: `test-key-123` (en header `X-API-Key`)
- **Usuario HTTP Basic**: `admin` / `admin123`

---

## 🔑 Claves de API y Seguridad

### API Keys Disponibles

| Clave | Estado | Uso |
|-------|--------|-----|
| `test-key-123` | ✅ Activa | Testing y desarrollo |
| `test-key-456` | ✅ Activa | Testing alternativo |
| `dev-api-key` | ✅ Activa | Desarrollo local |

### Cambiar las API Keys

**Con Docker:**
```bash
docker-compose down
API_KEYS=mi-clave-1,mi-clave-2 docker-compose up -d
```

**Local - Edita `src/main/resources/application.yaml`:**
```yaml
app:
  api-keys: mi-clave-1,mi-clave-2,mi-clave-3
```

Luego recompila y ejecuta.

---

## 🧪 Testing

### Ejecutar Tests

```bash
./mvnw test

# O en Windows
mvnw.cmd test
```

### Tests Incluidos

- ✅ `GalacticTournamentApiApplicationTests` - Tests de contexto
- ✅ `SpecieServiceImplTest` - Tests de especies
- ✅ `CombatServiceImplTest` - Tests de combates
- ✅ `RankingServiceImplTest` - Tests de rankings

### Testing Manual con Script PowerShell

```powershell
# Ejecutar suite de tests
.\docs\test-api-key.ps1
```

### Testing Manual con cURL

```bash
# Listar especies
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v1/species

# Crear una especie (requiere HTTP Basic Auth)
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/species \
  -H "Content-Type: application/json" \
  -d '{"name":"Humano","description":"Especie terrestre"}'

# Obtener documentación OpenAPI
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/v3/api-docs
```

### Testing Manual con PowerShell

```powershell
# Obtener todas las especies
$headers = @{"X-API-Key" = "test-key-123"}
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/v1/species" -Headers $headers
$response.Content | ConvertFrom-Json | Format-Table
```

---

## 📚 Endpoints Principales

### Especies

```
GET    /api/v1/species              - Listar todas las especies
POST   /api/v1/species              - Crear nueva especie
GET    /api/v1/species/{id}         - Obtener detalles de una especie
PUT    /api/v1/species/{id}         - Actualizar especie
DELETE /api/v1/species/{id}         - Eliminar especie
```

### Combates

```
GET    /api/v1/combats              - Listar todos los combates
POST   /api/v1/combats              - Registrar nuevo combate
GET    /api/v1/combats/{id}         - Obtener detalles del combate
```

### Rankings

```
GET    /api/v1/rankings             - Obtener ranking de especies
GET    /api/v1/rankings/species/{id} - Rankings por especie
```

> 💡 Accede al **Swagger UI** en `http://localhost:8080/api/swagger-ui.html` para ver todos los endpoints con ejemplos interactivos.

---

## 🏗️ Estructura del Proyecto

```
galactic-tournament-api/
├── src/
│   ├── main/
│   │   ├── java/com/technicaltests/mv/galactictournamentapi/
│   │   │   ├── entity/              # Modelos JPA (Specie, Combat, Ranking)
│   │   │   ├── repository/          # Interfaces JPA Repository
│   │   │   ├── service/             # Lógica de negocio
│   │   │   ├── controller/          # Endpoints REST
│   │   │   ├── config/              # Configuración (Security, API Key Filter)
│   │   │   └── specification/       # JPA Specifications para filtros
│   │   └── resources/
│   │       └── application.yaml     # Configuración de la aplicación
│   │
│   └── test/
│       ├── java/                    # Tests unitarios
│       └── resources/
│           └── application-test.yaml # Config para tests
│
├── ddl/                             # Scripts de base de datos
│   ├── 01-init.sql                 # Script de inicialización
│   └── DDL_POSTGRESQL.sql          # DDL completo
│
├── docs/                            # Documentación
│   ├── 00_COMIENZA_AQUI.md         # Guía de inicio
│   ├── DOCKER_QUICKSTART.md        # Quick start con Docker
│   └── test-api-key.ps1            # Script de testing
│
├── Dockerfile                       # Imagen Docker de la aplicación
├── docker-compose.yml               # Orquestación de servicios
├── pom.xml                          # Dependencias Maven
└── README.md                        # Este archivo
```

---

## ⚙️ Configuración Avanzada

### Variables de Entorno

| Variable | Valor por Defecto | Descripción |
|----------|-------------------|-------------|
| `DB_PASSWORD` | `tournament_password` | Contraseña de PostgreSQL |
| `DB_PORT` | `5432` | Puerto de PostgreSQL |
| `API_PORT` | `8080` | Puerto de la API |
| `API_KEYS` | `test-key-123,test-key-456,dev-api-key` | Claves de API válidas |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:4200,http://localhost:5173` | Orígenes CORS permitidos |
| `JAVA_OPTS` | `-Xmx512m -Xms256m -XX:+UseG1GC` | Opciones de JVM |

### Personalizar con Docker

```bash
# Cambiar contraseña de BD y API Keys
DB_PASSWORD=mi_password \
API_KEYS=mi-clave-1,mi-clave-2 \
docker-compose up -d
```

### Personalizar Localmente

Edita `src/main/resources/application.yaml` y cambia los valores de configuración.

---

## 🔍 Troubleshooting

### "No se pude conectar a PostgreSQL"

**Síntomas:**
```
ERROR: de.bwaldvogel.mongo.MongoHandlerFactory - Unable to connect to database
```

**Solución:**
- Verificar que PostgreSQL está corriendo: `psql -U postgres`
- Verificar la URL de conexión en `application.yaml`
- Asegurarse que la base de datos `galactic_tournament` existe

### "API Key rechazada (401)"

**Soluciones:**
1. Verificar que el header se llama `X-API-Key` (exactamente)
2. Usar una clave válida: `test-key-123`
3. Verificar que la configuración incluye la clave en `app.api-keys`

### "Puerto 8080 ya está en uso"

```bash
# Cambiar puerto en application.yaml
server:
  port: 8081  # Nuevo puerto
```

O con Docker:
```bash
API_PORT=9090 docker-compose up -d
```

### "Maven: command not found"

```bash
# Usar el maven wrapper incluido
./mvnw clean compile    # Mac/Linux
mvnw.cmd clean compile  # Windows
```

### "Java version mismatch"

Asegúrate de tener Java 25:
```bash
java -version  # Debería mostrar "java 25"
```

Si no la tienes, descárgala de [adoptium.net](https://adoptium.net)

---

## 📖 Documentación Adicional

Consulta estos archivos en la carpeta `docs/` para información detallada:

| Documento | Contenido |
|-----------|-----------|
| `00_COMIENZA_AQUI.md` | Resumen completo de la implementación |
| `DOCKER_QUICKSTART.md` | Guía rápida de Docker |
| `DOCKER_GUIA.md` | Guía completa de Docker |
| `API_KEY_HEADER_SWAGGER.md` | Detalles de seguridad con API Keys |
| `EJEMPLOS_TESTING.md` | Ejemplos de testing de la API |
| `test-api-key.ps1` | Script PowerShell para testing |

---

## 🚀 Próximos Pasos

### Para Desarrollo

1. Clonar el repositorio
2. Ejecutar `docker-compose up -d` o configurar PostgreSQL local
3. Compilar: `./mvnw clean compile`
4. Ejecutar: `./mvnw spring-boot:run`
5. Acceder a Swagger: `http://localhost:8080/api/swagger-ui.html`

### Para Producción

1. Cambiar todas las API Keys por claves seguras
2. Configurar las variables de entorno apropiadamente
3. Usar HTTPS en lugar de HTTP
4. Implementar rotación de API Keys
5. Configurar logs persistentes
6. Hacer backup automatizado de la BD

### Mejoras Futuras

- [ ] Implementar JWT en lugar de API Keys
- [ ] Agregar autenticación OAuth2
- [ ] Implementar rate limiting
- [ ] Agregar caché con Redis
- [ ] Crear dashboard de monitoreo
- [ ] Implementar CI/CD con GitHub Actions o Jenkins

---

## 🤝 Contribuciones

Para reportar bugs o sugerir mejoras, contacta al equipo de desarrollo.

---

## 📋 Versiones

| Componente | Versión |
|-----------|---------|
| Java | 25 LTS |
| Spring Boot | 4.0.6 |
| PostgreSQL | 16 |
| Maven | 3.8.9+ |
| Docker | 20.10+ |

---

## ✅ Checklist de Verificación

Una vez ejecutado el proyecto, verifica:

- [ ] PostgreSQL está corriendo
- [ ] La API responde en `http://localhost:8080/api/health`
- [ ] Swagger UI es accesible con API Key válida
- [ ] Puedes ejecutar al menos una prueba exitosamente
- [ ] Los logs no muestran errores críticos

---

## 📞 Soporte

Para preguntas o problemas:

1. **Consulta la documentación**: Revisa los archivos en `docs/`
2. **Ejecuta los tests**: `./mvnw test`
3. **Verifica los logs**: `logs/galactic-tournament-api.log`
4. **Contacta al equipo**: [Tu información de contacto]

---

## 🎉 ¡Listo!

Tu aplicación Galactic Tournament API está lista para usar. 

**Comanda para comenzar inmediatamente:**
```bash
docker-compose up -d
# Esperar 30-40 segundos y acceder a http://localhost:8080/api/swagger-ui.html
# (Usa API Key: test-key-123)
```

¡Que disfrutes desarrollando tu torneo galáctico! 🚀🌌

