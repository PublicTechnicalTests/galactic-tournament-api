package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.EspecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.EspecieRepository;
import com.technicaltests.mv.galactictournamentapi.specification.EspecieSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * @version 2.0
 * @since 2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EspecieService {

    private final EspecieRepository especieRepository;
    private final EspecieMapper especieMapper;

    private static final int MAX_PAGE_SIZE = 100;

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
     * Retrieves a paginated list of species with filters and sorting.
     *
     * @param query the query parameters including pagination, filters, and sorting
     * @return a paginated response with species data
     */
    @Transactional(readOnly = true)
    public PaginatedSpecieResponse listSpecies(SpecieListQuery query) {
        log.info("Listing species with query: page={}, size={}, sortBy={}, searchTerm={}",
                query.page(), query.size(), query.sortBy(), query.searchTerm());

        // Apply defaults to null values
        SpecieListQuery normalizedQuery = query.withDefaults();

        // Validate and limit page size
        int pageSize = Math.min(normalizedQuery.size(), MAX_PAGE_SIZE);

        // Create sort order
        Sort.Direction direction = "DESC".equalsIgnoreCase(normalizedQuery.sortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, normalizedQuery.sortBy());

        // Create pageable
        Pageable pageable = PageRequest.of(normalizedQuery.page(), pageSize, sort);

        // Create specification for filters
        var spec = EspecieSpecification.filterBy(
                normalizedQuery.searchTerm(),
                normalizedQuery.minPower(),
                normalizedQuery.maxPower()
        );

        // Fetch paginated results
        Page<Especie> page = especieRepository.findAll(spec, pageable);

        // Convert to response
        List<SpecieResponse> content = page.getContent().stream()
                .map(especieMapper::toResponse)
                .toList();

        log.info("Retrieved {} species from page {}", content.size(), normalizedQuery.page());

        return new PaginatedSpecieResponse(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
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

