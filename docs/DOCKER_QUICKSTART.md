# 🚀 Docker - Quick Start

## Galactic Tournament API - Iniciar Rápidamente

---

## ⚡ 3 Comandos para Iniciar

```bash
# 1. Construir imagen Docker
docker-compose build

# 2. Levantar servicios (PostgreSQL + API)
docker-compose up -d

# 3. Verificar estado
docker-compose ps
```

---

## 🌐 Acceso Inmediato

### Swagger UI (API Documentation)
```
URL: http://localhost:8080/api/swagger-ui.html
API Key: test-key-123 (ingresar en "Authorize")
```

### PostgreSQL (Database)
```
Host: localhost
Port: 5432
User: postgres
Password: tournament_password
Database: galactic_tournament

Conexión: psql -h localhost -U postgres -d galactic_tournament
```

---

## 📊 Servicios Activos

```
┌─────────────────────────────────────────┐
│ galactic-postgres (PostgreSQL 16)       │
│ Port: 5432                              │
│ Status: Healthy                         │
└─────────────────────────────────────────┘
           ↓ (waits for health)
┌─────────────────────────────────────────┐
│ galactic-api (Java 25)                  │
│ Port: 8080                              │
│ Status: Healthy                         │
└─────────────────────────────────────────┘
```

---

## 📁 Estructura Docker

```
Descargar imagen PostgreSQL 16
        ↓
Crear contenedor postgres
        ↓
Ejecutar ddl/01-init.sql
        ↓
PostgreSQL HEALTHY ✓
        ↓
Construir imagen Docker (Dockerfile)
        ↓
Crear contenedor galactic-api
        ↓
Conectar a PostgreSQL
        ↓
API HEALTHY ✓
        ↓
✅ LISTO PARA USAR
```

---

## 🎮 Comandos Frecuentes

```bash
# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio
docker-compose logs -f api
docker-compose logs -f postgres

# Parar servicios
docker-compose down

# Reiniciar servicios
docker-compose restart

# Entrar a la aplicación (shell)
docker exec -it galactic-api sh

# Ejecutar comando en PostgreSQL
docker exec galactic-postgres psql -U postgres -d galactic_tournament -c "SELECT * FROM especies;"

# Ver métricas (CPU, memoria)
docker stats
```

---

## 🔧 Configuración Personalizada

Edita `.env` para cambiar:

```env
# Puerto PostgreSQL
DB_PORT=5432

# Contraseña PostgreSQL
DB_PASSWORD=tournament_password

# Puerto API
API_PORT=8080

# API Keys válidas
API_KEYS=test-key-123,test-key-456,dev-api-key

# CORS Origins
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

Luego reinicia:
```bash
docker-compose up -d --build
```

---

## ✅ Verificación Rápida

```bash
# ¿PostgreSQL responde?
docker exec galactic-postgres pg_isready -U postgres

# ¿API responde?
curl -H "X-API-Key: test-key-123" http://localhost:8080/api/swagger-ui.html

# ¿Tablas creadas?
docker exec galactic-postgres psql -U postgres -d galactic_tournament -c "\dt"
```

---

## 🧹 Limpiar

```bash
# Parar servicios
docker-compose down

# Parar y borrar volúmenes (cuidado: borra BD)
docker-compose down -v
```

---

## 📖 Documentación Completa

Ver: `DOCKER_GUIA.md`

---

## 🎉 ¡Listo!

```bash
docker-compose up -d
```

Accede a: http://localhost:8080/api/swagger-ui.html

¡Éxito! 🚀

