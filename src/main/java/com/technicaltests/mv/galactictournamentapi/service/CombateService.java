package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.StartBattleRequest;
import com.technicaltests.mv.galactictournamentapi.dto.BattleResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combate;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.CombateMapper;
import com.technicaltests.mv.galactictournamentapi.repository.CombateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing battles.
 *
 * Provides business logic operations for battle management including starting battles
 * and determining winners based on species power and name.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CombateService {

    private final CombateRepository combateRepository;
    private final CombateMapper combateMapper;
    private final EspecieService especieService;

    /**
     * Starts a battle between two species.
     *
     * Battle logic:
     * 1. Compare power levels
     * 2. If equal, sort alphabetically by nombre and select first
     * 3. Winner is the species with higher power (or first alphabetically if tied)
     *
     * @param request the start battle request containing contender IDs
     * @return the battle response with winner information
     * @throws SpecieNotFoundException if either species is not found
     */
    public BattleResponse startBattle(StartBattleRequest request) {
        log.info("Starting battle between species {} and {}",
                request.idContendiente1(), request.idContendiente2());

        // Validate that both contenders exist
        Especie contendiente1 = especieService.getEspecieEntityById(request.idContendiente1());
        Especie contendiente2 = especieService.getEspecieEntityById(request.idContendiente2());

        // Determine winner based on battle logic
        Especie ganador = determineWinner(contendiente1, contendiente2);

        // Create battle entity
        Combate combate = new Combate(
                request.idContendiente1(),
                request.idContendiente2(),
                ganador.getIdEspecie()
        );

        // Save battle
        Combate savedCombate = combateRepository.save(combate);

        log.info("Battle {} completed. Winner: {} (ID: {})",
                savedCombate.getIdCombate(),
                ganador.getNombre(),
                ganador.getIdEspecie());

        return combateMapper.toResponse(savedCombate, ganador);
    }

    /**
     * Determines the winner of a battle between two species.
     *
     * Logic:
     * 1. Compare power levels - higher power wins
     * 2. If power is equal, sort by nombre alphabetically and first wins
     *
     * @param contender1 first species
     * @param contender2 second species
     * @return the winning species
     */
    private Especie determineWinner(Especie contender1, Especie contender2) {
        log.debug("Determining winner. Contender1: {} (power: {}), Contender2: {} (power: {})",
                contender1.getNombre(), contender1.getPoder(),
                contender2.getNombre(), contender2.getPoder());

        // Compare power levels
        int powerComparison = contender2.getPoder().compareTo(contender1.getPoder());

        if (powerComparison != 0) {
            // Different power levels - winner has higher power
            Especie winner = powerComparison > 0 ? contender2 : contender1;
            log.debug("Winner determined by power. Winner: {}", winner.getNombre());
            return winner;
        }

        // Same power - sort alphabetically and select first
        Especie winner = contender1.getNombre().compareTo(contender2.getNombre()) <= 0
                ? contender1
                : contender2;
        log.debug("Winner determined by alphabetical order. Winner: {}", winner.getNombre());
        return winner;
    }

    /**
     * Retrieves a battle by its ID.
     *
     * @param id the battle ID
     * @return the battle entity
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException if not found
     */
    @Transactional(readOnly = true)
    public Combate getBattleById(Long id) {
        log.info("Retrieving battle with ID: {}", id);
        return combateRepository.findById(id)
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
    public long countBattlesWonBySpecies(Long idEspecie) {
        log.info("Counting battles won by species ID: {}", idEspecie);
        long count = combateRepository.countByIdGanador(idEspecie);
        log.info("Species ID {} has won {} battles", idEspecie, count);
        return count;
    }
}

