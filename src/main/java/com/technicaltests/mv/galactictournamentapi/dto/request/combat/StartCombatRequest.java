package com.technicaltests.mv.galactictournamentapi.dto.request.combat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for starting a battle between two species.
 * <p>
 * This record is used when initiating a battle with automatic winner determination.
 * The winner is determined based on power levels.
 *
 * @param firstcontenderId  the ID of the first contender
 * @param secondContenderId the ID of the second contender
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for starting a battle")
public record StartCombatRequest(

        @JsonProperty("id_contendiente_1")
        @Schema(description = "First contender species ID", example = "1")
        @NotNull(message = "First contender ID must not be null")
        Long firstcontenderId,

        @JsonProperty("id_contendiente_2")
        @Schema(description = "Second contender species ID", example = "2")
        @NotNull(message = "Second contender ID must not be null")
        Long secondContenderId

) {
}

