package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for responding with species information.
 *
 * This record is used to return species data in API responses.
 * Includes all relevant information except internal details.
 *
 * @param idEspecie    the unique species identifier
 * @param nombre       the name of the species
 * @param poder        the power level of the species
 * @param habilidad    the special ability of the species
 * @param fechaCreacion the creation timestamp
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for species response")
public record SpecieResponse(

        @JsonProperty("id_especie")
        @Schema(description = "Unique species identifier", example = "1")
        Long idEspecie,

        @JsonProperty("nombre")
        @Schema(description = "Species name", example = "Vulcan")
        String nombre,

        @JsonProperty("poder")
        @Schema(description = "Species power level", example = "100")
        Integer poder,

        @JsonProperty("habilidad")
        @Schema(description = "Species special ability", example = "Mind meditation and control")
        String habilidad,

        @JsonProperty("fecha_creacion")
        @Schema(description = "Creation timestamp", example = "2026-06-01T10:00:00")
        LocalDateTime fechaCreacion

) {
}
