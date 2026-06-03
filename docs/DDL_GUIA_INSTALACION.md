# 🚀 GUÍA DE INSTALACIÓN - Base de Datos PostgreSQL

## Galactic Tournament API - Setup de BD

---

## 📋 Requisitos Previos

- PostgreSQL 13+ instalado
- Cliente `psql` (incluido con PostgreSQL)
- Acceso de administrador a PostgreSQL
- Usuario `postgres` (por defecto)

---

## 🔧 INSTALACIÓN PASO A PASO

### Paso 1: Verificar PostgreSQL

```bash
# Verificar que PostgreSQL está instalado
psql --version

# Debería mostrar algo como:
# psql (PostgreSQL) 13.0
```

### Paso 2: Crear la Base de Datos

#### Opción A: Usando el Script SQL

```bash
# Navega a la carpeta del proyecto
cd D:\PublicTechnicalTests\MorningValue\galactic-tournament-api

# Ejecutar el script DDL
psql -U postgres -f DDL_POSTGRESQL.sql

# Ingresa la contraseña de postgres cuando se solicite
```

#### Opción B: Manualmente

```bash
# Conectarse a PostgreSQL
psql -U postgres

# En el prompt de psql, ejecutar:
CREATE DATABASE galactic_tournament
    WITH
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0;

\c galactic_tournament

-- Luego ejecuta el contenido del archivo DDL_POSTGRESQL.sql
-- Puedes copiar y pegar el contenido del archivo
```

### Paso 3: Verificar la Creación

```bash
# Conectarse a la BD
psql -U postgres -d galactic_tournament

# Ver todas las tablas
\dt

# Debería mostrar:
# - public | combats    | table | postgres
# - public | especies   | table | postgres
# - public | rankings   | table | postgres

# Ver estructura de una tabla
\d+ especies

# Ver todas las vistas
\dv

# Ver todos los índices
\di
```

### Paso 4: Insertar Datos de Prueba (Opcional)

```sql
-- Conectar a la BD
psql -U postgres -d galactic_tournament

-- Insertar especies
INSERT INTO especies (name, power, ability) VALUES
    ('Klingons', 95, 'Hand-to-hand combat expertise'),
    ('Vulcans', 85, 'Mind meld and logic'),
    ('Ewoks', 60, 'Forest camouflage and ingenuity'),
    ('Wookiees', 90, 'Strength and roar'),
    ('Greys', 75, 'Telepathy and advanced technology');

-- Verificar inserción
SELECT * FROM especies;

-- Insertar rankings iniciales
INSERT INTO rankings (specie_id, victorias) VALUES
    (1, 0),
    (2, 0),
    (3, 0),
    (4, 0),
    (5, 0);

-- Verificar rankings
SELECT * FROM rankings;
```

### Paso 5: Configurar Conexión en la Aplicación

Verifica que `application.yaml` esté configurado correctamente:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/galactic_tournament
    username: postgres
    password: tu_contraseña_aqui
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate  # o 'validate' si prefieres no cambios automáticos
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Paso 6: Probar la Aplicación

```bash
# En PowerShell, navega a la carpeta del proyecto
cd D:\PublicTechnicalTests\MorningValue\galactic-tournament-api

# Ejecuta la aplicación
.\mvnw spring-boot:run

# Si ves mensajes como:
# "Hibernate: select ... from especies"
# ¡La conexión a la BD está funcionando!
```

---

## 🧪 PRUEBAS DE CONECTIVIDAD

### Test 1: Conexión Básica

```bash
psql -U postgres -d galactic_tournament -c "SELECT version();"

# Debería mostrar la versión de PostgreSQL
```

### Test 2: Ver Tablas

```bash
psql -U postgres -d galactic_tournament -c "\dt"

# Debería mostrar las 3 tablas
```

### Test 3: Contar Registros

```bash
psql -U postgres -d galactic_tournament -c "SELECT COUNT(*) FROM especies;"

# Debería mostrar un número
```

### Test 4: Ver Vistas

```bash
psql -U postgres -d galactic_tournament -c "\dv"

# Debería mostrar las 2 vistas
```

---

## 🐛 TROUBLESHOOTING

### Problema 1: "PostgreSQL no está en PATH"

**Solución**: 
```bash
# Busca la ruta de instalación de PostgreSQL (típicamente)
cd "C:\Program Files\PostgreSQL\13\bin"

# O agregarlo a PATH:
# Configuración → Variables de Entorno → Editar variables de entorno del sistema
# → Editar "Path" → Agregar C:\Program Files\PostgreSQL\13\bin
```

### Problema 2: "Error: database already exists"

**Solución**: 
```bash
# Eliminar la BD existente
psql -U postgres -c "DROP DATABASE IF EXISTS galactic_tournament;"

# Luego crear nuevamente
psql -U postgres -f DDL_POSTGRESQL.sql
```

### Problema 3: "FATAL: Ident authentication failed"

**Solución**: 
```bash
# Verificar archivo pg_hba.conf
# Ubicación típica: C:\Program Files\PostgreSQL\13\data\pg_hba.conf

# Cambiar la línea de autenticación local de:
# local   all             all                                     ident

# A:
# local   all             all                                     password

# Reiniciar PostgreSQL después
```

### Problema 4: "Error: Connection refused"

**Solución**: 
```bash
# Verificar que PostgreSQL está corriendo
# En Windows, revisar Servicios (services.msc) buscar "PostgreSQL"

# Si no está activo, iniciarlo
net start PostgreSQL13  # Ajusta el número de versión
```

### Problema 5: "SQLSTATE[3F000]: Invalid catalog name"

**Solución**: 
```bash
# Asegúrate de haber ejecutado:
\c galactic_tournament

# Antes de ejecutar el resto del script
```

---

## 📊 VERIFICACIÓN FINAL

### Ejecutar Todos los Checks

```sql
-- Conectarse
psql -U postgres -d galactic_tournament

-- Verificar tablas
SELECT tablename FROM pg_tables WHERE schemaname = 'public';

-- Verificar índices
SELECT indexname FROM pg_indexes WHERE schemaname = 'public';

-- Verificar vistas
SELECT viewname FROM pg_views WHERE schemaname = 'public';

-- Verificar secuencias
SELECT sequencename FROM pg_sequences WHERE schemaname = 'public';

-- Verificar constraints
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_schema = 'public';
```

---

## 🔐 SEGURIDAD (Recomendaciones)

### 1. Crear Usuario Específico

```sql
-- Crear usuario
CREATE USER galactic_user WITH PASSWORD 'contraseña_segura';

-- Dar permisos
GRANT CONNECT ON DATABASE galactic_tournament TO galactic_user;
GRANT USAGE ON SCHEMA public TO galactic_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO galactic_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO galactic_user;
```

### 2. Usar Variables de Entorno

```yaml
# application.yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/galactic_tournament
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:}
    driver-class-name: org.postgresql.Driver
```

```bash
# En el terminal, antes de ejecutar
$env:DB_USER = "galactic_user"
$env:DB_PASSWORD = "contraseña_segura"

.\mvnw spring-boot:run
```

---

## 📈 COMANDOS ÚTILES

### Backup de la BD

```bash
# Backup completo
pg_dump -U postgres galactic_tournament > galactic_tournament_backup.sql

# Backup solo de datos
pg_dump -a -U postgres galactic_tournament > galactic_tournament_data.sql

# Backup comprimido
pg_dump -U postgres galactic_tournament | gzip > galactic_tournament_backup.sql.gz
```

### Restore de la BD

```bash
# Desde archivo SQL
psql -U postgres < galactic_tournament_backup.sql

# Desde archivo comprimido
gunzip -c galactic_tournament_backup.sql.gz | psql -U postgres
```

### Otros Comandos Útiles

```sql
-- Ver tamaño de la BD
SELECT pg_size_pretty(pg_database_size('galactic_tournament'));

-- Ver tamaño de tablas
SELECT schemaname, tablename, 
       pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Ver conexiones activas
SELECT datname, count(*) FROM pg_stat_activity GROUP BY datname;

-- Vaciar tablas
TRUNCATE TABLE combats CASCADE;
TRUNCATE TABLE rankings CASCADE;
TRUNCATE TABLE especies CASCADE;
```

---

## ✅ Checklist de Instalación

- [ ] PostgreSQL 13+ instalado
- [ ] Base de datos `galactic_tournament` creada
- [ ] Tablas creadas (especies, combats, rankings)
- [ ] Índices creados
- [ ] Vistas creadas
- [ ] Función `sp_record_battle` creada
- [ ] Datos de prueba insertados (opcional)
- [ ] `application.yaml` configurado
- [ ] Conexión desde aplicación comprobada
- [ ] Swagger UI accesible en http://localhost:8080/api/swagger-ui.html

---

## 🎉 ¡Listo!

Si todos los pasos se completaron correctamente, tu base de datos está lista para usar.

**Próximos pasos**:
1. Ejecuta: `.\mvnw spring-boot:run`
2. Accede a: `http://localhost:8080/api/swagger-ui.html`
3. Ingresa API Key: `test-key-123`
4. ¡Prueba los endpoints!

---

## 📞 Contacto / Soporte

Si encuentras problemas:
1. Revisar el archivo `DDL_DOCUMENTACION.md`
2. Consultar logs de PostgreSQL
3. Verificar la sección de Troubleshooting arriba


