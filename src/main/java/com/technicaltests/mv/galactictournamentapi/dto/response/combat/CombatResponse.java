package com.technicaltests.mv.galactictournamentapi.dto.response.combat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for battle response.
 *
 * This record is used to return battle information in API responses,
 * including details about the contenders and the winner.
 *
 * @param idCombate      the battle ID
 * @param idContendiente1 the first contender species ID
 * @param idContendiente2 the second contender species ID
 * @param idGanador      the winner species ID
 * @param nombreGanador  the name of the winning species
 * @param poderGanador   the power level of the winning species
 * @param fechaCreacion  the creation timestamp
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for battle response")
public record CombatResponse(

        @JsonProperty("id_combate")
        @Schema(description = "Battle ID", example = "1")
        Long idCombate,

        @JsonProperty("id_contendiente_1")
        @Schema(description = "First contender species ID", example = "1")
        Long idContendiente1,

        @JsonProperty("id_contendiente_2")
        @Schema(description = "Second contender species ID", example = "2")
        Long idContendiente2,

        @JsonProperty("id_ganador")
        @Schema(description = "Winner species ID", example = "1")
        Long idGanador,

        @JsonProperty("nombre_ganador")
        @Schema(description = "Name of the winning species", example = "Vulcan")
        String nombreGanador,

        @JsonProperty("poder_ganador")
        @Schema(description = "Power level of the winning species", example = "100")
        Long poderGanador
//        ,
//
//        @JsonProperty("fecha_creacion")
//        @Schema(description = "Battle creation timestamp", example = "2026-06-01T10:00:00")
//        LocalDateTime fechaCreacion

) {
}

