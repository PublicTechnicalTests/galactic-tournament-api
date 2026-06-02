package com.technicaltests.mv.galactictournamentapi.dto.request.specie;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

/**
 * DTO for species list query parameters.
 *
 * This record is used to pass filtering and pagination parameters for listing species.
 * All fields are optional for flexible querying.
 *
 * @param page           the page number (0-indexed, default 0)
 * @param size           the page size (default 20, max 100)
 * @param sortBy         the field to sort by (name, power, creationDate)
 * @param sortDirection  the sort direction (ASC or DESC)
 * @param searchTerm     optional search term for species name
 * @param minPower       optional minimum power level filter
 * @param maxPower       optional maximum power level filter
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "Query parameters for listing species")
public record SpecieListQuery(

        @JsonProperty("page")
        @Schema(description = "Page number (0-indexed)", example = "0")
        @Min(value = 0, message = "Page must be >= 0")
        Integer page,

        @JsonProperty("size")
        @Schema(description = "Page size (max 100)", example = "20")
        @Min(value = 1, message = "Size must be >= 1")
        Integer size,

        @JsonProperty("sort_by")
        @Schema(description = "Sort field (name, power, creationDate)", example = "power")
        String sortBy,

        @JsonProperty("sort_direction")
        @Schema(description = "Sort direction (ASC, DESC)", example = "DESC")
        String sortDirection,

        @JsonProperty("search_term")
        @Schema(description = "Search term for species name", example = "Vulcan")
        String searchTerm,

        @JsonProperty("min_power")
        @Schema(description = "Minimum power level", example = "50")
        Integer minPower,

        @JsonProperty("max_power")
        @Schema(description = "Maximum power level", example = "150")
        Integer maxPower

) {
    /**
     * Provides default values for pagination and sorting.
     * Returns a new SpecieListQuery with sensible defaults if parameters are null.
     *
     * @return SpecieListQuery with defaults applied
     */
    public SpecieListQuery withDefaults() {
        return new SpecieListQuery(
                page != null ? page : 0,
                size != null ? size : 20,
                sortBy != null ? sortBy : "power",
                sortDirection != null ? sortDirection : "DESC",
                searchTerm,
                minPower,
                maxPower
        );
    }
}

