package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.CreateRankingRequest;
import com.technicaltests.mv.galactictournamentapi.dto.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.RankingMapper;
import com.technicaltests.mv.galactictournamentapi.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing rankings.
 *
 * Provides business logic operations for ranking management including creation,
 * update, and retrieval of species rankings based on battle victories.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RankingMapper rankingMapper;
    private final EspecieService especieService;

    /**
     * Creates a new ranking for a species.
     *
     * Validates that the species exists and doesn't already have a ranking.
     *
     * @param request the create ranking request
     * @return the created ranking
     * @throws SpecieNotFoundException if the species is not found
     * @throws IllegalStateException if the ranking already exists
     */
    public RankingResponse createRanking(CreateRankingRequest request) {
        log.info("Creating ranking for species ID: {}", request.idEspecie());

        // Validate species exists
        Especie especie = especieService.getEspecieEntityById(request.idEspecie());

        // Check if ranking already exists
        if (rankingRepository.existsByIdEspecie(request.idEspecie())) {
            log.warn("Ranking already exists for species ID: {}", request.idEspecie());
            throw new IllegalStateException(
                    "Ranking already exists for species with ID: " + request.idEspecie()
            );
        }

        // Create new ranking with 0 victories
        Ranking ranking = new Ranking(request.idEspecie());
        Ranking savedRanking = rankingRepository.save(ranking);

        log.info("Ranking created successfully for species ID: {} with ID: {}",
                request.idEspecie(), savedRanking.getIdRanking());

        return rankingMapper.toResponse(savedRanking, especie);
    }

    /**
     * Updates a ranking by incrementing the victory count.
     *
     * Validates that the ranking exists.
     *
     * @param idEspecie the species ID whose ranking to update
     * @return the updated ranking
     * @throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException if ranking not found
     */
    public RankingResponse addVictory(Long idEspecie) {
        log.info("Adding victory to ranking for species ID: {}", idEspecie);

        // Get ranking
        Ranking ranking = rankingRepository.findByIdEspecie(idEspecie)
                .orElseThrow(() -> {
                    log.warn("Ranking not found for species ID: {}", idEspecie);
                    return new RuntimeException("Ranking not found for species with ID: " + idEspecie);
                });

        // Increment victory count
        ranking.addVictory();
        Ranking updatedRanking = rankingRepository.save(ranking);

        log.info("Victory added to ranking. New victory count: {} for species ID: {}",
                updatedRanking.getVictorias(), idEspecie);

        // Get species for response
        Especie especie = especieService.getEspecieEntityById(idEspecie);

        return rankingMapper.toResponse(updatedRanking, especie);
    }

    /**
     * Retrieves a ranking by ID.
     *
     * @param id the ranking ID
     * @return the ranking
     */
    @Transactional(readOnly = true)
    public RankingResponse getRankingById(Long id) {
        log.info("Retrieving ranking with ID: {}", id);

        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ranking not found with ID: {}", id);
                    return new RuntimeException("Ranking not found with ID: " + id);
                });

        Especie especie = especieService.getEspecieEntityById(ranking.getIdEspecie());
        return rankingMapper.toResponse(ranking, especie);
    }

    /**
     * Retrieves a ranking by species ID.
     *
     * @param idEspecie the species ID
     * @return the ranking if exists
     */
    @Transactional(readOnly = true)
    public RankingResponse getRankingBySpecieId(Long idEspecie) {
        log.info("Retrieving ranking for species ID: {}", idEspecie);

        Ranking ranking = rankingRepository.findByIdEspecie(idEspecie)
                .orElseThrow(() -> {
                    log.warn("Ranking not found for species ID: {}", idEspecie);
                    return new RuntimeException("Ranking not found for species with ID: " + idEspecie);
                });

        Especie especie = especieService.getEspecieEntityById(idEspecie);
        return rankingMapper.toResponse(ranking, especie);
    }

    /**
     * Gets all rankings ordered by victories (highest first).
     *
     * @return list of all rankings sorted by victories descending
     */
    @Transactional(readOnly = true)
    public List<RankingResponse> getAllRankingsOrderedByVictorias() {
        log.info("Retrieving all rankings ordered by victories");

        List<Ranking> rankings = rankingRepository.findAllOrderByVictorias();

        return rankings.stream()
                .map(ranking -> {
                    Especie especie = especieService.getEspecieEntityById(ranking.getIdEspecie());
                    return rankingMapper.toResponse(ranking, especie);
                })
                .toList();
    }

    /**
     * Checks if a ranking exists for a species.
     *
     * @param idEspecie the species ID
     * @return true if ranking exists
     */
    @Transactional(readOnly = true)
    public boolean rankingExists(Long idEspecie) {
        return rankingRepository.existsByIdEspecie(idEspecie);
    }
}

