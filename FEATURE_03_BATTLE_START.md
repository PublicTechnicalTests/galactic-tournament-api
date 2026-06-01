# Feature 03: Start Battle Between Species

## Overview
This feature implements battle logic in the Galactic Tournament API, allowing two species to compete with a winner determined by power level or alphabetical order.

## Requirements
- Start a battle between two species
- Determine winner by power level (higher wins)
- If power is equal, determine winner alphabetically (first alphabetically wins)
- Save battle result
- Return battle details with winner information

## Implementation Details

### New Entity
- **Combate**: Represents a battle
  - IdCombate: Long (PK)
  - IdContendiente1: Long (FK to Especie)
  - IdContendiente2: Long (FK to Especie)
  - IdGanador: Long (FK to Especie)
  - FechaCreacion: LocalDateTime (DEFAULT)

### New DTOs (Records)
- **StartBattleRequest**: Request to start a battle
  - idContendiente1: Long (required)
  - idContendiente2: Long (required)

- **BattleResponse**: Response with battle result
  - idCombate: Long
  - idContendiente1: Long
  - idContendiente2: Long
  - idGanador: Long
  - nombreGanador: String
  - poderGanador: Integer
  - fechaCreacion: LocalDateTime

### Repository
- **CombateRepository**: JPA repository for battles
  - findByIdGanador: Find battles by winner
  - countByIdGanador: Count wins by species
  - findBattlesBetweenSpecies: Find battles between two species

### Service
- **CombateService**: Battle business logic
  - startBattle: Initiate battle and determine winner
  - determineWinner: Winner logic (power/alphabetical)
  - countBattlesWonBySpecies: Get win count

### Controller Endpoint
#### POST /api/v1/battles
**Start a battle between two species**
```
Request:
{
  "id_contendiente_1": 1,
  "id_contendiente_2": 2
}

Response: 201 Created
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

## Battle Logic

### Winner Determination
1. **Compare Power Levels**
   - If `power2 > power1`: Species 2 wins
   - If `power1 > power2`: Species 1 wins
   - If `power1 == power2`: Use alphabetical order

2. **Alphabetical Order (if power is equal)**
   - Sort by nombre alphabetically
   - First species alphabetically wins

### Examples

#### Example 1: Different Power
- Vulcan (power: 100) vs Klingon (power: 150)
- Result: Klingon wins (150 > 100)

#### Example 2: Same Power, Different Names
- Andorian (power: 100) vs Betazoid (power: 100)
- Result: Andorian wins (comes first alphabetically: "Andorian" < "Betazoid")

## Testing

### Unit Tests (CombateServiceTest)
- ✅ Start battle and determine winner by power
- ✅ Determine winner alphabetically when power is equal
- ✅ Throw exception when first contender not found
- ✅ Throw exception when second contender not found
- ✅ Count battles won by species
- ✅ Return zero wins for species with no battles

## Files Created
- **Combate.java** - Entity (~160 lines)
- **StartBattleRequest.java** - DTO (~28 lines)
- **BattleResponse.java** - DTO (~50 lines)
- **CombateRepository.java** - Repository (~45 lines)
- **CombateMapper.java** - Mapper (~30 lines)
- **CombateService.java** - Service (~110 lines)
- **CombateController.java** - Controller (~70 lines)
- **CombateServiceTest.java** - Unit Tests (~180 lines)

## Best Practices Applied
- Entity with proper JPA annotations
- Records for DTOs
- Service layer encapsulation
- Repository pattern
- Mapper pattern
- Comprehensive unit tests
- Javadoc documentation
- Swagger/OpenAPI annotations

## Next Steps
1. ✅ Execute all tests
2. ✅ Verify compilation
3. ✅ Commit changes
4. ✅ Push to repository
5. ✅ Create Pull Request to development

