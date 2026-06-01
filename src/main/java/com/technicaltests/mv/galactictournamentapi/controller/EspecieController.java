package com.technicaltests.mv.galactictournamentapi.controller;

import com.technicaltests.mv.galactictournamentapi.dto.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.service.EspecieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing species in the galactic tournament.
 *
 * Provides endpoints for creating and retrieving species information.
 * All endpoints include comprehensive API documentation via Swagger/OpenAPI.
 *
 * @author Backend Team
 * @version 2.0
 * @since 2026
 */
@RestController
@RequestMapping("/v1/species")
@Tag(name = "Species", description = "API for managing galactic species")
@Slf4j
@RequiredArgsConstructor
public class EspecieController {

    private final EspecieService especieService;

    /**
     * Creates a new species in the tournament.
     *
     * @param request the create species request body
     * @return ResponseEntity with the created species and HTTP 201 (Created) status
     */
    @PostMapping
    @Operation(
            summary = "Create a new species",
            description = "Adds a new species to the galactic tournament. The species name must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Species created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Species with this name already exists")
    })
    public ResponseEntity<SpecieResponse> createSpecie(@Valid @RequestBody CreateSpecieRequest request) {
        log.info("POST request to create species: {}", request.nombre());
        SpecieResponse response = especieService.createSpecie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all species in the tournament (deprecated, use list endpoint).
     *
     * @return ResponseEntity with a list of all species and HTTP 200 (OK) status
     */
    @GetMapping
    @Deprecated(since = "2.0", forRemoval = true)
    @Operation(
            summary = "Retrieve all species (deprecated)",
            description = "Returns a list of all species registered in the galactic tournament. Use /list endpoint with pagination instead."
    )
    @ApiResponse(responseCode = "200", description = "Species list retrieved successfully")
    public ResponseEntity<List<SpecieResponse>> getAllSpecies() {
        log.info("GET request to retrieve all species");
        List<SpecieResponse> response = especieService.getAllSpecies();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a paginated list of species with optional filtering and sorting.
     *
     * @param page           the page number (0-indexed, default 0)
     * @param size           the page size (default 20, max 100)
     * @param sortBy         the field to sort by (nombre, poder, fechaCreacion, default: poder)
     * @param sortDirection  the sort direction (ASC or DESC, default: DESC)
     * @param searchTerm     optional search term for species name
     * @param minPower       optional minimum power level filter
     * @param maxPower       optional maximum power level filter
     * @return ResponseEntity with paginated species and HTTP 200 (OK) status
     */
    @GetMapping("/list")
    @Operation(
            summary = "List species with pagination and filtering",
            description = "Returns a paginated list of species with optional filtering by name and power level, and sorting capabilities."
    )
    @ApiResponse(responseCode = "200", description = "Paginated species list retrieved successfully")
    public ResponseEntity<PaginatedSpecieResponse> listSpecies(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") Integer size,

            @Parameter(description = "Sort field (nombre, poder, fechaCreacion)")
            @RequestParam(defaultValue = "poder") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection,

            @Parameter(description = "Search term for species name")
            @RequestParam(required = false) String searchTerm,

            @Parameter(description = "Minimum power level")
            @RequestParam(required = false) Integer minPower,

            @Parameter(description = "Maximum power level")
            @RequestParam(required = false) Integer maxPower
    ) {
        log.info("GET request to list species with filters: page={}, size={}, sortBy={}, searchTerm={}, minPower={}, maxPower={}",
                page, size, sortBy, searchTerm, minPower, maxPower);

        SpecieListQuery query = new SpecieListQuery(page, size, sortBy, sortDirection, searchTerm, minPower, maxPower);
        PaginatedSpecieResponse response = especieService.listSpecies(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific species by ID.
     *
     * @param id the species ID
     * @return ResponseEntity with the species and HTTP 200 (OK) status
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a species by ID",
            description = "Returns detailed information about a specific species"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<SpecieResponse> getSpecieById(@PathVariable Long id) {
        log.info("GET request to retrieve species with ID: {}", id);
        SpecieResponse response = especieService.getSpecieById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific species by name.
     *
     * @param nombre the species name
     * @return ResponseEntity with the species and HTTP 200 (OK) status
     */
    @GetMapping("/by-name/{nombre}")
    @Operation(
            summary = "Retrieve a species by name",
            description = "Returns detailed information about a specific species identified by its name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<SpecieResponse> getSpecieByNombre(@PathVariable String nombre) {
        log.info("GET request to retrieve species with name: {}", nombre);
        SpecieResponse response = especieService.getSpecieByNombre(nombre);
        return ResponseEntity.ok(response);
    }
}
