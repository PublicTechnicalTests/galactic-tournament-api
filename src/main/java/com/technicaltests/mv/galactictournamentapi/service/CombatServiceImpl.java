package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.combat.AddCombatResultRequest;
import com.technicaltests.mv.galactictournamentapi.dto.request.combat.StartCombatRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.CombatMapper;
import com.technicaltests.mv.galactictournamentapi.repository.CombatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing battles.
 * <p>
 * Provides business logic operations for battle management including starting battles,
 * adding battle results manually, and determining winners based on species power and name.
 *
 * @author Backend Team
 * @version 2.0
 * @since 2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CombatServiceImpl implements CombatService {

    private final CombatRepository combatRepository;
    private final CombatMapper combatMapper;
    private final SpecieService specieService;
    private final RankingService rankingService;

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
    @Override
    public CombatResponse startBattle(StartCombatRequest request) {
        log.info("Starting battle between species {} and {}",
                request.firstcontenderId(), request.secondContenderId());

        // Validate that both contenders exist
        Specie firstContender = specieService.getEspecieEntityById(request.firstcontenderId());
        Specie secondContender = specieService.getEspecieEntityById(request.secondContenderId());

        // Determine winner based on battle logic
        Specie ganador = determineWinner(firstContender, secondContender);

        // Create and save battle
        Combat combat =
                Combat.builder()
                        .firstContenderId(request.firstcontenderId())
                        .secondContenderId(request.secondContenderId())
                        .winnerId(ganador.getSpecieId())
                        .build();


        // Save battle
        Combat savedCombat = combatRepository.save(combat);

        rankingService.addVictory(ganador.getSpecieId());

        log.info("Battle {} completed. Winner: {} (ID: {})",
                savedCombat.getCombatId(),
                ganador.getName(),
                ganador.getSpecieId());

        return combatMapper.toResponse(savedCombat, ganador);
    }

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
    @Override
    public CombatResponse addBattleResult(AddCombatResultRequest request) {
        log.info("Adding battle result between species {} and {} with winner {}",
                request.firstcontenderId(), request.secondContenderId(), request.winnerId());

        // Validate that both contenders exist
        Specie firstContender = specieService.getEspecieEntityById(request.firstcontenderId());
        Specie secondContender = specieService.getEspecieEntityById(request.secondContenderId());

        // Validate that winner is one of the contenders
        if (!request.winnerId().equals(request.firstcontenderId()) &&
                !request.winnerId().equals(request.secondContenderId())) {
            log.warn("Invalid winner ID: {} (not one of the contenders)", request.winnerId());
            throw new IllegalArgumentException(
                    "Winner ID must be one of the contenders"
            );
        }

        // Validate that contenders are different
        if (request.firstcontenderId().equals(request.secondContenderId())) {
            log.warn("Contenders cannot be the same: {}", request.firstcontenderId());
            throw new IllegalArgumentException(
                    "Contenders must be different species"
            );
        }

        // Get winner species
        Specie ganador = specieService.getEspecieEntityById(request.winnerId());

        // Create and save battle
        Combat combat =
                Combat.builder()
                        .firstContenderId(request.firstcontenderId())
                        .secondContenderId(request.secondContenderId())
                        .winnerId(ganador.getSpecieId())
                        .build();

        Combat savedCombat = combatRepository.save(combat);

        log.info("Battle result {} added. Winner: {} (ID: {})",
                savedCombat.getCombatId(),
                ganador.getName(),
                ganador.getSpecieId());

        return combatMapper.toResponse(savedCombat, ganador);
    }

    /**
     * Retrieves a battle by its ID.
     *
     * @param id the battle ID
     * @return the battle entity
     */
    @Transactional(readOnly = true)
    @Override
    public Combat getBattleById(Long id) {
        log.info("Retrieving battle with ID: {}", id);
        return combatRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Battle not found with ID: {}", id);
                    return new RuntimeException("Battle not found with ID: " + id);
                });
    }

    /**
     * Counts the number of battles won by a species.
     *
     * @param idEspecie the species ID
     * @return the count of wins
     */
    @Transactional(readOnly = true)
    @Override
    public long countBattlesWonBySpecies(Long idEspecie) {
        log.info("Counting battles won by species ID: {}", idEspecie);
        long count = combatRepository.countByWinnerId(idEspecie);
        log.info("Species ID {} has won {} battles", idEspecie, count);
        return count;
    }

    /**
     * Determines the winner of a battle between two species.
     * <p>
     * Logic:
     * 1. Compare power levels - higher power wins
     * 2. If power is equal, sort by name alphabetically and first wins
     *
     * @param contender1 first species
     * @param contender2 second species
     * @return the winning species
     */
    private Specie determineWinner(Specie contender1, Specie contender2) {
        log.debug("Determining winner. Contender1: {} (power: {}), Contender2: {} (power: {})",
                contender1.getName(), contender1.getPower(),
                contender2.getName(), contender2.getPower());

        // Compare power levels
        int powerComparison = contender2.getPower().compareTo(contender1.getPower());

        if (powerComparison != 0) {
            // Different power levels - winner has higher power
            Specie winner = powerComparison > 0 ? contender2 : contender1;
            log.debug("Winner determined by power. Winner: {}", winner.getName());
            return winner;
        }

        // Same power - sort alphabetically and select first
        Specie winner = contender1.getName().compareTo(contender2.getName()) <= 0
                ? contender1
                : contender2;
        log.debug("Winner determined by alphabetical order. Winner: {}", winner.getName());
        return winner;
    }

}

