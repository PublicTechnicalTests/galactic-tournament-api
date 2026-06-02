package com.technicaltests.mv.galactictournamentapi.repository;

import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for the Ranking entity.
 *
 * Provides data access operations for ranking management.
 * Extends JpaRepository to include standard CRUD operations and JPA-specific queries.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    /**
     * Finds a ranking by species ID.
     *
     * @param SpecieId the species ID
     * @return an Optional containing the ranking if found
     */
    Optional<Ranking> findBySpecieId(Long SpecieId);

    /**
     * Checks if a ranking exists for a species.
     *
     * @param SpecieId the species ID
     * @return true if a ranking exists for this species
     */
    boolean existsBySpecieId(Long SpecieId);

    /**
     * Finds all rankings ordered by victories in descending order.
     *
     * @return list of all rankings sorted by victories (highest first)
     */
    @Query("SELECT r FROM Ranking r ORDER BY r.victories DESC")
    List<Ranking> findAllOrderByVictories();
}

