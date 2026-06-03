package com.technicaltests.mv.galactictournamentapi.controller;

import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing rankings in the galactic tournament.
 * <p>
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

