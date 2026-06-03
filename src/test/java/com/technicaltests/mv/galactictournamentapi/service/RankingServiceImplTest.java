package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.response.ranking.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.mapper.RankingMapper;
import com.technicaltests.mv.galactictournamentapi.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RankingService (Features 05 & 06).
 *
 * Tests ranking creation, updates, and retrieval.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RankingService Unit Tests")
class RankingServiceImplTest {

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private RankingMapper rankingMapper;

    @Mock
    private SpecieService specieService;

    @InjectMocks
    private RankingServiceImpl rankingServiceImpl;

    private Specie specieVulcan;
    private Ranking newRanking;
    private RankingResponse rankingResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan
        specieVulcan =
                Specie.builder()
                        .specieId(1L)
                        .name("Vulcan")
                        .power(100)
                        .ability("Mind meditation")
                        .creationDate(LocalDateTime.now())
                        .build();

        // Setup new ranking
        newRanking = Ranking.builder()
                .rankingId(1L)
                .specieId(1L)
                .build();

        // Setup response
        rankingResponse = new RankingResponse(1L, 1L, "Vulcan", 0L);
    }

    @Test
    @DisplayName("Should add victory and create ranking if not exists")
    void testAddVictoryCreatesRankingIfNotExists() {
        // Arrange
        when(rankingRepository.existsBySpecieId(1L)).thenReturn(false);
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingRepository.save(any(Ranking.class))).thenReturn(newRanking);
        when(rankingMapper.toResponse(newRanking, specieVulcan)).thenReturn(rankingResponse);

        // Act
        boolean result = rankingServiceImpl.addVictory(1L);

        // Assert
        assertThat(result).isTrue();
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Should add victory to existing ranking")
    void testAddVictoryToExistingRanking() {
        // Arrange
        Ranking existingRanking = Ranking.builder()
                .rankingId(1L)
                .specieId(1L)
                .victories(5L)
                .build();

        Ranking updatedRanking = Ranking.builder()
                .rankingId(1L)
                .specieId(1L)
                .victories(6L)
                .build();

        when(rankingRepository.existsBySpecieId(1L)).thenReturn(true);
        when(rankingRepository.findBySpecieId(1L)).thenReturn(Optional.of(existingRanking));
        when(rankingRepository.save(any(Ranking.class))).thenReturn(updatedRanking);

        // Act
        boolean result = rankingServiceImpl.addVictory(1L);

        // Assert
        assertThat(result).isTrue();
        verify(rankingRepository).save(any(Ranking.class));
    }


    @Test
    @DisplayName("Should retrieve all rankings ordered by victories")
    void testGetAllRankingsOrderedByVictorias() {
        // Arrange
        List<Ranking> rankings = List.of(newRanking);
        when(rankingRepository.findAllOrderByVictories()).thenReturn(rankings);
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingMapper.toResponse(newRanking, specieVulcan)).thenReturn(rankingResponse);

        // Act
        List<RankingResponse> result = rankingServiceImpl.getAllRankingsOrderedByVictorias();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).idEspecie()).isEqualTo(1L);
        verify(rankingRepository).findAllOrderByVictories();
    }

    @Test
    @DisplayName("Should check if ranking exists")
    void testRankingExists() {
        // Arrange
        when(rankingRepository.existsBySpecieId(1L)).thenReturn(true);

        // Act
        boolean exists = rankingServiceImpl.rankingExists(1L);

        // Assert
        assertThat(exists).isTrue();
        verify(rankingRepository).existsBySpecieId(1L);
    }

    @Test
    @DisplayName("Should return false when ranking does not exist")
    void testRankingNotExists() {
        // Arrange
        when(rankingRepository.existsBySpecieId(999L)).thenReturn(false);

        // Act
        boolean exists = rankingServiceImpl.rankingExists(999L);

        // Assert
        assertThat(exists).isFalse();
    }
}

