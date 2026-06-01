# Galactic Tournament API - Features 04 & 05 Documentation

## API Endpoints

### Battles (Feature 04)

#### 1. Start Battle with Automatic Winner Determination
**POST** `/api/v1/battles`

Initiates a battle between two species. The winner is determined automatically based on:
- Power level (higher power wins)
- If power is equal, alphabetical order (first alphabetically wins)

**Request Body:**
```json
{
  "id_contendiente_1": 1,
  "id_contendiente_2": 2
}
```

**Response (201 Created):**
```json
{
  "id_combate": 1,
  "id_contendiente_1": 1,
  "id_contendiente_2": 2,
  "id_ganador": 1,
  "nombre_ganador": "Vulcan",
  "poder_ganador": 100,
  "fecha_creacion": "2026-06-01T10:30:00"
}
```

**Error Cases:**
- 404: Species not found
- 400: Invalid request

---

#### 2. Add Battle Result Manually
**POST** `/api/v1/battles/result`

Adds a battle result with explicitly specified winner (no automatic determination).

**Request Body:**
```json
{
  "id_contendiente_1": 1,
  "id_contendiente_2": 2,
  "id_ganador": 1
}
```

**Response (201 Created):**
```json
{
  "id_combate": 2,
  "id_contendiente_1": 1,
  "id_contendiente_2": 2,
  "id_ganador": 1,
  "nombre_ganador": "Vulcan",
  "poder_ganador": 100,
  "fecha_creacion": "2026-06-01T10:31:00"
}
```

**Validations:**
- Winner must be one of the contenders
- Contenders must be different species
- Both species must exist

**Error Cases:**
- 404: Species not found
- 400: Winner is not one of contenders
- 400: Contenders are the same

---

### Rankings (Feature 05)

#### 3. Create Ranking for Species
**POST** `/api/v1/rankings`

Creates a new ranking entry for a species, initialized with 0 victories.

**Request Body:**
```json
{
  "id_especie": 1
}
```

**Response (201 Created):**
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 0
}
```

**Validations:**
- Species must exist
- Ranking must not already exist for this species

**Error Cases:**
- 404: Species not found
- 400: Ranking already exists for this species

---

#### 4. Get Ranking by ID
**GET** `/api/v1/rankings/{id}`

Retrieves ranking information by ranking ID.

**Response (200 OK):**
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 5
}
```

**Error Cases:**
- 404: Ranking not found

---

#### 5. Get Ranking by Species ID
**GET** `/api/v1/rankings/by-species/{specieId}`

Retrieves ranking information for a specific species.

**Response (200 OK):**
```json
{
  "id_ranking": 1,
  "id_especie": 1,
  "nombre_especie": "Vulcan",
  "victorias": 5
}
```

**Error Cases:**
- 404: Ranking not found for this species

---

#### 6. Get Leaderboard
**GET** `/api/v1/rankings/leaderboard`

Retrieves all rankings ordered by victories (highest first).

**Response (200 OK):**
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
  },
  {
    "id_ranking": 3,
    "id_especie": 3,
    "nombre_especie": "Romulan",
    "victorias": 3
  }
]
```

---

## Data Models

### Battle (Combate)
```
Entity: combate
Columns:
  - id_combate: Long (PK, AI)
  - id_contendiente_1: Long (FK to especies.id_especie)
  - id_contendiente_2: Long (FK to especies.id_especie)
  - id_ganador: Long (FK to especies.id_especie)
  - fecha_creacion: LocalDateTime (not null, auto-set)

Indexes:
  - idx_id_ganador
  - idx_contendiente1
  - idx_contendiente2
```

### Ranking
```
Entity: ranking
Columns:
  - id_ranking: Long (PK, AI)
  - id_especie: Long (FK to especies.id_especie, UNIQUE)
  - victorias: Long (default 0, not null)

Indexes:
  - idx_victorias (DESC)
  - idx_id_especie

Constraints:
  - uk_ranking_especie: UNIQUE(id_especie)
```

---

## Integration Flow

### Battle + Ranking Integration (Feature 06)
After a battle is completed (Feature 04), the system should:
1. Record the battle result in the `combate` table
2. Update the winner's ranking in the `ranking` table
3. Increment the `victorias` count for the winner

**Future Sequence:**
```
POST /battles/result
  ↓
Save Combate entity
  ↓
Call RankingService.addVictory(idGanador)
  ↓
Update Ranking entity (victorias++)
  ↓
Return combined response
```

---

## Error Response Format

All errors follow this standard format:

```json
{
  "timestamp": "2026-06-01T10:32:00",
  "status": 404,
  "error": "Not Found",
  "message": "Species not found with ID: 999",
  "path": "/api/v1/battles/result"
}
```

---

## Testing

### Unit Test Coverage

**CombateServiceAddResultTest** (4 tests)
- ✅ Add battle result with valid winner
- ✅ Throw exception when winner not one of contenders
- ✅ Throw exception when contenders are same
- ✅ Allow either contender as winner

**RankingServiceTest** (7 tests)
- ✅ Create ranking for species
- ✅ Throw exception when ranking already exists
- ✅ Add victory to ranking
- ✅ Retrieve ranking by ID
- ✅ Retrieve ranking by species ID
- ✅ Check if ranking exists
- ✅ Return false when ranking doesn't exist

---

## Implementation Summary

| Component | Status | Lines |
|-----------|--------|-------|
| DTOs | ✅ Complete | 5 files |
| Entities | ✅ Complete | 2 files |
| Repositories | ✅ Complete | 2 files |
| Mappers | ✅ Complete | 2 files |
| Services | ✅ Complete | 2 files |
| Controllers | ✅ Complete | 2 files |
| Tests | ✅ Complete | 2 files |
| Documentation | ✅ Complete | 2 files |

**Total:** 19 files, ~2000+ lines of code

---

## Next Steps

1. ✅ Feature 04: Add Battle Result - COMPLETE
2. ✅ Feature 05: Add Ranking - COMPLETE
3. ⏳ Feature 06: Update Ranking After Battle
4. ⏳ Feature 07: Get Ranking by Species
5. ⏳ Feature 08: List Ranking (Leaderboard)

