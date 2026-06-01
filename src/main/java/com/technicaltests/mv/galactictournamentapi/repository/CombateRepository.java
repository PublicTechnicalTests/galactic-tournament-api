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
     * Finds all battles where a specific species was the winner.
     *
     * @param idGanador the winner species ID
     * @return list of battles won by the species
     */
    List<Combate> findByIdGanador(Long idGanador);

    /**
     * Counts battles won by a specific species.
     *
     * @param idGanador the winner species ID
     * @return count of battles won
     */
    long countByIdGanador(Long idGanador);

    /**
     * Finds battles between two specific species (in either order).
     *
     * @param id1 first species ID
     * @param id2 second species ID
     * @return list of battles between the two species
     */
    @Query("SELECT c FROM Combate c WHERE " +
            "(c.idContendiente1 = :id1 AND c.idContendiente2 = :id2) OR " +
            "(c.idContendiente1 = :id2 AND c.idContendiente2 = :id1)")
    List<Combate> findBattlesBetweenSpecies(@Param("id1") Long id1, @Param("id2") Long id2);
}

