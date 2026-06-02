package com.technicaltests.mv.galactictournamentapi.controller;

import com.technicaltests.mv.galactictournamentapi.dto.request.combat.StartCombatRequest;
import com.technicaltests.mv.galactictournamentapi.dto.request.combat.AddCombatResultRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.service.CombatService;
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
 * <p>
 * Provides endpoints for starting battles and adding battle results.
 * All endpoints include comprehensive API documentation via Swagger/OpenAPI.
 *
 * @author Backend Team
 * @version 2.0
 * @since 2026
 */
@RestController
@RequestMapping("/v1/battles")
@Tag(name = "Battles", description = "API for managing galactic battles")
@Slf4j
@RequiredArgsConstructor
public class CombatController {

    private final CombatService combatService;

    /**
     * Starts a battle between two species with automatic winner determination.
     * <p>
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
            description = "Initiates a battle between two species with automatic winner determination. " +
                    "Winner is determined by power level, or alphabetically if power is equal."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Battle started successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "One or both species not found")
    })
    public ResponseEntity<CombatResponse> startBattle(@Valid @RequestBody StartCombatRequest request) {
        log.info("POST request to start battle between species {} and {}",
                request.firstcontenderId(), request.secondContenderId());

        CombatResponse response = combatService.startBattle(request);
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
        Object response = combatService.getBattleById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Adds a battle result manually without automatic winner determination.
     * <p>
     * This endpoint allows explicitly specifying the winner without
     * using the automatic power-based winner determination.
     *
     * @param request the add battle result request with contender IDs and winner
     * @return ResponseEntity with battle result and HTTP 201 (Created) status
     */
    @PostMapping("/result")
    @Operation(
            summary = "Add a battle result manually",
            description = "Adds a battle result with explicitly specified winner. " +
                    "Winner must be one of the contenders. No automatic winner determination is used."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Battle result added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (winner not one of contenders)"),
            @ApiResponse(responseCode = "404", description = "One or more species not found")
    })
    public ResponseEntity<CombatResponse> addBattleResult(@Valid @RequestBody AddCombatResultRequest request) {
        log.info("POST request to add battle result: {} vs {}, winner: {}",
                request.firstcontenderId(), request.secondContenderId(), request.winnerId());

        CombatResponse response = combatService.addBattleResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}

