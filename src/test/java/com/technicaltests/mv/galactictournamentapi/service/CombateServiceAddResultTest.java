package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.AddBattleResultRequest;
import com.technicaltests.mv.galactictournamentapi.dto.BattleResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Combate;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
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
 * Unit tests for adding battle results (Feature 04).
 *
 * Tests the manual battle result addition functionality.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Feature 04: Add Battle Result Tests")
class CombateServiceAddResultTest {

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
    private AddBattleResultRequest battleResultRequest;
    private Combate savedCombate;
    private BattleResponse battleResponse;

    @BeforeEach
    void setUp() {
        // Setup Vulcan
        especieVulcan = new Especie("Vulcan", 100, "Mind meditation");
        especieVulcan.setIdEspecie(1L);
        especieVulcan.setFechaCreacion(LocalDateTime.now());

        // Setup Klingon
        especieKlingon = new Especie("Klingon", 150, "Warrior strength");
        especieKlingon.setIdEspecie(2L);
        especieKlingon.setFechaCreacion(LocalDateTime.now());

        // Setup request specifying Klingon as winner
        battleResultRequest = new AddBattleResultRequest(1L, 2L, 2L);

        // Setup saved battle
        savedCombate = new Combate(1L, 2L, 2L);
        savedCombate.setIdCombate(1L);
        savedCombate.setFechaCreacion(LocalDateTime.now());

        // Setup response
        battleResponse = new BattleResponse(1L, 1L, 2L, 2L, "Klingon", 150L, LocalDateTime.now());
    }

    @Test
    @DisplayName("Should add battle result with valid winner")
    void testAddBattleResultValid() {
        // Arrange
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(especieService.getEspecieEntityById(2L)).thenReturn(especieKlingon);
        when(combateRepository.save(any(Combate.class))).thenReturn(savedCombate);
        when(combateMapper.toResponse(savedCombate, especieKlingon)).thenReturn(battleResponse);

        // Act
        BattleResponse result = combateService.addBattleResult(battleResultRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idGanador()).isEqualTo(2L);
        assertThat(result.nombreGanador()).isEqualTo("Klingon");
        verify(combateRepository).save(any(Combate.class));
    }

    @Test
    @DisplayName("Should throw exception when winner is not one of contenders")
    void testAddBattleResultInvalidWinner() {
        // Arrange
        AddBattleResultRequest invalidRequest = new AddBattleResultRequest(1L, 2L, 999L);
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(especieService.getEspecieEntityById(2L)).thenReturn(especieKlingon);

        // Act & Assert
        assertThatThrownBy(() -> combateService.addBattleResult(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Winner ID must be one of the contenders");

        verify(combateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when contenders are the same")
    void testAddBattleResultSameContenders() {
        // Arrange
        AddBattleResultRequest sameContendersRequest = new AddBattleResultRequest(1L, 1L, 1L);
        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);

        // Act & Assert
        assertThatThrownBy(() -> combateService.addBattleResult(sameContendersRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Contenders must be different species");

        verify(combateRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow either contender as winner")
    void testAddBattleResultVulcanWins() {
        // Arrange
        AddBattleResultRequest vulcanWinsRequest = new AddBattleResultRequest(1L, 2L, 1L);
        BattleResponse vulcanWinsResponse = new BattleResponse(2L, 1L, 2L, 1L, "Vulcan", 100L, LocalDateTime.now());

        when(especieService.getEspecieEntityById(1L)).thenReturn(especieVulcan);
        when(especieService.getEspecieEntityById(2L)).thenReturn(especieKlingon);

        Combate vulcanWinsCombate = new Combate(1L, 2L, 1L);
        vulcanWinsCombate.setIdCombate(2L);
        vulcanWinsCombate.setFechaCreacion(LocalDateTime.now());

        when(combateRepository.save(any(Combate.class))).thenReturn(vulcanWinsCombate);
        when(combateMapper.toResponse(vulcanWinsCombate, especieVulcan)).thenReturn(vulcanWinsResponse);

        // Act
        BattleResponse result = combateService.addBattleResult(vulcanWinsRequest);

        // Assert
        assertThat(result.idGanador()).isEqualTo(1L);
        assertThat(result.nombreGanador()).isEqualTo("Vulcan");
        verify(combateRepository).save(any(Combate.class));
    }
}

