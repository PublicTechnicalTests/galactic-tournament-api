package com.technicaltests.mv.galactictournamentapi.dto.response.specie;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for responding with species information.
 * <p>
 * This record is used to return species data in API responses.
 * Includes all relevant information except internal details.
 *
 * @param specieId    the unique species identifier
 * @param name         the name of the species
 * @param power        the power level of the species
 * @param ability      the special ability of the species
// * @param creationDate the creation timestamp
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for species response")
public record SpecieResponse(

        @JsonProperty("id_especie")
        @Schema(description = "Unique species identifier", example = "1")
        Long specieId,

        @JsonProperty("name")
        @Schema(description = "Species name", example = "Vulcan")
        String name,

        @JsonProperty("power")
        @Schema(description = "Species power level", example = "100")
        Integer power,

        @JsonProperty("ability")
        @Schema(description = "Species special ability", example = "Mind meditation and control")
        String ability
//        ,
//
//        @JsonProperty("creation_date")
//        @Schema(description = "Creation timestamp", example = "2026-06-01T10:00:00")
//        LocalDateTime creationDate

) {
}
