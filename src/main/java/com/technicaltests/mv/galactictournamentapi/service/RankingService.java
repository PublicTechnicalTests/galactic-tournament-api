package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;

import java.util.List;

public interface RankingService {
    /**
     * Updates a ranking by incrementing the victory count.
     * <p>
     * Validates that the ranking exists.
     *
     * @param idEspecie the species ID whose ranking to update
     * @return the updated ranking
     */
    boolean addVictory(Long idEspecie);
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
