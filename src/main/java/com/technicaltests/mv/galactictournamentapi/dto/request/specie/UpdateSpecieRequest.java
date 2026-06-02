package com.technicaltests.mv.galactictournamentapi.dto.request.specie;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for updating an existing species.
 *
 * This record is used in PUT/PATCH requests to update species information.
 * All fields are optional for partial updates.
 *
 * @param poder     the power level of the species (positive, optional)
 * @param habilidad the special ability of the species (optional)
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for updating species information")
public record UpdateSpecieRequest(

        @JsonProperty("power")
        @Schema(description = "Species power level - must be positive", example = "120")
        @NotNull(message = "Species power must not be null")
        @Positive(message = "Species power must be positive")
        Integer poder,

        @JsonProperty("ability")
        @Schema(description = "Species special ability", example = "Enhanced telepathy")
        String habilidad

) {
}
