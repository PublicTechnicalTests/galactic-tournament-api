package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.ranking.CreateRankingRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;

import java.util.List;

public interface RankingService {
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
    RankingResponse createRanking(CreateRankingRequest request);

    /**
     * Updates a ranking by incrementing the victory count.
     * <p>
     * Validates that the ranking exists.
     *
     * @param idEspecie the species ID whose ranking to update
     * @return the updated ranking
     */
    RankingResponse addVictory(Long idEspecie);

    /**
     * Retrieves a ranking by ID.
     *
     * @param id the ranking ID
     * @return the ranking
     */
    RankingResponse getRankingById(Long id);

    /**
     * Retrieves a ranking by species ID.
     *
     * @param idEspecie the species ID
     * @return the ranking if exists
     */
    RankingResponse getRankingBySpecieId(Long idEspecie);

    /**
     * Gets all rankings ordered by victories (highest first).
     *
     * @return list of all rankings sorted by victories descending
     */
    List<RankingResponse> getAllRankingsOrderedByVictorias();

    /**
     * Checks if a ranking exists for a species.
     *
     * @param idEspecie the species ID
     * @return true if ranking exists
     */
    boolean rankingExists(Long idEspecie);
}
