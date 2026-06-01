package com.technicaltests.mv.galactictournamentapi.controller;

import com.technicaltests.mv.galactictournamentapi.dto.CreateRankingRequest;
import com.technicaltests.mv.galactictournamentapi.dto.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing rankings in the galactic tournament.
 *
 * Provides endpoints for creating and retrieving species rankings.
 * Rankings track the number of victories for each species.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@RestController
@RequestMapping("/v1/rankings")
@Tag(name = "Rankings", description = "API for managing species rankings")
@Slf4j
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    /**
     * Creates a new ranking for a species.
     *
     * Validates that the species exists and doesn't already have a ranking.
     *
     * @param request the create ranking request
     * @return ResponseEntity with the created ranking and HTTP 201 (Created) status
     */
    @PostMapping
    @Operation(
            summary = "Create a ranking for a species",
            description = "Creates a new ranking entry for a species. " +
                    "The species must exist and must not already have a ranking."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ranking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or ranking already exists"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<RankingResponse> createRanking(@Valid @RequestBody CreateRankingRequest request) {
        log.info("POST request to create ranking for species ID: {}", request.idEspecie());

        RankingResponse response = rankingService.createRanking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a ranking by ID.
     *
     * @param id the ranking ID
     * @return ResponseEntity with the ranking and HTTP 200 (OK) status
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a ranking by ID",
            description = "Returns detailed ranking information for a specific ranking ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ranking not found")
    })
    public ResponseEntity<RankingResponse> getRankingById(@PathVariable Long id) {
        log.info("GET request to retrieve ranking with ID: {}", id);

        RankingResponse response = rankingService.getRankingById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a ranking by species ID.
     *
     * @param specieId the species ID
     * @return ResponseEntity with the ranking and HTTP 200 (OK) status
     */
    @GetMapping("/by-species/{specieId}")
    @Operation(
            summary = "Retrieve ranking by species ID",
            description = "Returns ranking information for a specific species"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ranking not found for this species")
    })
    public ResponseEntity<RankingResponse> getRankingBySpecieId(@PathVariable Long specieId) {
        log.info("GET request to retrieve ranking for species ID: {}", specieId);

        RankingResponse response = rankingService.getRankingBySpecieId(specieId);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all rankings ordered by victories (highest first).
     *
     * @return ResponseEntity with list of all rankings and HTTP 200 (OK) status
     */
    @GetMapping("/leaderboard")
    @Operation(
            summary = "Get leaderboard of all rankings",
            description = "Returns all species rankings ordered by number of victories (highest first)"
    )
    @ApiResponse(responseCode = "200", description = "Leaderboard retrieved successfully")
    public ResponseEntity<List<RankingResponse>> getLeaderboard() {
        log.info("GET request to retrieve leaderboard");

        List<RankingResponse> response = rankingService.getAllRankingsOrderedByVictorias();
        return ResponseEntity.ok(response);
    }
}

