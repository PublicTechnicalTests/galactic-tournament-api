package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.EspecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing species.
 *
 * Provides business logic operations for species management including creation, retrieval,
 * and validation. Implements transactional operations and logging for audit purposes.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EspecieService {

    private final EspecieRepository especieRepository;
    private final EspecieMapper especieMapper;

    /**
     * Creates a new species in the tournament.
     *
     * Validates that the species name is unique before persisting.
     *
     * @param request the create species request containing species details
     * @return the created species as a SpecieResponse
     * @throws SpecieAlreadyExistsException if a species with the same name already exists
     */
    public SpecieResponse createSpecie(CreateSpecieRequest request) {
        log.info("Creating new species: {}", request.nombre());

        // Validate that the species name doesn't already exist
        if (especieRepository.existsByNombre(request.nombre())) {
            log.warn("Attempt to create duplicate species: {}", request.nombre());
            throw new SpecieAlreadyExistsException(
                    "A species with the name '" + request.nombre() + "' already exists"
            );
        }

        // Convert DTO to entity and save
        Especie especie = especieMapper.toEntity(request);
        Especie savedEspecie = especieRepository.save(especie);

        log.info("Species created successfully with ID: {}", savedEspecie.getIdEspecie());
        return especieMapper.toResponse(savedEspecie);
    }

    /**
     * Retrieves a species by its ID.
     *
     * @param id the species ID
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    public SpecieResponse getSpecieById(Long id) {
        log.info("Retrieving species with ID: {}", id);

        Especie especie = especieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Species not found with ID: {}", id);
                    return new SpecieNotFoundException("Species not found with ID: " + id);
                });

        return especieMapper.toResponse(especie);
    }

    /**
     * Retrieves a species by its name.
     *
     * @param nombre the species name
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    public SpecieResponse getSpecieByNombre(String nombre) {
        log.info("Retrieving species with name: {}", nombre);

        Especie especie = especieRepository.findByNombre(nombre)
                .orElseThrow(() -> {
                    log.warn("Species not found with name: {}", nombre);
                    return new SpecieNotFoundException("Species not found with name: " + nombre);
                });

        return especieMapper.toResponse(especie);
    }

    /**
     * Retrieves all species in the tournament.
     *
     * @return a list of all species as SpecieResponse objects
     */
    @Transactional(readOnly = true)
    public List<SpecieResponse> getAllSpecies() {
        log.info("Retrieving all species");
        List<Especie> especies = especieRepository.findAll();
        log.info("Found {} species", especies.size());
        return especies.stream()
                .map(especieMapper::toResponse)
                .toList();
    }

    /**
     * Checks if a species exists by its ID.
     *
     * @param id the species ID
     * @return true if the species exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return especieRepository.existsById(id);
    }

    /**
     * Retrieves the raw Especie entity by ID (for internal use).
     *
     * @param id the species ID
     * @return the Especie entity
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    public Especie getEspecieEntityById(Long id) {
        return especieRepository.findById(id)
                .orElseThrow(() -> new SpecieNotFoundException("Species not found with ID: " + id));
    }
}

