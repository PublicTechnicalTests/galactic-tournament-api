# Feature 01: Add Species

## Overview
This feature implements the first functionality of the Galactic Tournament API: adding and managing galactic species.

## Requirements
- Add a new species to the tournament
- List all registered species
- Retrieve species by ID
- Retrieve species by name

## Implementation Details

### Entities
- **Especie**: Main entity representing a galactic species with properties:
  - `idEspecie`: Unique identifier (Primary Key)
  - `nombre`: Species name (UNIQUE, NOT NULL)
  - `poder`: Power level (NOT NULL, POSITIVE)
  - `habilidad`: Special ability (NOT NULL)
  - `fechaCreacion`: Creation timestamp (DEFAULT CURRENT_TIMESTAMP)

### DTOs
- **CreateSpecieRequest**: Record for creating a new species (with validation)
- **UpdateSpecieRequest**: Record for updating species (power and ability)
- **SpecieResponse**: Record for API responses

### API Endpoints

#### 1. Create Species
```
POST /api/v1/species
Content-Type: application/json

{
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control"
}

Response: 201 Created
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

#### 2. Get All Species
```
GET /api/v1/species

Response: 200 OK
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

#### 3. Get Species by ID
```
GET /api/v1/species/{id}

Response: 200 OK
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

#### 4. Get Species by Name
```
GET /api/v1/species/by-name/{nombre}

Response: 200 OK
{
  "id_especie": 1,
  "nombre": "Vulcan",
  "poder": 100,
  "habilidad": "Mind meditation and control",
  "fecha_creacion": "2026-06-01T10:00:00"
}
```

## Error Handling

### 409 Conflict
When attempting to create a duplicate species (same name):
```json
{
  "timestamp": "2026-06-01T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "A species with the name 'Vulcan' already exists",
  "path": "/api/v1/species"
}
```

### 404 Not Found
When species ID doesn't exist:
```json
{
  "timestamp": "2026-06-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with ID: 999",
  "path": "/api/v1/species/999"
}
```

### 400 Bad Request
Validation errors:
```json
{
  "timestamp": "2026-06-01T10:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "path": "/api/v1/species",
  "validation_errors": [
    {
      "field": "nombre",
      "message": "Species name must not be blank",
      "rejected_value": ""
    },
    {
      "field": "poder",
      "message": "Species power must be positive",
      "rejected_value": -10
    }
  ]
}
```

## Testing

### Unit Tests
- `EspecieServiceTest`: Tests for service business logic
  - Create species successfully
  - Prevent duplicate species
  - Retrieve by ID
  - Retrieve by name
  - Get all species
  - Existence checks

### Integration Tests
- `EspecieControllerIntegrationTest`: Tests for REST endpoints
  - HTTP 201 Created on successful creation
  - HTTP 400 Bad Request on validation failure
  - HTTP 409 Conflict on duplicate
  - HTTP 200 OK on retrieval

### Repository Tests
- `EspecieRepositoryTest`: Tests for data persistence layer
  - Save and retrieve
  - Find by name
  - Unique constraint enforcement
  - Delete operations

## Technology Stack
- **Java**: 25
- **Spring Boot**: 4.0.6
- **Spring Data JPA**: For data persistence
- **PostgreSQL**: Production database
- **Lombok**: For reducing boilerplate
- **Swagger/OpenAPI**: API documentation
- **JUnit 5**: Unit testing
- **Mockito**: Mocking framework
- **TestContainers**: Integration testing with real database

## Files Created
```
src/main/java/com/technicaltests/mv/galactictournamentapi/
├── entity/
│   └── Especie.java
├── dto/
│   ├── CreateSpecieRequest.java
│   ├── UpdateSpecieRequest.java
│   └── SpecieResponse.java
├── repository/
│   └── EspecieRepository.java
├── service/
│   └── EspecieService.java
├── controller/
│   └── EspecieController.java
├── mapper/
│   └── EspecieMapper.java
└── exception/
    ├── SpecieNotFoundException.java
    ├── SpecieAlreadyExistsException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java

src/test/java/com/technicaltests/mv/galactictournamentapi/
├── service/
│   └── EspecieServiceTest.java
├── controller/
│   └── EspecieControllerIntegrationTest.java
├── repository/
│   └── EspecieRepositoryTest.java
└── TestContainersConfiguration.java

src/test/resources/
└── application-test.yaml
```

## Best Practices Applied
1. **Records for DTOs**: Modern Java 25 approach for immutable data objects
2. **Validation at Boundaries**: Jakarta Validation annotations on DTOs
3. **Global Exception Handling**: Centralized error response formatting
4. **Service Layer**: Business logic separated from REST layer
5. **Repository Pattern**: Data access abstraction
6. **Mapper Pattern**: Separation of entities and DTOs
7. **Comprehensive Documentation**: Javadoc for all public classes and methods
8. **Logging**: Structured logging with SLF4J
9. **Testing**: Unit, integration, and repository tests
10. **API Documentation**: Swagger annotations for OpenAPI/Swagger UI

## Next Steps
- Deploy to development environment
- Create Pull Request to development branch
- Code review and testing
- Merge to development
- Start Feature 02: List Species (with filtering and pagination)

