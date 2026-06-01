package com.technicaltests.mv.galactictournamentapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO for paginated species response.
 *
 * This record wraps paginated results with metadata about the current page.
 *
 * @param content           the list of species in this page
 * @param totalElements     the total number of elements across all pages
 * @param totalPages        the total number of pages
 * @param currentPage       the current page number (0-indexed)
 * @param pageSize          the size of the current page
 * @param hasNextPage       whether there is a next page
 * @param hasPreviousPage   whether there is a previous page
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Schema(description = "Paginated species response")
public record PaginatedSpecieResponse(

        @JsonProperty("content")
        @Schema(description = "List of species in this page")
        List<SpecieResponse> content,

        @JsonProperty("total_elements")
        @Schema(description = "Total number of species", example = "100")
        long totalElements,

        @JsonProperty("total_pages")
        @Schema(description = "Total number of pages", example = "5")
        int totalPages,

        @JsonProperty("current_page")
        @Schema(description = "Current page number (0-indexed)", example = "0")
        int currentPage,

        @JsonProperty("page_size")
        @Schema(description = "Size of current page", example = "20")
        int pageSize,

        @JsonProperty("has_next_page")
        @Schema(description = "Whether there is a next page", example = "true")
        boolean hasNextPage,

        @JsonProperty("has_previous_page")
        @Schema(description = "Whether there is a previous page", example = "false")
        boolean hasPreviousPage

) {
}

