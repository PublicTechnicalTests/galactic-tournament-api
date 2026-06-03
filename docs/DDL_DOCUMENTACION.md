# 📊 DDL - DOCUMENTACIÓN DE BASE DE DATOS

## Galactic Tournament API - PostgreSQL

---

## 📋 Resumen

Este documento describe el esquema de base de datos (DDL) para la aplicación Galactic Tournament API en PostgreSQL.

**DBMS**: PostgreSQL 13+  
**Encoding**: UTF-8  
**Base de datos**: `galactic_tournament`

---

## 🏗️ ESTRUCTURA DE TABLAS

### 1. Tabla: `ESPECIES` (Species)

#### Propósito
Almacena información sobre las especies galácticas que participan en los torneos.

#### Estructura
```sql
CREATE TABLE especies (
    specie_id          BIGSERIAL PRIMARY KEY,
    name               VARCHAR(100) NOT NULL UNIQUE,
    power              INTEGER NOT NULL,
    ability            VARCHAR(255) NOT NULL,
    creation_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Columnas

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `specie_id` | BIGSERIAL | PRIMARY KEY | Identificador único de la especie (Auto-incremental) |
| `name` | VARCHAR(100) | NOT NULL, UNIQUE | Nombre de la especie (debe ser único) |
| `power` | INTEGER | NOT NULL, > 0 | Nivel de poder de la especie (debe ser positivo) |
| `ability` | VARCHAR(255) | NOT NULL | Habilidad especial de la especie |
| `creation_date` | TIMESTAMP | NOT NULL | Fecha de creación (se establece automáticamente) |

#### Índices
- `idx_specie_name` - Para búsquedas por nombre
- `idx_specie_power` - Para ordenamiento por poder
- `idx_specie_creation_date` - Para búsquedas por fecha de creación

#### Constraints
- PK: `specie_id`
- UK: `name` (único)
- CHECK: `power > 0`

---

### 2. Tabla: `COMBATS` (Combates/Batallas)

#### Propósito
Registra los combates entre dos especies y determina el ganador.

#### Estructura
```sql
CREATE TABLE combats (
    combat_id              BIGSERIAL PRIMARY KEY,
    first_contender_id     BIGINT NOT NULL,
    second_contender_id    BIGINT NOT NULL,
    winner_id              BIGINT NOT NULL,
    creation_date          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Columnas

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `combat_id` | BIGSERIAL | PRIMARY KEY | Identificador único del combate |
| `first_contender_id` | BIGINT | NOT NULL, FK | ID de la primera especie contendiente |
| `second_contender_id` | BIGINT | NOT NULL, FK | ID de la segunda especie contendiente |
| `winner_id` | BIGINT | NOT NULL, FK | ID de la especie ganadora |
| `creation_date` | TIMESTAMP | NOT NULL | Fecha y hora en que ocurrió el combate |

#### Claves Foráneas
- FK `first_contender_id` → `especies.specie_id` (ON DELETE RESTRICT)
- FK `second_contender_id` → `especies.specie_id` (ON DELETE RESTRICT)
- FK `winner_id` → `especies.specie_id` (ON DELETE RESTRICT)

#### Índices
- `idx_winner_id` - Para búsquedas de ganadores
- `idx_firstContender` - Para búsquedas de primer contendiente
- `idx_secondContender` - Para búsquedas de segundo contendiente
- `idx_combat_creation_date` - Para búsquedas por fecha

#### Constraints
- PK: `combat_id`
- CHECK: `first_contender_id != second_contender_id` (contendientes diferentes)
- CHECK: `winner_id IN (first_contender_id, second_contender_id)` (ganador debe ser uno de los contendientes)

---

### 3. Tabla: `RANKINGS` (Rankings)

#### Propósito
Registra el número de victorias de cada especie en el torneo.

#### Estructura
```sql
CREATE TABLE rankings (
    ranking_id      BIGSERIAL PRIMARY KEY,
    specie_id       BIGINT NOT NULL UNIQUE,
    victorias       BIGINT NOT NULL DEFAULT 0
);
```

#### Columnas

| Columna | Tipo | Constraints | Descripción |
|---------|------|-------------|-------------|
| `ranking_id` | BIGSERIAL | PRIMARY KEY | Identificador único del ranking |
| `specie_id` | BIGINT | NOT NULL, UNIQUE, FK | ID de la especie (una entrada por especie) |
| `victorias` | BIGINT | NOT NULL, >= 0 | Número de batallas ganadadas |

#### Claves Foráneas
- FK `specie_id` → `especies.specie_id` (ON DELETE CASCADE)

#### Índices
- `idx_victories` - Para ordenamiento por número de victorias
- `idx_specie_id` - Para búsquedas por especie
- `idx_ranking_specie` - Índice adicional para la clave foránea

#### Constraints
- PK: `ranking_id`
- UK: `specie_id` (una entrada por especie)
- CHECK: `victorias >= 0` (no puede ser negativo)
- FK: `specie_id` (eliminación en cascada)

---

## 📈 VISTAS (Views)

### Vista: `vw_species_with_victories`

**Propósito**: Mostrar todas las especies con su conteo de victorias, ordenadas por ranking.

```sql
SELECT 
    e.specie_id,
    e.name,
    e.power,
    e.ability,
    COALESCE(r.victorias, 0) AS victories,
    e.creation_date
FROM especies e
LEFT JOIN rankings r ON e.specie_id = r.specie_id
ORDER BY COALESCE(r.victorias, 0) DESC, e.power DESC;
```

---

### Vista: `vw_battle_history`

**Propósito**: Mostrar el historial de batallas con nombres de especies.

```sql
SELECT 
    c.combat_id,
    c.creation_date,
    e1.name AS first_contender_name,
    e1.power AS first_contender_power,
    e2.name AS second_contender_name,
    e2.power AS second_contender_power,
    winner.name AS winner_name
FROM combats c
JOIN especies e1 ON c.first_contender_id = e1.specie_id
JOIN especies e2 ON c.second_contender_id = e2.specie_id
JOIN especies winner ON c.winner_id = winner.specie_id
ORDER BY c.creation_date DESC;
```

---

## 🔧 PROCEDIMIENTOS ALMACENADOS

### Función: `sp_record_battle`

**Propósito**: Registrar un nuevo combate y actualizar automáticamente el ranking.

**Parámetros de Entrada**:
- `p_first_contender_id` (BIGINT) - ID del primer contendiente
- `p_second_contender_id` (BIGINT) - ID del segundo contendiente
- `p_winner_id` (BIGINT) - ID del ganador

**Parámetro de Salida**:
- `p_combat_id` (BIGINT) - ID del combate registrado

**Funcionamiento**:
1. Inserta un nuevo registro en la tabla `combats`
2. Incrementa el contador de `victorias` para el ganador en `rankings`
3. Si el ranking no existe, lo crea

---

## 📊 RELACIONES ENTRE TABLAS

```
    ┌─────────────────────────┐
    │      especies           │
    │ ─────────────────────── │
    │ specie_id (PK)          │
    │ name                    │
    │ power                   │
    │ ability                 │
    │ creation_date           │
    └──────────┬──────────────┘
               │
       ┌───────┼───────┬────────────┐
       │       │       │            │
       │       │       │            │
    ┌──▼──┐┌──▼──┐┌───▼────┐    ┌──▼──────┐
    │ FK  ││ FK  ││  FK    │    │  FK     │
    │  1  ││  2  ││Winner  │    │ Ranking │
    │     ││     ││        │    │         │
    └──┬──┘└──┬──┘└───┬────┘    └──┬──────┘
       │      │       │           │
    ┌──▼──────▼───────▼────┐   ┌──▼──────────┐
    │    combats           │   │  rankings   │
    │ ─────────────────────│   │ ─────────── │
    │ combat_id (PK)       │   │ ranking_id  │
    │ first_contender_id   │   │ specie_id   │
    │ second_contender_id  │   │ victorias   │
    │ winner_id            │   └─────────────┘
    │ creation_date        │
    └──────────────────────┘
```

---

## 🚀 CÓMO USAR

### 1. Crear la Base de Datos

```bash
# Conectarse a PostgreSQL
psql -U postgres

# Ejecutar el script DDL
\i DDL_POSTGRESQL.sql

# O desde línea de comandos
psql -U postgres -f DDL_POSTGRESQL.sql
```

### 2. Verificar la Creación

```sql
-- Ver todas las tablas
\dt

-- Ver estructura de una tabla
\d+ especies

-- Ver todas las vistas
\dv

-- Ver todos los índices
\di
```

### 3. Insertar Datos de Prueba

```sql
INSERT INTO especies (name, power, ability) VALUES
    ('Klingons', 95, 'Hand-to-hand combat expertise'),
    ('Vulcans', 85, 'Mind meld and logic'),
    ('Ewoks', 60, 'Forest camouflage and ingenuity'),
    ('Wookiees', 90, 'Strength and roar');

INSERT INTO rankings (specie_id, victorias) VALUES
    (1, 0),
    (2, 0),
    (3, 0),
    (4, 0);
```

### 4. Consultar Datos

```sql
-- Ver todas las especies con sus victorias
SELECT * FROM vw_species_with_victories;

-- Ver historial de combates
SELECT * FROM vw_battle_history;

-- Registrar un combate
SELECT * FROM sp_record_battle(1, 2, 1);
```

---

## 🔐 SEGURIDAD Y CONSTRAINTS

### Integridad Referencial
- Las claves foráneas en `combats` apuntan a `especies.specie_id`
- La clave foránea en `rankings` apunta a `especies.specie_id` con ON DELETE CASCADE

### Validaciones de Negocio
- El poder de una especie debe ser positivo (> 0)
- Las dos especies en un combate deben ser diferentes
- El ganador debe ser uno de los dos contendientes
- No puede haber dos rankings para la misma especie

### Restricciones de Unicidad
- Cada especie tiene un nombre único
- Cada especie tiene un solo ranking

---

## 📈 ÍNDICES

### Tabla ESPECIES
- `name` - Para búsquedas y validación de unicidad
- `power` - Para ordenamiento de rankings
- `creation_date` - Para búsquedas temporales

### Tabla COMBATS
- `winner_id` - Para encontrar victorias de una especie
- `first_contender_id` - Para encontrar combates de primer contendiente
- `second_contender_id` - Para encontrar combates de segundo contendiente
- `creation_date` - Para búsquedas temporales

### Tabla RANKINGS
- `victorias` - Para crear rankings ordenados
- `specie_id` - Para búsquedas y joins

---

## 📝 NOTAS IMPORTANTES

1. **BIGSERIAL vs SERIAL**: Se usa BIGSERIAL (8 bytes) para permitir crecimiento futuro
2. **UTF-8**: La base de datos usa UTF-8 para soporte de caracteres especiales
3. **Timestamps**: Todos los `creation_date` se establecen automáticamente
4. **Foreign Keys**: Incluyen ON DELETE RESTRICT para prevenir eliminaciones accidentales
5. **Vistas**: Simplemente disponibles para consultas frecuentes
6. **Procedimientos**: La función `sp_record_battle` mantiene la integridad de datos

---

## 🛠️ MANTENIMIENTO

### Backup
```bash
pg_dump -U postgres galactic_tournament > galactic_tournament_backup.sql
```

### Restore
```bash
psql -U postgres < galactic_tournament_backup.sql
```

### Vaciar Tablas (Preservar Estructura)
```sql
TRUNCATE TABLE combats CASCADE;
TRUNCATE TABLE rankings CASCADE;
TRUNCATE TABLE especies CASCADE;
```

---

## 📊 Conversión de Tipos JPA a PostgreSQL

| JPA | PostgreSQL | Notas |
|-----|-----------|-------|
| `BIGSERIAL` | `BIGSERIAL` | Auto-incremental, 8 bytes |
| `@GeneratedValue(IDENTITY)` | `BIGSERIAL` | Genera automáticamente |
| `VARCHAR(n)` | `VARCHAR(n)` | Cadena de longitud máxima |
| `INTEGER` | `INTEGER` | Número entero, 4 bytes |
| `BIGINT` | `BIGINT` | Número entero largo, 8 bytes |
| `LocalDateTime` | `TIMESTAMP` | Fecha y hora sin zona |
| `@NotNull` | `NOT NULL` | Columna obligatoria |
| `@Unique` | `UNIQUE` | Valores únicos |
| `@OneToMany` | `FOREIGN KEY` | Relación uno a muchos |

---

## ✅ Archivos Relacionados

- `DDL_POSTGRESQL.sql` - Script SQL completo
- Entidades JPA en `src/main/java/entity/`
- `application.yaml` - Configuración de conexión a BD

---

**¡Listo!** La base de datos está completamente configurada y documentada. 🎉

