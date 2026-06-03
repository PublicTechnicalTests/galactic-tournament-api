
package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.ranking.CreateRankingRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
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
 * <p>
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
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;
    private final RankingMapper rankingMapper;
    private final SpecieService specieService;

    /**
     * Gets all rankings ordered by victories (highest first).
     *
     * @return list of all rankings sorted by victories descending
     */
    @Transactional(readOnly = true)
    @Override
    public List<RankingResponse> getAllRankingsOrderedByVictorias() {
        log.info("Retrieving all rankings ordered by victories");

        List<Ranking> rankings = rankingRepository.findAllOrderByVictories();

        return rankings.stream()
                .map(ranking -> {
                    Specie specie = specieService.getEspecieEntityById(ranking.getSpecieId());
                    return rankingMapper.toResponse(ranking, specie);
                })
                .toList();
    }

    /**
     * Updates a ranking by incrementing the victory count.
     * <p>
     * Validates that the ranking exists.
     *
     * @param idEspecie the species ID whose ranking to update
     * @return the updated ranking
     */
    @Override
    public boolean addVictory(Long idEspecie) {
        log.info("Adding victory to ranking for species ID: {}", idEspecie);

        if(!rankingRepository.existsBySpecieId(idEspecie)){
            RankingResponse ranking = this.createRanking(new CreateRankingRequest(idEspecie));
            log.info("Victory added to ranking. New victory count: {} for species ID: {}",
                    ranking.victorias(), idEspecie);
            return ranking.idRanking() != null;
        };

        // Get ranking
        Ranking ranking = rankingRepository.findBySpecieId(idEspecie)
                .orElseThrow(() -> {
                    log.warn("Ranking not found for species ID: {}", idEspecie);
                    return new RuntimeException("Ranking not found for species with ID: " + idEspecie);
                });

        // Increment victory count
        ranking.addVictory();
        Ranking updatedRanking = rankingRepository.save(ranking);

        log.info("Victory added to ranking. New victory count: {} for species ID: {}",
                updatedRanking.getVictories(), idEspecie);

        return updatedRanking.getRankingId() != null;
    }


    /**
     * Checks if a ranking exists for a species.
     *
     * @param idEspecie the species ID
     * @return true if ranking exists
     */
    @Transactional(readOnly = true)
    @Override
    public boolean rankingExists(Long idEspecie) {
        return rankingRepository.existsBySpecieId(idEspecie);
    }

    /**
     * Creates a new ranking for a species.
     * <p>
     * Validates that the species exists and doesn't already have a ranking.
     *
     * @param request the create ranking request
     * @return the created ranking
     * @throws SpecieNotFoundException if the species is not found
     * @throws IllegalStateException   if the ranking already exists
     */
    private RankingResponse createRanking(CreateRankingRequest request) {
        log.info("Creating ranking for species ID: {}", request.specieId());

        // Validate species exists
        Specie specie = specieService.getEspecieEntityById(request.specieId());

        // Create new ranking with 0 victories
        Ranking ranking =
                Ranking.builder()
                        .specieId(request.specieId())
                        .victories(1L)
                        .build();
        Ranking savedRanking = rankingRepository.save(ranking);

        log.info("Ranking created successfully for species ID: {} with ID: {}",
                request.specieId(), savedRanking.getRankingId());

        return rankingMapper.toResponse(savedRanking, specie);
    }
}

