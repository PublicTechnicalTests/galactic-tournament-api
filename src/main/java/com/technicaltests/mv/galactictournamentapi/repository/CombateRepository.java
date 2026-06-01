package com.technicaltests.mv.galactictournamentapi.repository;

import com.technicaltests.mv.galactictournamentapi.entity.Combate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the Combate entity.
 *
 * Provides data access operations for battle management.
 * Extends JpaRepository to include standard CRUD operations and JPA-specific queries.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Repository
public interface CombateRepository extends JpaRepository<Combate, Long> {

    /**
     * Counts the number of battles won by a specific species.
     *
     * @param idGanador the winner species ID
     * @return count of battles won by this species
     */
    long countByIdGanador(Long idGanador);

    /**
     * Finds all battles where a species was a contender (either winning or losing).
     *
     * @param idEspecie the species ID
     * @return list of battles where this species participated
     */
    @Query("SELECT c FROM Combate c WHERE c.idContendiente1 = :idEspecie OR c.idContendiente2 = :idEspecie")
    List<Combate> findBattlesBySpecieId(@Param("idEspecie") Long idEspecie);
}

