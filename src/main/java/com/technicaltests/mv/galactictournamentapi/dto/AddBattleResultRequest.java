package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for adding a battle result manually.
 *
 * This record is used to explicitly add a battle result without
 * triggering the automatic winner determination logic.
 *
 * @param idContendiente1 the ID of the first contender
 * @param idContendiente2 the ID of the second contender
 * @param idGanador       the ID of the winner (must be one of the contenders)
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for adding a battle result")
public record AddBattleResultRequest(

        @JsonProperty("id_contendiente_1")
        @Schema(description = "First contender species ID", example = "1")
        @NotNull(message = "First contender ID must not be null")
        Long idContendiente1,

        @JsonProperty("id_contendiente_2")
        @Schema(description = "Second contender species ID", example = "2")
        @NotNull(message = "Second contender ID must not be null")
        Long idContendiente2,

        @JsonProperty("id_ganador")
        @Schema(description = "Winner species ID (must be one of contenders)", example = "1")
        @NotNull(message = "Winner ID must not be null")
        Long idGanador

) {
}

