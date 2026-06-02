package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
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
     * @param specie the associated Especie entity (for name)
     * @return the converted RankingResponse DTO
     */
    public RankingResponse toResponse(Ranking ranking, Specie specie) {
        return new RankingResponse(
                ranking.getRankingId(),
                ranking.getSpecieId(),
                specie.getName(),
                ranking.getVictories()
        );
    }
}

