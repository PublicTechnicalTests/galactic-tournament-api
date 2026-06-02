package com.technicaltests.mv.galactictournamentapi.dto.request.specie;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for creating a new species.
 *
 * This record is used in POST requests to add a new species to the tournament.
 * All fields are validated at the API boundary.
 *
 * @param name    the name of the species (unique, required)
 * @param power     the power level of the species (positive, required)
 * @param ability the special ability of the species (required)
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "DTO for creating a new species")
public record CreateSpecieRequest(

        @JsonProperty("name")
        @Schema(description = "Species name - must be unique", example = "Vulcan")
        @NotBlank(message = "Species name must not be blank")
        String name,

        @JsonProperty("power")
        @Schema(description = "Species power level - must be positive", example = "100")
        @NotNull(message = "Species power must not be null")
        @Positive(message = "Species power must be positive")
        Integer power,

        @JsonProperty("ability")
        @Schema(description = "Species special ability", example = "Mind meditation and control")
        @NotBlank(message = "Species ability must not be blank")
        String ability

) {
}
