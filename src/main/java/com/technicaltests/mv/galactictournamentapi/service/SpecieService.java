package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.specie.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.request.specie.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.request.specie.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.dto.response.specie.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;

import java.util.List;

public interface SpecieService {
    /**
     * Creates a new species in the tournament.
     * <p>
     * Validates that the species name is unique before persisting.
     *
     * @param request the create species request containing species details
     * @return the created species as a SpecieResponse
     * @throws SpecieAlreadyExistsException if a species with the same name already exists
     */
    SpecieResponse createSpecie(CreateSpecieRequest request);
    /**
     * Retrieves a species by its ID.
     *
     * @param id the species ID
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    SpecieResponse getSpecieById(Long id);
    /**
     * Retrieves a species by its name.
     *
     * @param nombre the species name
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    SpecieResponse getSpecieByNombre(String nombre);
    /**
     * Retrieves all species in the tournament.
     *
     * @return a list of all species as SpecieResponse objects
     */
    List<SpecieResponse> getAllSpecies();
    /**
     * Retrieves a paginated list of species with filters and sorting.
     *
     * @param query the query parameters including pagination, filters, and sorting
     * @return a paginated response with species data
     */
    PaginatedSpecieResponse listSpecies(SpecieListQuery query);
    /**
     * Checks if a species exists by its ID.
     *
     * @param id the species ID
     * @return true if the species exists, false otherwise
     */
    boolean existsById(Long id);
    /**
     * Retrieves the raw Especie entity by ID (for internal use).
     *
     * @param id the species ID
     * @return the Especie entity
     * @throws SpecieNotFoundException if the species is not found
     */
    Specie getEspecieEntityById(Long id);
}
