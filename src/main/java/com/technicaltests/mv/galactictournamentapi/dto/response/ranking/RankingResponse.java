package com.technicaltests.mv.galactictournamentapi.dto.response.ranking;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for ranking response.
 *
 * This record is used to return ranking information in API responses.
 *
 * @param idRanking   the unique ranking identifier
 * @param idEspecie   the species ID
 * @param nombreEspecie the name of the species
 * @param victorias   the number of victories
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for ranking response")
public record RankingResponse(

        @JsonProperty("id_ranking")
        @Schema(description = "Unique ranking identifier", example = "1")
        Long idRanking,

        @JsonProperty("id_especie")
        @Schema(description = "Species ID", example = "1")
        Long idEspecie,

        @JsonProperty("nombre_especie")
        @Schema(description = "Name of the species", example = "Vulcan")
        String nombreEspecie,

        @JsonProperty("victorias")
        @Schema(description = "Number of victories", example = "5")
        Long victorias

) {
}

