package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.specie.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.request.specie.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.request.specie.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.dto.response.specie.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.SpecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.SpecieRepository;
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
 * <p>
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
public class SpecieServiceImpl implements SpecieService {

    private final SpecieRepository specieRepository;
    private final SpecieMapper specieMapper;

    private static final int MAX_PAGE_SIZE = 100;

    /**
     * Creates a new species in the tournament.
     * <p>
     * Validates that the species name is unique before persisting.
     *
     * @param request the create species request containing species details
     * @return the created species as a SpecieResponse
     * @throws SpecieAlreadyExistsException if a species with the same name already exists
     */
    @Override
    public SpecieResponse createSpecie(CreateSpecieRequest request) {
        log.info("Creating new species: {}", request.name());

        // Validate that the species name doesn't already exist
        if (specieRepository.existsByName(request.name())) {
            log.warn("Attempt to create duplicate species: {}", request.name());
            throw new SpecieAlreadyExistsException(
                    "A species with the name '" + request.name() + "' already exists"
            );
        }

        // Convert DTO to entity and save
        Specie specie = specieMapper.toEntity(request);
        Specie savedSpecie = specieRepository.save(specie);

        log.info("Species created successfully with ID: {}", savedSpecie.getSpecieId());
        return specieMapper.toResponse(savedSpecie);
    }

    /**
     * Retrieves a species by its ID.
     *
     * @param id the species ID
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    @Override
    public SpecieResponse getSpecieById(Long id) {
        log.info("Retrieving species with ID: {}", id);

        Specie specie = specieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Species not found with ID: {}", id);
                    return new SpecieNotFoundException("Species not found with ID: " + id);
                });

        return specieMapper.toResponse(specie);
    }

    /**
     * Retrieves a species by its name.
     *
     * @param nombre the species name
     * @return the species as a SpecieResponse
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    @Override
    public SpecieResponse getSpecieByNombre(String nombre) {
        log.info("Retrieving species with name: {}", nombre);

        Specie specie = specieRepository.findByName(nombre)
                .orElseThrow(() -> {
                    log.warn("Species not found with name: {}", nombre);
                    return new SpecieNotFoundException("Species not found with name: " + nombre);
                });

        return specieMapper.toResponse(specie);
    }

    /**
     * Retrieves all species in the tournament.
     *
     * @return a list of all species as SpecieResponse objects
     */
    @Transactional(readOnly = true)
    @Override
    public List<SpecieResponse> getAllSpecies() {
        log.info("Retrieving all species");
        List<Specie> especies = specieRepository.findAll();
        log.info("Found {} species", especies.size());
        return especies.stream()
                .map(specieMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a paginated list of species with filters and sorting.
     *
     * @param query the query parameters including pagination, filters, and sorting
     * @return a paginated response with species data
     */
    @Transactional(readOnly = true)
    @Override
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
        Page<Specie> page = specieRepository.findAll(spec, pageable);

        // Convert to response
        List<SpecieResponse> content = page.getContent().stream()
                .map(specieMapper::toResponse)
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
    @Override
    public boolean existsById(Long id) {
        return specieRepository.existsById(id);
    }

    /**
     * Retrieves the raw Especie entity by ID (for internal use).
     *
     * @param id the species ID
     * @return the Especie entity
     * @throws SpecieNotFoundException if the species is not found
     */
    @Transactional(readOnly = true)
    @Override
    public Specie getEspecieEntityById(Long id) {
        return specieRepository.findById(id)
                .orElseThrow(() -> new SpecieNotFoundException("Species not found with ID: " + id));
    }
}

