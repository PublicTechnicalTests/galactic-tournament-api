package com.technicaltests.mv.galactictournamentapi.repository;

import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the Especie entity.
 * <p>
 * Provides data access operations for species management.
 * Extends JpaRepository to include standard CRUD operations and JPA-specific queries.
 * Extends JpaSpecificationExecutor for advanced filtering and pagination.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Repository
public interface SpecieRepository extends JpaRepository<Specie, Long>, JpaSpecificationExecutor<Specie> {

    /**
     * Finds a species by its name.
     *
     * @param name the name of the species
     * @return an Optional containing the species if found
     */
    Optional<Specie> findByName(String name);

    /**
     * Checks if a species with the given name exists.
     *
     * @param name the name of the species
     * @return true if a species with this name exists, false otherwise
     */
    boolean existsByName(String name);
}
