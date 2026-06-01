package com.technicaltests.mv.galactictournamentapi.controller;

import com.technicaltests.mv.galactictournamentapi.dto.StartBattleRequest;
import com.technicaltests.mv.galactictournamentapi.dto.BattleResponse;
import com.technicaltests.mv.galactictournamentapi.service.CombateService;
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

/**
 * REST Controller for managing battles in the galactic tournament.
 *
 * Provides endpoints for starting battles between species.
 * All endpoints include comprehensive API documentation via Swagger/OpenAPI.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@RestController
@RequestMapping("/v1/battles")
@Tag(name = "Battles", description = "API for managing galactic battles")
@Slf4j
@RequiredArgsConstructor
public class CombateController {

    private final CombateService combateService;

    /**
     * Starts a battle between two species.
     *
     * The winner is determined by:
     * 1. Higher power level wins
     * 2. If power is equal, alphabetically first species wins
     *
     * @param request the start battle request with contender IDs
     * @return ResponseEntity with battle result and HTTP 201 (Created) status
     */
    @PostMapping
    @Operation(
            summary = "Start a battle between two species",
            description = "Initiates a battle between two species. Winner is determined by power level, " +
                    "or alphabetically if power is equal. The battle result and winner information are returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Battle started and completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "One or both species not found")
    })
    public ResponseEntity<BattleResponse> startBattle(@Valid @RequestBody StartBattleRequest request) {
        log.info("POST request to start battle between species {} and {}",
                request.idContendiente1(), request.idContendiente2());

        BattleResponse response = combateService.startBattle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a specific battle by ID.
     *
     * @param id the battle ID
     * @return ResponseEntity with the battle and HTTP 200 (OK) status
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a battle by ID",
            description = "Returns detailed information about a specific battle"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Battle retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Battle not found")
    })
    public ResponseEntity<Object> getBattleById(@PathVariable Long id) {
        log.info("GET request to retrieve battle with ID: {}", id);
        Object response = combateService.getBattleById(id);
        return ResponseEntity.ok(response);
    }
}

