package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.BattleResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combate;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
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
public class CombateMapper {

    /**
     * Converts a Combate entity to a BattleResponse DTO.
     *
     * @param combate the Combate entity
     * @param ganador the winning Especie
     * @return the converted BattleResponse DTO
     */
    public BattleResponse toResponse(Combate combate, Especie ganador) {
        return new BattleResponse(
                combate.getIdCombate(),
                combate.getIdContendiente1(),
                combate.getIdContendiente2(),
                combate.getIdGanador(),
                ganador.getNombre(),
                ganador.getPoder(),
                combate.getFechaCreacion()
        );
    }
}

