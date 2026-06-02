package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Combate entities and DTOs.
 *
 * Provides utility methods to convert between domain objects and data transfer objects.
 * This separation maintains clean architecture and prevents exposure of internal entities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Component
public class CombatMapper {

    /**
     * Converts a Combate entity to a BattleResponse DTO.
     *
     * @param combat the Combate entity
     * @param ganador the Especie entity representing the winner
     * @return the converted BattleResponse DTO
     */
    public CombatResponse toResponse(Combat combat, Specie ganador) {
        return new CombatResponse(
                combat.getCombatId(),
                combat.getFirstContenderId(),
                combat.getSecondContenderId(),
                combat.getWinnerId(),
                ganador.getName(),
                ganador.getPower().longValue()
//                ,
//                combat.getFechaCreacion()
        );
    }
}
