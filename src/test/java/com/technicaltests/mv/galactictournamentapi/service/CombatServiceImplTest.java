package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.combat.StartCombatRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.CombatMapper;
import com.technicaltests.mv.galactictournamentapi.repository.CombatRepository;
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
 * <p>
 * Tests the business logic layer for battle management.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CombateService Unit Tests")
class CombatServiceImplTest {

    @Mock
    private CombatRepository combatRepository;

    @Mock
    private CombatMapper combatMapper;

    @Mock
    private SpecieService specieService;

    @InjectMocks
    private CombatServiceImpl combatService;

    private Specie specieVulcan;
    private Specie specieKlingon;
    private StartCombatRequest battleRequest;
    private Combat savedCombat;
    private CombatResponse combatResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan (100 power)
        specieVulcan = new Specie("Vulcan", 100, "Mind meditation");
        specieVulcan.setSpecieId(1L);
        specieVulcan.setCreationDate(LocalDateTime.now());

        // Setup Klingon (150 power)
        specieKlingon = new Specie("Klingon", 150, "Warrior strength");
        specieKlingon.setSpecieId(2L);
        specieKlingon.setCreationDate(LocalDateTime.now());

        // Setup request
        battleRequest = new StartCombatRequest(1L, 2L);

        // Setup saved battle (Klingon wins because of higher power)
        savedCombat =
                Combat.builder()
                        .firstContenderId(1L)
                        .secondContenderId(2L)
                        .winnerId(2L)
                        .build();
        savedCombat.setCombatId(1L);
        savedCombat.setFechaCreacion(LocalDateTime.now());

        // Setup response
        combatResponse = new CombatResponse(1L, 1L, 2L, 2L, "Klingon", 150L, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should start a battle and determine winner by power")
    void testStartBattleWinnerByPower() {
        // Arrange
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(specieService.getEspecieEntityById(2L)).thenReturn(specieKlingon);
        when(combatRepository.save(any(Combat.class))).thenReturn(savedCombat);
        when(combatMapper.toResponse(savedCombat, specieKlingon)).thenReturn(combatResponse);

        // Act
        CombatResponse result = combatService.startBattle(battleRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idGanador()).isEqualTo(2L);
        assertThat(result.nombreGanador()).isEqualTo("Klingon");
        assertThat(result.poderGanador()).isEqualTo(150);
        verify(combatRepository).save(any(Combat.class));
    }

    @Test
    @DisplayName("Should determine winner alphabetically when power is equal")
    void testStartBattleWinnerAlphabetical() {
        // Arrange - Setup species with equal power
        Specie specieA = new Specie("Andorian", 100, "Logical");
        specieA.setSpecieId(3L);
        specieA.setCreationDate(LocalDateTime.now());

        Specie specieB = new Specie("Betazoid", 100, "Telepathy");
        specieB.setSpecieId(4L);
        specieB.setCreationDate(LocalDateTime.now());

        StartCombatRequest equalPowerBattle = new StartCombatRequest(3L, 4L);

        Combat equalPowerCombat =
                Combat.builder()
                        .firstContenderId(3L)
                        .secondContenderId(4L)
                        .winnerId(3L)
                        .build();// Andorian wins (comes first alphabetically)
        equalPowerCombat.setCombatId(2L);
        equalPowerCombat.setFechaCreacion(LocalDateTime.now());

        CombatResponse equalPowerResponse = new CombatResponse(2L, 3L, 4L, 3L, "Andorian", 100L, LocalDateTime.now());

        when(specieService.getEspecieEntityById(3L)).thenReturn(specieA);
        when(specieService.getEspecieEntityById(4L)).thenReturn(specieB);
        when(combatRepository.save(any(Combat.class))).thenReturn(equalPowerCombat);
        when(combatMapper.toResponse(equalPowerCombat, specieA)).thenReturn(equalPowerResponse);

        // Act
        CombatResponse result = combatService.startBattle(equalPowerBattle);

        // Assert
        assertThat(result.idGanador()).isEqualTo(3L);
        assertThat(result.nombreGanador()).isEqualTo("Andorian");
        verify(combatRepository).save(any(Combat.class));
    }

    @Test
    @DisplayName("Should throw exception when first contender not found")
    void testStartBattleFirstContenderNotFound() {
        // Arrange
        when(specieService.getEspecieEntityById(1L))
                .thenThrow(new SpecieNotFoundException("Species not found"));

        // Act & Assert
        assertThatThrownBy(() -> combatService.startBattle(battleRequest))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");

        verify(combatRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when second contender not found")
    void testStartBattleSecondContenderNotFound() {
        // Arrange
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(specieService.getEspecieEntityById(2L))
                .thenThrow(new SpecieNotFoundException("Species not found"));

        // Act & Assert
        assertThatThrownBy(() -> combatService.startBattle(battleRequest))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");

        verify(combatRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should count battles won by species")
    void testCountBattlesWonBySpecies() {
        // Arrange
        when(combatRepository.countByWinnerId(2L)).thenReturn(5L);

        // Act
        long count = combatService.countBattlesWonBySpecies(2L);

        // Assert
        assertThat(count).isEqualTo(5L);
        verify(combatRepository).countByWinnerId(2L);
    }

    @Test
    @DisplayName("Should return zero wins for species with no battles")
    void testCountBattlesWonBySpeciesZero() {
        // Arrange
        when(combatRepository.countByWinnerId(999L)).thenReturn(0L);

        // Act
        long count = combatService.countBattlesWonBySpecies(999L);

        // Assert
        assertThat(count).isZero();
    }
}

