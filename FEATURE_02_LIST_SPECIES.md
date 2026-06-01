# Feature 02: List Species with Pagination and Filtering

## Overview
This feature extends the Galactic Tournament API with advanced listing capabilities including pagination, filtering, and sorting of species.

## Requirements
- List all registered species with pagination
- Filter species by name (search term)
- Filter species by power level range
- Sort by different fields (nombre, poder, fechaCreacion)
- Support both ascending and descending order
- Return metadata about current page

## Implementation Details

### New DTOs (Records)
- **SpecieListQuery**: Request parameters for list queries
  ```java
  SpecieListQuery(page, size, sortBy, sortDirection, searchTerm, minPower, maxPower)
  ```

- **PaginatedSpecieResponse**: Response with pagination metadata
  ```java
  PaginatedSpecieResponse(content, totalElements, totalPages, currentPage, 
                         pageSize, hasNextPage, hasPreviousPage)
  ```

### JPA Specification
- **EspecieSpecification**: Dynamic query builder for complex filters
  - Like-based search on species name
  - Range filters for power level

### Repository Enhancement
- Extended with `JpaSpecificationExecutor<Especie>`
- Supports complex filters and pagination

### Service Enhancements
- **listSpecies(SpecieListQuery)**: Main pagination method
  - Default: page=0, size=20, sortBy=poder, sortDirection=DESC
  - Maximum page size enforced at 100
  - Applies filters via Specification

### Controller Endpoints

#### GET /api/v1/species/list
**List species with pagination and filtering**
```
Query Parameters:
- page (default: 0, min: 0)
- size (default: 20, max: 100)
- sortBy (default: poder) - options: nombre, poder, fechaCreacion
- sortDirection (default: DESC) - options: ASC, DESC
- searchTerm (optional) - search in species name
- minPower (optional) - minimum power level
- maxPower (optional) - maximum power level

Response: 200 OK
{
  "content": [ /* list of species */ ],
  "total_elements": 100,
  "total_pages": 5,
  "current_page": 0,
  "page_size": 20,
  "has_next_page": true,
  "has_previous_page": false
}
```

## API Examples

### Example 1: List first page (default)
```
GET /api/v1/species/list
```

### Example 2: Search with filter
```
GET /api/v1/species/list?searchTerm=Vulcan&sortBy=poder&sortDirection=DESC
```

### Example 3: Power range filter
```
GET /api/v1/species/list?minPower=100&maxPower=200&page=0&size=10
```

### Example 4: Sorted by name
```
GET /api/v1/species/list?sortBy=nombre&sortDirection=ASC
```

### Example 5: Complex query
```
GET /api/v1/species/list?searchTerm=sa&minPower=50&maxPower=150&sortBy=poder&sortDirection=ASC&page=1&size=15
```

## Error Handling

### 400 Bad Request
Invalid pagination parameters:
```json
{
  "timestamp": "2026-06-01T10:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Page must be >= 0"
}
```

## Testing

### Unit Tests (EspecieServiceListTest)
- ✅ List with default pagination
- ✅ Apply search filters
- ✅ Apply power range filters
- ✅ Enforce maximum page size
- ✅ Handle empty results
- ✅ Calculate pagination metadata correctly

## Files Changed
- **Modified**: EspecieRepository.java (added JpaSpecificationExecutor)
- **Modified**: EspecieService.java (added listSpecies method)
- **Modified**: EspecieController.java (added /list endpoint)
- **Created**: SpecieListQuery.java (DTO for query params)
- **Created**: PaginatedSpecieResponse.java (DTO for paginated response)
- **Created**: EspecieSpecification.java (JPA specification for filters)
- **Created**: EspecieServiceListTest.java (unit tests)

## Best Practices Applied
- Specification pattern for dynamic queries
- Default values for pagination
- Maximum page size enforcement
- Comprehensive filtering support
- Pagination metadata calculation
- Descriptive logging
- Javadoc documentation
- Swagger/OpenAPI annotations

## Next Steps
1. ✅ Execute all tests
2. ✅ Verify compilation
3. ✅ Commit changes
4. ✅ Push to repository
5. ✅ Create Pull Request to development

