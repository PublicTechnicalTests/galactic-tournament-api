package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for starting a battle between two species.
 *
 * This record is used in POST requests to initiate a battle.
 * Both contender IDs must be provided and must exist.
 *
 * @param idContendiente1 the ID of the first species contender
 * @param idContendiente2 the ID of the second species contender
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for starting a battle between two species")
public record StartBattleRequest(

        @JsonProperty("id_contendiente_1")
        @Schema(description = "First contender species ID", example = "1")
        @NotNull(message = "First contender ID must not be null")
        Long idContendiente1,

        @JsonProperty("id_contendiente_2")
        @Schema(description = "Second contender species ID", example = "2")
        @NotNull(message = "Second contender ID must not be null")
        Long idContendiente2

) {
}

