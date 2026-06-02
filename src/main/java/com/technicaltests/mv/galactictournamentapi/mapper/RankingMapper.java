package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Ranking entities and DTOs.
 *
 * Provides utility methods to convert between domain objects and data transfer objects.
 * This separation maintains clean architecture and prevents exposure of internal entities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Component
public class RankingMapper {

    /**
     * Converts a Ranking entity to a RankingResponse DTO.
     *
     * @param ranking the Ranking entity
     * @param especie the associated Especie entity (for nombre)
     * @return the converted RankingResponse DTO
     */
    public RankingResponse toResponse(Ranking ranking, Especie especie) {
        return new RankingResponse(
                ranking.getIdRanking(),
                ranking.getIdEspecie(),
                especie.getNombre(),
                ranking.getVictorias()
        );
    }
}

