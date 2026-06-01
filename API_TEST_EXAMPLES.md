# Galactic Tournament API - Feature 01 Test Examples

## Prerequisites
- API running on http://localhost:8080/api
- cURL or Postman installed

## Test Examples

### 1. Create Species (Success)
```bash
curl -X POST http://localhost:8080/api/v1/species \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Vulcan",
    "poder": 100,
    "habilidad": "Mind meditation and control"
  }'
```

Response:
```json
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

### 2. Create Species (Missing Required Field)
```bash
curl -X POST http://localhost:8080/api/v1/species \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Klingon",
    "poder": 120
  }'
```

Response (400):
```json
{
  "timestamp": "2026-06-01T10:05:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validation_errors": [
    {
      "field": "habilidad",
      "message": "Species ability must not be blank",
      "rejected_value": null
    }
  ]
}
```

### 3. Create Duplicate Species
```bash
curl -X POST http://localhost:8080/api/v1/species \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Vulcan",
    "poder": 80,
    "habilidad": "Different ability"
  }'
```

Response (409):
```json
{
  "timestamp": "2026-06-01T10:05:30",
  "status": 409,
  "error": "Conflict",
  "message": "A species with the name 'Vulcan' already exists",
  "path": "/api/v1/species"
}
```

### 4. Get All Species
```bash
curl -X GET http://localhost:8080/api/v1/species \
  -H "Content-Type: application/json"
```

Response:
```json
[
  {
    "id_especie": 1,
    "nombre": "Vulcan",
    "poder": 100,
    "habilidad": "Mind meditation and control",
    "fecha_creacion": "2026-06-01T10:00:00"
  }
]
```

### 5. Get Species by ID
```bash
curl -X GET http://localhost:8080/api/v1/species/1 \
  -H "Content-Type: application/json"
```

Response:
```json
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

### 6. Get Non-existent Species by ID
```bash
curl -X GET http://localhost:8080/api/v1/species/999 \
  -H "Content-Type: application/json"
```

Response (404):
```json
{
  "timestamp": "2026-06-01T10:06:00",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with ID: 999",
  "path": "/api/v1/species/999"
}
```

### 7. Get Species by Name
```bash
curl -X GET http://localhost:8080/api/v1/species/by-name/Vulcan \
  -H "Content-Type: application/json"
```

Response:
```json
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

### 8. Get Species by Non-existent Name
```bash
curl -X GET http://localhost:8080/api/v1/species/by-name/Klingon \
  -H "Content-Type: application/json"
```

Response (404):
```json
{
  "timestamp": "2026-06-01T10:06:30",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with name: Klingon",
  "path": "/api/v1/species/by-name/Klingon"
}
```

## Swagger/OpenAPI Documentation

Access the interactive API documentation at:
```
http://localhost:8080/api/swagger-ui.html
```

Or retrieve the OpenAPI spec at:
```
http://localhost:8080/api/v3/api-docs
```

