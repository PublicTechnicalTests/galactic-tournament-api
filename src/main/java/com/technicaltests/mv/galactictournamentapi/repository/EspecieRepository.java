package com.technicaltests.mv.galactictournamentapi.repository;

import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the Especie entity.
 *
 * Provides data access operations for species management.
 * Extends JpaRepository to include standard CRUD operations and JPA-specific queries.
 * Extends JpaSpecificationExecutor for advanced filtering and pagination.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long>, JpaSpecificationExecutor<Especie> {

    /**
     * Finds a species by its name.
     *
     * @param nombre the name of the species
     * @return an Optional containing the species if found
     */
    Optional<Especie> findByNombre(String nombre);

    /**
     * Checks if a species with the given name exists.
     *
     * @param nombre the name of the species
     * @return true if a species with this name exists, false otherwise
     */
    boolean existsByNombre(String nombre);
}
