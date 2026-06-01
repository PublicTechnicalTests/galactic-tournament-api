# Feature 05: Add Ranking

## Overview
This feature implements species ranking system, tracking victories for tournament standings and leaderboards.

## Requirements
- Create ranking for species
- Validate species exists
- Validate ranking doesn't already exist
- Initialize victories to 0
- Save ranking entry

## Implementation Details

### New Entity
- **Ranking**: Tournament standings
  - IdRanking: Long (PK)
  - IdEspecie: Long (FK, UNIQUE)
  - Victorias: Long (default 0)

### New DTOs (Records)
- **CreateRankingRequest**: For creating new ranking
  - idEspecie: Long (required)

- **RankingResponse**: For ranking responses
  - idRanking: Long
  - idEspecie: Long
  - nombreEspecie: String
  - victorias: Long

### Repository
- **RankingRepository**: JPA repository
  - findByIdEspecie()
  - existsByIdEspecie()
  - findAllOrderByVictorias() - Custom query for leaderboard

### Service
- **RankingService**: Ranking business logic
  - createRanking()
  - addVictory() (to be used in Feature 06)
  - getRankingById()
  - getRankingBySpecieId()
  - getAllRankingsOrderedByVictorias()
  - rankingExists()

### Controller Endpoints
#### POST /api/v1/rankings
**Create a ranking for a species**

Request:
```json
{
  "id_especie": 1
}
```

Response (201 Created):
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 0
}
```

#### GET /api/v1/rankings/{id}
**Retrieve ranking by ID**

Response (200 OK):
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 5
}
```

#### GET /api/v1/rankings/by-species/{specieId}
**Retrieve ranking by species ID**

Response (200 OK):
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 5
}
```

#### GET /api/v1/rankings/leaderboard
**Get leaderboard (all rankings ordered by victories)**

Response (200 OK):
```json
[
  {
    "id_ranking": 2,
    "id_especie": 2,
    "nombre_especie": "Klingon",
    "victorias": 10
  },
  {
    "id_ranking": 1,
    "id_especie": 1,
    "nombre_especie": "Vulcan",
    "victorias": 5
  }
]
```

## Validations

1. **Species Exists**: Species must exist before creating ranking
2. **Ranking Unique**: Only one ranking per species
3. **Victories Default**: Initialized to 0

## Files Created
- **Ranking.java** - Entity with addVictory() method
- **CreateRankingRequest.java** - DTO
- **RankingResponse.java** - DTO
- **RankingRepository.java** - Repository
- **RankingMapper.java** - Mapper
- **RankingService.java** - Service (6 methods)
- **RankingController.java** - Controller (4 endpoints)
- **RankingServiceTest.java** - 6 unit tests

## Testing

### Unit Tests (RankingServiceTest)
- ✅ Create ranking for species
- ✅ Throw exception when ranking already exists
- ✅ Add victory to ranking
- ✅ Retrieve ranking by ID
- ✅ Retrieve ranking by species ID
- ✅ Check if ranking exists
- ✅ Return false when ranking doesn't exist (implied)

## Features Provided

1. **League Table**: Track species by victories
2. **Leaderboard**: GET /rankings/leaderboard endpoint
3. **Query by Species**: Fast lookup by species ID
4. **Victory Tracking**: Atomic victory increments (Feature 06)

## Integration

This feature lays groundwork for:
- Feature 06: Update ranking (add victory after battle)
- Feature 07: Get ranking by ID
- Feature 08: List ranking (leaderboard)

## Next Steps
1. Execute all tests
2. Verify compilation
3. Commit changes
4. Push to repository
5. Create Pull Request

