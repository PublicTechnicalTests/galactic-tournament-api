# 🐳 DOCKER - Guía Completa

## Galactic Tournament API - Docker & Docker Compose

---

## 📋 Requisitos Previos

- Docker 20.10+ instalado y corriendo
- Docker Compose 2.0+ instalado
- Al menos 2GB de memoria RAM disponible
- 1GB de espacio en disco

---

## 📁 Estructura de Carpetas

```
galactic-tournament-api/
├── Dockerfile                      # Docker image para la API
├── docker-compose.yml              # Orquestación de servicios
├── .env                           # Variables de entorno (generadas)
├── .env.example                   # Variables de ejemplo
├── .dockerignore                  # Archivos excluidos del build
├── ddl/
│   └── 01-init.sql               # Script SQL inicialización
├── docker/
│   └── postgres/
│       ├── postgres.conf         # Configuración PostgreSQL
│       └── data/                 # Volumen de datos
└── config/
    └── application.yaml          # Configuración app (opcional)
```

---

## 🚀 Inicio Rápido

### 1. Desde Cero (Build + Run)

```bash
# Navega a la carpeta del proyecto
cd galactic-tournament-api

# Construir la imagen Docker
docker-compose build

# Levantar los servicios
docker-compose up -d

# Verificar estado
docker-compose ps
```

### 2. Parar los Servicios

```bash
# Detener servicios (sin borrar datos)
docker-compose down

# Detener y borrar volúmenes (LIMPIA TODO)
docker-compose down -v
```

### 3. Ver Logs

```bash
# Ver logs de todos los servicios
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f api
docker-compose logs -f postgres

# Últimas 100 líneas
docker-compose logs --tail=100
```

---

## 🔍 Verificación y Testing

### Test 1: Verificar Servicios Activos

```bash
# Ver estado de servicios
docker-compose ps

# Debería mostrar:
# NAME                STATUS              PORTS
# galactic-api        Up (healthy)        0.0.0.0:8080->8080/tcp
# galactic-postgres   Up (healthy)        0.0.0.0:5432->5432/tcp
```

### Test 2: Verificar Conectividad a PostgreSQL

```bash
# Conectar a PostgreSQL desde host
psql -h localhost -U postgres -d galactic_tournament

# Dentro de psql:
SELECT * FROM especies;
SELECT version();
\dt  # Ver todas las tablas
```

### Test 3: Verificar API

```bash
# Con cURL
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html

# Con PowerShell
$h = @{"X-API-Key" = "test-key-123"}
Invoke-WebRequest -Uri http://localhost:8080/api/swagger-ui.html -Headers $h

# Debería retornar status 200
```

### Test 4: Health Check

```bash
# API health
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html

# Verificar que ambos contenedores están "healthy"
docker-compose ps
```

---

## 🛠️ Configuración y Personalización

### Cambiar Variables de Entorno

```bash
# Editar el archivo .env
# Ejemplo: cambiar puerto de API
API_PORT=9090

# Reconstruir y reiniciar
docker-compose up -d --build
```

### Eventos en el Archivo .env

```
DB_PORT=5432                    # Puerto PostgreSQL
DB_PASSWORD=tournament_password # Contraseña BD
API_PORT=8080                   # Puerto API
CORS_ALLOWED_ORIGINS=...        # CORS origins
API_KEYS=...                    # API Keys válidas
JAVA_OPTS=...                   # Opciones JVM
```

### Modificar Configuración de PostgreSQL

```bash
# Editar docker/postgres/postgres.conf
# Cambios toman efecto al reiniciar:
docker-compose restart postgres
```

---

## 📊 Docker Compose - Servicios

### Servicio: PostgreSQL

**Imagen**: `postgres:16-alpine`  
**Puerto**: 5432 (remapeo en .env)  
**Usuario**: postgres  
**Base de datos**: galactic_tournament  

**Health Check**:
```
Estado: Healthy solo cuando la BD responde a pg_isready
Intervalo: 10s
Timeout: 5s
Reintentos: 5
Startup delay: 30s
```

**Volúmenes**:
- `ddl/01-init.sql` → `/docker-entrypoint-initdb.d/01-init.sql`
  - Script de inicialización (se ejecuta automáticamente)
- `postgres_data` → `/var/lib/postgresql/data`
  - Persistencia de datos
- `docker/postgres/postgres.conf` → `/etc/postgresql/postgresql.conf`
  - Configuración personalizada

---

### Servicio: API (Java)

**Imagen**: Construida a partir de `Dockerfile`  
**Puerto**: 8080 (remapeo en .env)  
**Dependencia**: PostgreSQL (health check)  

**Health Check**:
```
Test: curl a /swagger-ui.html con API Key
Intervalo: 30s
Timeout: 10s
Reintentos: 3
Startup delay: 40s
```

**Volúmenes**:
- `logs/` → `/app/logs`
  - Para logs de la aplicación
- `config/application.yaml` → `/app/config/application.yaml`
  - Configuración personalizada (opcional)

**Variables de Entorno**:
Se pasan automáticamente desde `.env`:
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- API_KEYS
- CORS_ALLOWED_ORIGINS
- JAVA_OPTS

---

## 🔐 Escalado y Mantenimiento

### Build Manual

```bash
# Build de la imagen completa
docker-compose build --no-cache

# Build forzado sin cache
docker-compose build --force-rm --no-cache
```

### Limpiar Recursos

```bash
# Remover contenedores detenidos
docker container prune

# Remover imágenes no usadas
docker image prune

# Remover volúmenes no usados
docker volume prune

# Remover todo (CUIDADO)
docker system prune -a --volumes
```

### Backup de Base de Datos

```bash
# Dump de la BD
docker exec galactic-postgres pg_dump -U postgres galactic_tournament > backup.sql

# Restore desde backup
docker exec -i galactic-postgres psql -U postgres galactic_tournament < backup.sql
```

### Ver Tamaño de Volúmenes

```bash
# Tamaño total de Docker
docker system df

# Tamaño de volumen específico
docker volume inspect galactic-tournament-api_postgres_data
```

---

## 🐛 Troubleshooting

### Problema 1: "Port already in use"

**Causa**: El puerto está siendo usado por otro proceso  
**Solución**:
```bash
# Cambiar puerto en .env
API_PORT=9090

# O matar el proceso que usa el puerto
# En Windows (PowerShell):
Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process
```

### Problema 2: "Service postgres is unhealthy"

**Causa**: PostgreSQL no inicia correctamente  
**Solución**:
```bash
# Ver logs
docker-compose logs postgres

# Reiniciar
docker-compose restart postgres

# Verificar volumen
docker volume ls
```

### Problema 3: "API can't connect to database"

**Causa**: Problem de conectividad entre contenedores  
**Solución**:
```bash
# Verificar network
docker network ls
docker network inspect <network-id>

# Reiniciar servicios
docker-compose restart
```

### Problema 4: "Out of memory"

**Causa**: Configuración de memoria insuficiente  
**Solución**:
```bash
# En .env, ajustar JAVA_OPTS
JAVA_OPTS=-Xmx256m -Xms128m -XX:+UseG1GC

# Reiniciar
docker-compose restart api
```

### Problema 5: "Permission denied while trying to connect to Docker"

**Windows/Mac**: Reiniciar Docker Desktop  
**Linux**: 
```bash
sudo usermod -aG docker $USER
newgrp docker
```

---

## 📈 Monitoreo

### Ver Métricas en Tiempo Real

```bash
# CPU, memoria, I/O
docker stats

# Solo contenedores de este proyecto
docker stats galactic-postgres galactic-api
```

### Ver Eventos Docker

```bash
docker events --filter 'container=galactic-api'
```

### Inspeccionar Contenedor

```bash
# Ver detalles del contenedor
docker inspect galactic-api

# Ver IP del contenedor
docker inspect -f '{{.NetworkSettings.IPAddress}}' galactic-api
```

---

## 🔄 Workflows Comunes

### Desarrollo Local

```bash
# 1. Iniciar servicios
docker-compose up -d

# 2. Desarrollar aplicación (cambios locales)
# 3. Reiniciar API con cambios
docker-compose restart api

# 4. Ver logs
docker-compose logs -f api

# 5. Detener todo
docker-compose down
```

### Agregar Nueva Función

```bash
# 1. Parar servicios
docker-compose down

# 2. Modificar código/BD
# 3. Reconstruir imagen
docker-compose build

# 4. Reiniciar
docker-compose up -d

# 5. Probar
docker-compose logs -f
```

### Actualizar Base de Datos

```bash
# 1. Modificar ddl/01-init.sql
# 2. Borrar volumen de datos
docker volume rm galactic-tournament-api_postgres_data

# 3. Reiniciar servicios (se reinicializa BD)
docker-compose up -d

# 4. Verificar
docker-compose logs postgres
```

---

## 📊 Versiones

**Docker Compose Version**: 3.9  
**PostgreSQL**: 16 (Alpine)  
**Java**: 25 (Eclipse Temurin - JRE)  
**Base Image**: alpine:latest (mínimo)  

---

## 📚 Archivos Importantes

### Dockerfile
- Multi-stage: Build + Runtime
- Usuario no-root (appuser)
- Health check incluido
- Logs en JSON

### docker-compose.yml
- PostgreSQL con init automático
- Java API con health check
- Network bridge
- Volúmenes configurados
- Logs de contenedor limitados (10m, 3 archivos)

### .env
- Configuración por entorno
- Fácilmente personalizable
- No versionado en git

---

## ✅ Checklist

- [ ] Docker instalado y funcionando
- [ ] Docker Compose instalado
- [ ] Permisos de Docker configurados
- [ ] `docker-compose ps` muestra servicios healthy
- [ ] Swagger UI accesible en localhost:8080/api/swagger-ui.html
- [ ] PostgreSQL acepta conexiones
- [ ] Volúmenes creados y persistentes

---

## 🎉 ¡Listo!

Tu aplicación está completamente containerizada y lista para:
- ✅ Desarrollo local
- ✅ Testing
- ✅ Deployment

**Comando para iniciar**:
```bash
docker-compose up -d
```

¡Éxito! 🚀

