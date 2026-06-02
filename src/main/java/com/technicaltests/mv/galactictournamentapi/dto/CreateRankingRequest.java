package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a ranking entry.
 *
 * This record is used when creating a new ranking for a species.
 *
 * @param idEspecie the species ID for which to create the ranking
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for creating a ranking")
public record CreateRankingRequest(

        @JsonProperty("id_especie")
        @Schema(description = "Species ID", example = "1")
        @NotNull(message = "Species ID must not be null")
        Long idEspecie

) {
}

