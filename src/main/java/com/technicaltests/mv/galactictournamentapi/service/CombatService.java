package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.combat.AddCombatResultRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.dto.request.combat.StartCombatRequest;
import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;

public interface CombatService {

    /**
     * Starts a battle between two species with automatic winner determination.
     * <p>
     * Battle logic:
     * 1. Compare power levels
     * 2. If equal, sort alphabetically by name and select first
     * 3. Winner is the species with higher power (or first alphabetically if tied)
     *
     * @param request the start battle request containing contender IDs
     * @return the battle response with winner information
     * @throws SpecieNotFoundException if either species is not found
     */
    CombatResponse startBattle(StartCombatRequest request);

    /**
     * Adds a battle result manually without automatic winner determination.
     * <p>
     * Validates that:
     * 1. Both contenders exist
     * 2. Winner is one of the contenders
     * 3. Winner is not the same as one of the contenders twice
     *
     * @param request the add battle result request
     * @return the battle response with the specified winner
     * @throws SpecieNotFoundException  if any species is not found
     * @throws IllegalArgumentException if winner is not one of the contenders
     */
    CombatResponse addBattleResult(AddCombatResultRequest request);

//    /**
//     * Determines the winner of a battle between two species.
//     * <p>
//     * Logic:
//     * 1. Compare power levels - higher power wins
//     * 2. If power is equal, sort by name alphabetically and first wins
//     *
//     * @param contender1 first species
//     * @param contender2 second species
//     * @return the winning species
//     */
//    Especie determineWinner(Especie contender1, Especie contender2);

    /**
     * Retrieves a battle by its ID.
     *
     * @param id the battle ID
     * @return the battle entity
     */
    Combat getBattleById(Long id);

    /**
     * Counts the number of battles won by a species.
     *
     * @param idEspecie the species ID
     * @return the count of wins
     */
    long countBattlesWonBySpecies(Long idEspecie);
}
