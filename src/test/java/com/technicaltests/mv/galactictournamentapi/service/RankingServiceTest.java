package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.CreateRankingRequest;
import com.technicaltests.mv.galactictournamentapi.dto.RankingResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Ranking;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
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
class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private RankingMapper rankingMapper;

    @Mock
    private EspecieService especieService;

    @InjectMocks
    private RankingService rankingService;

    private Especie especieVulcan;
    private CreateRankingRequest createRequest;
    private Ranking newRanking;
    private RankingResponse rankingResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan
        especieVulcan = new Especie("Vulcan", 100, "Mind meditation");
        especieVulcan.setIdEspecie(1L);
        especieVulcan.setFechaCreacion(LocalDateTime.now());

        // Setup request
        createRequest = new CreateRankingRequest(1L);

        // Setup new ranking
        newRanking = new Ranking(1L);
        newRanking.setIdRanking(1L);

        // Setup response
        rankingResponse = new RankingResponse(1L, 1L, "Vulcan", 0L);
    }

    @Test
    @DisplayName("Should create ranking for species")
    void testCreateRanking() {
        // Arrange
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(rankingRepository.existsByIdEspecie(1L)).thenReturn(false);
        when(rankingRepository.save(any(Ranking.class))).thenReturn(newRanking);
        when(rankingMapper.toResponse(newRanking, especieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingService.createRanking(createRequest);

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
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(rankingRepository.existsByIdEspecie(1L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> rankingService.createRanking(createRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ranking already exists");

        verify(rankingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should add victory to ranking")
    void testAddVictory() {
        // Arrange
        Ranking existingRanking = new Ranking(1L, 5L);
        existingRanking.setIdRanking(1L);

        Ranking updatedRanking = new Ranking(1L, 6L);
        updatedRanking.setIdRanking(1L);

        RankingResponse updatedResponse = new RankingResponse(1L, 1L, "Vulcan", 6L);

        when(rankingRepository.findByIdEspecie(1L)).thenReturn(Optional.of(existingRanking));
        when(rankingRepository.save(any(Ranking.class))).thenReturn(updatedRanking);
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(rankingMapper.toResponse(updatedRanking, especieVulcan)).thenReturn(updatedResponse);

        // Act
        RankingResponse result = rankingService.addVictory(1L);

        // Assert
        assertThat(result.victorias()).isEqualTo(6L);
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    @DisplayName("Should retrieve ranking by ID")
    void testGetRankingById() {
        // Arrange
        when(rankingRepository.findById(1L)).thenReturn(Optional.of(newRanking));
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(rankingMapper.toResponse(newRanking, especieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingService.getRankingById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idRanking()).isEqualTo(1L);
        verify(rankingRepository).findById(1L);
    }

    @Test
    @DisplayName("Should retrieve ranking by species ID")
    void testGetRankingBySpecieId() {
        // Arrange
        when(rankingRepository.findByIdEspecie(1L)).thenReturn(Optional.of(newRanking));
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(rankingMapper.toResponse(newRanking, especieVulcan)).thenReturn(rankingResponse);

        // Act
        RankingResponse result = rankingService.getRankingBySpecieId(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idEspecie()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should check if ranking exists")
    void testRankingExists() {
        // Arrange
        when(rankingRepository.existsByIdEspecie(1L)).thenReturn(true);

        // Act
        boolean exists = rankingService.rankingExists(1L);

        // Assert
        assertThat(exists).isTrue();
        verify(rankingRepository).existsByIdEspecie(1L);
    }

    @Test
    @DisplayName("Should return false when ranking does not exist")
    void testRankingNotExists() {
        // Arrange
        when(rankingRepository.existsByIdEspecie(999L)).thenReturn(false);

        // Act
        boolean exists = rankingService.rankingExists(999L);

        // Assert
        assertThat(exists).isFalse();
    }
}

