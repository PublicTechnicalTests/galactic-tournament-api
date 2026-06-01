# Galactic Tournament API - Feature 02 Test Examples

## Endpoint: List Species with Pagination and Filtering

Base URL: `http://localhost:8080/api/v1/species/list`

### 1. Default pagination (first page)
```bash
curl -X GET http://localhost:8080/api/v1/species/list
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 2,
      "nombre": "Klingon",
      "poder": 150,
      "habilidad": "Warrior strength",
      "fecha_creacion": "2026-06-01T10:05:00"
    },
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 2,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 2. Search by name
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?searchTerm=Vulcan"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 1,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 3. Filter by power range
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?minPower=50&maxPower=120"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 1,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 4. Sort by name (ascending)
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?sortBy=nombre&sortDirection=ASC"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 2,
      "nombre": "Klingon",
      "poder": 150,
      "habilidad": "Warrior strength",
      "fecha_creacion": "2026-06-01T10:05:00"
    },
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 2,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 5. Sort by power (descending)
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?sortBy=poder&sortDirection=DESC"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 2,
      "nombre": "Klingon",
      "poder": 150,
      "habilidad": "Warrior strength",
      "fecha_creacion": "2026-06-01T10:05:00"
    },
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 2,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 6. Pagination (page 1, size 1)
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?page=1&size=1"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 2,
  "total_pages": 2,
  "current_page": 1,
  "page_size": 1,
  "has_next_page": false,
  "has_previous_page": true
}
```

### 7. Complex query (search + filter + sort)
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?searchTerm=an&minPower=50&maxPower=200&sortBy=poder&sortDirection=DESC&page=0&size=10"
```

Response (200 OK):
```json
{
  "content": [
    {
      "id_especie": 2,
      "nombre": "Klingon",
      "poder": 150,
      "habilidad": "Warrior strength",
      "fecha_creacion": "2026-06-01T10:05:00"
    },
    {
      "id_especie": 1,
      "nombre": "Vulcan",
      "poder": 100,
      "habilidad": "Mind meditation",
      "fecha_creacion": "2026-06-01T10:00:00"
    }
  ],
  "total_elements": 2,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 10,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 8. Empty search results
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?searchTerm=NonExistent"
```

Response (200 OK):
```json
{
  "content": [],
  "total_elements": 0,
  "total_pages": 0,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": false,
  "has_previous_page": false
}
```

### 9. Invalid page parameter
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?page=-1"
```

Response (400 Bad Request):
```json
{
  "timestamp": "2026-06-01T12:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validation_errors": [
    {
      "field": "page",
      "message": "Page must be >= 0",
      "rejected_value": -1
    }
  ]
}
```

### 10. Max page size is enforced
```bash
curl -X GET "http://localhost:8080/api/v1/species/list?size=500"
```

Response (200 OK) - Note: page_size will be capped at 100:
```json
{
  "content": [ /* ... */ ],
  "total_elements": 2,
  "total_pages": 1,
  "current_page": 0,
  "page_size": 100,
  "has_next_page": false,
  "has_previous_page": false
}
```

## Query Parameters Reference

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (0-indexed) |
| `size` | integer | 20 | Page size (max 100) |
| `sortBy` | string | poder | Sort field (nombre, poder, fechaCreacion) |
| `sortDirection` | string | DESC | Sort direction (ASC, DESC) |
| `searchTerm` | string | optional | Search in species name |
| `minPower` | integer | optional | Minimum power level filter |
| `maxPower` | integer | optional | Maximum power level filter |

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| `content` | array | List of species in current page |
| `total_elements` | long | Total number of species |
| `total_pages` | integer | Total number of pages |
| `current_page` | integer | Current page number (0-indexed) |
| `page_size` | integer | Size of current page |
| `has_next_page` | boolean | Whether there is a next page |
| `has_previous_page` | boolean | Whether there is a previous page |

