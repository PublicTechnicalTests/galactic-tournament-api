package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for battle response.
 *
 * This record is used to return battle result information in API responses.
 *
 * @param idCombate          the unique battle identifier
 * @param idContendiente1    the ID of the first contender
 * @param idContendiente2    the ID of the second contender
 * @param idGanador          the ID of the winning species
 * @param nombreGanador      the name of the winning species
 * @param poderGanador       the power level of the winning species
 * @param fechaCreacion      the battle creation timestamp
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for battle response")
public record BattleResponse(

        @JsonProperty("id_combate")
        @Schema(description = "Unique battle identifier", example = "1")
        Long idCombate,

        @JsonProperty("id_contendiente_1")
        @Schema(description = "First contender species ID", example = "1")
        Long idContendiente1,

        @JsonProperty("id_contendiente_2")
        @Schema(description = "Second contender species ID", example = "2")
        Long idContendiente2,

        @JsonProperty("id_ganador")
        @Schema(description = "Winning species ID", example = "1")
        Long idGanador,

        @JsonProperty("nombre_ganador")
        @Schema(description = "Name of winning species", example = "Vulcan")
        String nombreGanador,

        @JsonProperty("poder_ganador")
        @Schema(description = "Power level of winning species", example = "100")
        Integer poderGanador,

        @JsonProperty("fecha_creacion")
        @Schema(description = "Battle creation timestamp", example = "2026-06-01T10:00:00")
        LocalDateTime fechaCreacion

) {
}

