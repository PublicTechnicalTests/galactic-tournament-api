# Galactic Tournament API - Feature 03 Test Examples

## Endpoint: Start a Battle

Base URL: `http://localhost:8080/api/v1/battles`

### 1. Start battle between different power species
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": 1,
    "id_contendiente_2": 2
  }'
```

Response (201 Created):
```json
{
  "id_combate": 1,
  "id_contendiente_1": 1,
  "id_contendiente_2": 2,
  "id_ganador": 2,
  "nombre_ganador": "Klingon",
  "poder_ganador": 150,
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

### 2. Start battle between same power species (alphabetical winner)
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": 3,
    "id_contendiente_2": 4
  }'
```

Response (201 Created) - Andorian comes first alphabetically:
```json
{
  "id_combate": 2,
  "id_contendiente_1": 3,
  "id_contendiente_2": 4,
  "id_ganador": 3,
  "nombre_ganador": "Andorian",
  "poder_ganador": 100,
  "fecha_creacion": "2026-06-01T10:05:00"
}
```

### 3. Start battle with missing first contender
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": 999,
    "id_contendiente_2": 1
  }'
```

Response (404 Not Found):
```json
{
  "timestamp": "2026-06-01T10:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with ID: 999",
  "path": "/api/v1/battles"
}
```

### 4. Start battle with missing second contender
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": 1,
    "id_contendiente_2": 999
  }'
```

Response (404 Not Found):
```json
{
  "timestamp": "2026-06-01T10:11:00",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with ID: 999",
  "path": "/api/v1/battles"
}
```

### 5. Start battle with invalid request (missing field)
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": 1
  }'
```

Response (400 Bad Request):
```json
{
  "timestamp": "2026-06-01T10:12:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validation_errors": [
    {
      "field": "idContendiente2",
      "message": "Second contender ID must not be null",
      "rejected_value": null
    }
  ]
}
```

### 6. Start battle with null values
```bash
curl -X POST http://localhost:8080/api/v1/battles \
  -H "Content-Type: application/json" \
  -d '{
    "id_contendiente_1": null,
    "id_contendiente_2": 2
  }'
```

Response (400 Bad Request):
```json
{
  "timestamp": "2026-06-01T10:13:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validation_errors": [
    {
      "field": "idContendiente1",
      "message": "First contender ID must not be null",
      "rejected_value": null
    }
  ]
}
```

## Battle Logic Examples

### Example 1: Winner Determined by Power
```
Species A: Vulcan (power: 100)
Species B: Klingon (power: 150)

Battle Result: Klingon WINS
Reason: 150 > 100 (higher power)
```

### Example 2: Winner Determined Alphabetically
```
Species A: Betazoid (power: 100)
Species B: Andorian (power: 100)

Battle Result: Andorian WINS
Reason: Same power, "Andorian" < "Betazoid" (alphabetically first)
```

### Example 3: Complex Power Comparison
```
Species A: Ferengi (power: 75)
Species B: Romulan (power: 125)

Battle Result: Romulan WINS
Reason: 125 > 75 (higher power)
```

## Response Fields Reference

| Field | Type | Description |
|-------|------|-------------|
| `id_combate` | long | Unique battle identifier |
| `id_contendiente_1` | long | First contender species ID |
| `id_contendiente_2` | long | Second contender species ID |
| `id_ganador` | long | Winning species ID |
| `nombre_ganador` | string | Winning species name |
| `poder_ganador` | integer | Winning species power level |
| `fecha_creacion` | datetime | Battle creation timestamp |

## Error Response Reference

| Status | Error | Meaning |
|--------|-------|---------|
| 201 | N/A | Battle successfully created |
| 400 | Validation Failed | Invalid request (missing/null fields) |
| 404 | Not Found | Species not found |
| 500 | Internal Server Error | Unexpected server error |

