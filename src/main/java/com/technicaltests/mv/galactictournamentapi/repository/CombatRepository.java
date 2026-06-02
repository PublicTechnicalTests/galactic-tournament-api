package com.technicaltests.mv.galactictournamentapi.repository;

import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the Combate entity.
 * <p>
 * Provides data access operations for battle management.
 * Extends JpaRepository to include standard CRUD operations and JPA-specific queries.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Repository
public interface CombatRepository extends JpaRepository<Combat, Long> {

    /**
     * Finds all battles where a specific species was the winner.
     *
     * @param winnerId the winner species ID
     * @return list of battles won by the species
     */
    List<Combat> findByWinnerId(Long winnerId);

    /**
     * Counts the number of battles won by a specific species.
     *
     * @param winnerId the winner species ID
     * @return count of battles won by this species
     */
    long countByWinnerId(Long winnerId);

    /**
     * Finds battles between two specific species (in either order).
     *
     * @param id1 first species ID
     * @param id2 second species ID
     * @return list of battles between the two species
     */
    @Query("SELECT c FROM Combat c WHERE " +
            "(c.firstContenderId = :id1 AND c.secondContenderId = :id2) OR " +
            "(c.firstContenderId = :id2 AND c.secondContenderId = :id1)")
    List<Combat> findBattlesBetweenSpecies(@Param("id1") Long id1, @Param("id2") Long id2);


    /**
     * Finds all battles where a species was a contender (either winning or losing).
     *
     * @param idEspecie the species ID
     * @return list of battles where this species participated
     */
    @Query("SELECT c FROM Combat c WHERE c.firstContenderId = :idEspecie OR c.secondContenderId = :idEspecie")
    List<Combat> findBattlesBySpecieId(@Param("specieId") Long idEspecie);
}

