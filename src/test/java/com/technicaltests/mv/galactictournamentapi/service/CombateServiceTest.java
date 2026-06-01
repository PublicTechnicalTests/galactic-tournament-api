package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.StartBattleRequest;
import com.technicaltests.mv.galactictournamentapi.dto.BattleResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combate;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.CombateMapper;
import com.technicaltests.mv.galactictournamentapi.repository.CombateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CombateService.
 *
 * Tests the business logic layer for battle management.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CombateService Unit Tests")
class CombateServiceTest {

    @Mock
    private CombateRepository combateRepository;

    @Mock
    private CombateMapper combateMapper;

    @Mock
    private EspecieService especieService;

    @InjectMocks
    private CombateService combateService;

    private Especie especieVulcan;
    private Especie especieKlingon;
    private StartBattleRequest battleRequest;
    private Combate savedCombate;
    private BattleResponse battleResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan (100 power)
        especieVulcan = new Especie("Vulcan", 100, "Mind meditation");
        especieVulcan.setIdEspecie(1L);
        especieVulcan.setFechaCreacion(LocalDateTime.now());

        // Setup Klingon (150 power)
        especieKlingon = new Especie("Klingon", 150, "Warrior strength");
        especieKlingon.setIdEspecie(2L);
        especieKlingon.setFechaCreacion(LocalDateTime.now());

        // Setup request
        battleRequest = new StartBattleRequest(1L, 2L);

        // Setup saved battle (Klingon wins because of higher power)
        savedCombate = new Combate(1L, 2L, 2L);
        savedCombate.setIdCombate(1L);
        savedCombate.setFechaCreacion(LocalDateTime.now());

        // Setup response
        battleResponse = new BattleResponse(1L, 1L, 2L, 2L, "Klingon", 150, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should start a battle and determine winner by power")
    void testStartBattleWinnerByPower() {
        // Arrange
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(especieService.getEspecieEntityById(2L)).thenReturn(especieKlingon);
        when(combateRepository.save(any(Combate.class))).thenReturn(savedCombate);
        when(combateMapper.toResponse(savedCombate, especieKlingon)).thenReturn(battleResponse);

        // Act
        BattleResponse result = combateService.startBattle(battleRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idGanador()).isEqualTo(2L);
        assertThat(result.nombreGanador()).isEqualTo("Klingon");
        assertThat(result.poderGanador()).isEqualTo(150);
        verify(combateRepository).save(any(Combate.class));
    }

    @Test
    @DisplayName("Should determine winner alphabetically when power is equal")
    void testStartBattleWinnerAlphabetical() {
        // Arrange - Setup species with equal power
        Especie specieA = new Especie("Andorian", 100, "Logical");
        specieA.setIdEspecie(3L);
        specieA.setFechaCreacion(LocalDateTime.now());

        Especie specieB = new Especie("Betazoid", 100, "Telepathy");
        specieB.setIdEspecie(4L);
        specieB.setFechaCreacion(LocalDateTime.now());

        StartBattleRequest equalPowerBattle = new StartBattleRequest(3L, 4L);

        Combate equalPowerCombate = new Combate(3L, 4L, 3L); // Andorian wins (comes first alphabetically)
        equalPowerCombate.setIdCombate(2L);
        equalPowerCombate.setFechaCreacion(LocalDateTime.now());

        BattleResponse equalPowerResponse = new BattleResponse(2L, 3L, 4L, 3L, "Andorian", 100, LocalDateTime.now());

        when(especieService.getEspecieEntityById(3L)).thenReturn(specieA);
        when(especieService.getEspecieEntityById(4L)).thenReturn(specieB);
        when(combateRepository.save(any(Combate.class))).thenReturn(equalPowerCombate);
        when(combateMapper.toResponse(equalPowerCombate, specieA)).thenReturn(equalPowerResponse);

        // Act
        BattleResponse result = combateService.startBattle(equalPowerBattle);

        // Assert
        assertThat(result.idGanador()).isEqualTo(3L);
        assertThat(result.nombreGanador()).isEqualTo("Andorian");
        verify(combateRepository).save(any(Combate.class));
    }

    @Test
    @DisplayName("Should throw exception when first contender not found")
    void testStartBattleFirstContenderNotFound() {
        // Arrange
        when(especieService.getEspecieEntityById(1L))
                .thenThrow(new SpecieNotFoundException("Species not found"));

        // Act & Assert
        assertThatThrownBy(() -> combateService.startBattle(battleRequest))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");

        verify(combateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when second contender not found")
    void testStartBattleSecondContenderNotFound() {
        // Arrange
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(especieService.getEspecieEntityById(2L))
                .thenThrow(new SpecieNotFoundException("Species not found"));

        // Act & Assert
        assertThatThrownBy(() -> combateService.startBattle(battleRequest))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");

        verify(combateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should count battles won by species")
    void testCountBattlesWonBySpecies() {
        // Arrange
        when(combateRepository.countByIdGanador(2L)).thenReturn(5L);

        // Act
        long count = combateService.countBattlesWonBySpecies(2L);

        // Assert
        assertThat(count).isEqualTo(5L);
        verify(combateRepository).countByIdGanador(2L);
    }

    @Test
    @DisplayName("Should return zero wins for species with no battles")
    void testCountBattlesWonBySpeciesZero() {
        // Arrange
        when(combateRepository.countByIdGanador(999L)).thenReturn(0L);

        // Act
        long count = combateService.countBattlesWonBySpecies(999L);

        // Assert
        assertThat(count).isZero();
    }
}

