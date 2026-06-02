package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.ranking.CreateRankingRequest;
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
    private CreateRankingRequest createRequest;
    private Ranking newRanking;
    private RankingResponse rankingResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan
        specieVulcan =
                Specie.builder()
                        .name("Vulcan")
                        .power(100)
                        .ability("Mind meditation")
                        .build();
        specieVulcan.setSpecieId(1L);
        specieVulcan.setCreationDate(LocalDateTime.now());

        // Setup request
        createRequest = new CreateRankingRequest(1L);

        // Setup new ranking
        newRanking = Ranking.builder().rankingId(1L).build();
        newRanking.setRankingId(1L);

        // Setup response
        rankingResponse = new RankingResponse(1L, 1L, "Vulcan", 0L);
    }

    @Test
    @DisplayName("Should create ranking for species")
    void testCreateRanking() {
        // Arrange
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingRepository.existsBySpecieId(1L)).thenReturn(false);
        when(rankingRepository.save(any(Ranking.class))).thenReturn(newRanking);
        when(rankingMapper.toResponse(newRanking, specieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingServiceImpl.createRanking(createRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idEspecie()).isEqualTo(1L);
        assertThat(result.victorias()).isZero();
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Should throw exception when ranking already exists")
    void testCreateRankingAlreadyExists() {
        // Arrange
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingRepository.existsBySpecieId(1L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> rankingServiceImpl.createRanking(createRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ranking already exists");

        verify(rankingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should add victory to ranking")
    void testAddVictory() {
        // Arrange
        Ranking existingRanking =
                Ranking.builder()
                        .rankingId(1L)
                        .specieId(5L)
                        .build();
        existingRanking.setRankingId(1L);

        Ranking updatedRanking =                 Ranking.builder()
                .rankingId(1L)
                .specieId(6L)
                .build();
        updatedRanking.setRankingId(1L);

        RankingResponse updatedResponse = new RankingResponse(1L, 1L, "Vulcan", 6L);

        when(rankingRepository.findBySpecieId(1L)).thenReturn(Optional.of(existingRanking));
        when(rankingRepository.save(any(Ranking.class))).thenReturn(updatedRanking);
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingMapper.toResponse(updatedRanking, specieVulcan)).thenReturn(updatedResponse);

        // Act
        RankingResponse result = rankingServiceImpl.addVictory(1L);

        // Assert
        assertThat(result.victorias()).isEqualTo(6L);
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Should retrieve ranking by ID")
    void testGetRankingById() {
        // Arrange
        when(rankingRepository.findById(1L)).thenReturn(Optional.of(newRanking));
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingMapper.toResponse(newRanking, specieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingServiceImpl.getRankingById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idRanking()).isEqualTo(1L);
        verify(rankingRepository).findById(1L);
    }

    @Test
    @DisplayName("Should retrieve ranking by species ID")
    void testGetRankingBySpecieId() {
        // Arrange
        when(rankingRepository.findBySpecieId(1L)).thenReturn(Optional.of(newRanking));
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(rankingMapper.toResponse(newRanking, specieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingServiceImpl.getRankingBySpecieId(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idEspecie()).isEqualTo(1L);
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

