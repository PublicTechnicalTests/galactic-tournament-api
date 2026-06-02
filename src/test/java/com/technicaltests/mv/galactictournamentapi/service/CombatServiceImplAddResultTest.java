package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.combat.AddCombatResultRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.combat.CombatResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combat;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
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
 * Unit tests for adding battle results (Feature 04).
 * <p>
 * Tests the manual battle result addition functionality.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Feature 04: Add Battle Result Tests")
class CombatServiceImplAddResultTest {

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
    private AddCombatResultRequest battleResultRequest;
    private Combat savedCombat;
    private CombatResponse combatResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan
        specieVulcan = new Specie("Vulcan", 100, "Mind meditation");
        specieVulcan.setSpecieId(1L);
        specieVulcan.setCreationDate(LocalDateTime.now());

        // Setup Klingon
        specieKlingon = new Specie("Klingon", 150, "Warrior strength");
        specieKlingon.setSpecieId(2L);
        specieKlingon.setCreationDate(LocalDateTime.now());

        // Setup request specifying Klingon as winner
        battleResultRequest = new AddCombatResultRequest(1L, 2L, 2L);

        // Setup saved battle
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
    @DisplayName("Should add battle result with valid winner")
    void testAddBattleResultValid() {
        // Arrange
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(specieService.getEspecieEntityById(2L)).thenReturn(specieKlingon);
        when(combatRepository.save(any(Combat.class))).thenReturn(savedCombat);
        when(combatMapper.toResponse(savedCombat, specieKlingon)).thenReturn(combatResponse);

        // Act
        CombatResponse result = combatService.addBattleResult(battleResultRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idGanador()).isEqualTo(2L);
        assertThat(result.nombreGanador()).isEqualTo("Klingon");
        verify(combatRepository).save(any(Combat.class));
    }

    @Test
    @DisplayName("Should throw exception when winner is not one of contenders")
    void testAddBattleResultInvalidWinner() {
        // Arrange
        AddCombatResultRequest invalidRequest = new AddCombatResultRequest(1L, 2L, 999L);
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(specieService.getEspecieEntityById(2L)).thenReturn(specieKlingon);

        // Act & Assert
        assertThatThrownBy(() -> combatService.addBattleResult(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Winner ID must be one of the contenders");

        verify(combatRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when contenders are the same")
    void testAddBattleResultSameContenders() {
        // Arrange
        AddCombatResultRequest sameContendersRequest = new AddCombatResultRequest(1L, 1L, 1L);
        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);

        // Act & Assert
        assertThatThrownBy(() -> combatService.addBattleResult(sameContendersRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Contenders must be different species");

        verify(combatRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow either contender as winner")
    void testAddBattleResultVulcanWins() {
        // Arrange
        AddCombatResultRequest vulcanWinsRequest = new AddCombatResultRequest(1L, 2L, 1L);
        CombatResponse vulcanWinsResponse = new CombatResponse(2L, 1L, 2L, 1L, "Vulcan", 100L, LocalDateTime.now());

        when(specieService.getEspecieEntityById(1L)).thenReturn(specieVulcan);
        when(specieService.getEspecieEntityById(2L)).thenReturn(specieKlingon);

        Combat vulcanWinsCombat =
                Combat.builder()
                        .firstContenderId(1L)
                        .secondContenderId(2L)
                        .winnerId(1L)
                        .build();
        vulcanWinsCombat.setCombatId(2L);
        vulcanWinsCombat.setFechaCreacion(LocalDateTime.now());

        when(combatRepository.save(any(Combat.class))).thenReturn(vulcanWinsCombat);
        when(combatMapper.toResponse(vulcanWinsCombat, specieVulcan)).thenReturn(vulcanWinsResponse);

        // Act
        CombatResponse result = combatService.addBattleResult(vulcanWinsRequest);

        // Assert
        assertThat(result.idGanador()).isEqualTo(1L);
        assertThat(result.nombreGanador()).isEqualTo("Vulcan");
        verify(combatRepository).save(any(Combat.class));
    }
}

