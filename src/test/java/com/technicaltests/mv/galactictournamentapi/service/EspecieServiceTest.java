package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.EspecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.EspecieRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EspecieService.
 *
 * Tests the business logic layer for species management operations.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EspecieService Unit Tests")
class EspecieServiceTest {

    @Mock
    private EspecieRepository especieRepository;

    @Mock
    private EspecieMapper especieMapper;

    @InjectMocks
    private EspecieService especieService;

    private CreateSpecieRequest createRequest;
    private Especie testEspecie;
    private SpecieResponse testResponse;

    @BeforeEach
    void setUp() {
        createRequest = new CreateSpecieRequest("Vulcan", 100, "Mind meditation");

        testEspecie = new Especie("Vulcan", 100, "Mind meditation");
        testEspecie.setIdEspecie(1L);
        testEspecie.setFechaCreacion(LocalDateTime.now());

        testResponse = new SpecieResponse(1L, "Vulcan", 100, "Mind meditation", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a new species successfully")
    void testCreateSpecieSuccess() {
        // Arrange
        when(especieRepository.existsByNombre(createRequest.nombre())).thenReturn(false);
        when(especieMapper.toEntity(createRequest)).thenReturn(testEspecie);
        when(especieRepository.save(any(Especie.class))).thenReturn(testEspecie);
        when(especieMapper.toResponse(testEspecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = especieService.createSpecie(createRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idEspecie()).isEqualTo(1L);
        assertThat(result.nombre()).isEqualTo("Vulcan");
        verify(especieRepository).save(any(Especie.class));
        verify(especieMapper).toResponse(testEspecie);
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate species")
    void testCreateSpecieDuplicate() {
        // Arrange
        when(especieRepository.existsByNombre(createRequest.nombre())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> especieService.createSpecie(createRequest))
                .isInstanceOf(SpecieAlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(especieRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve species by ID successfully")
    void testGetSpecieByIdSuccess() {
        // Arrange
        when(especieRepository.findById(1L)).thenReturn(Optional.of(testEspecie));
        when(especieMapper.toResponse(testEspecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = especieService.getSpecieById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idEspecie()).isEqualTo(1L);
        verify(especieRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when species not found by ID")
    void testGetSpecieByIdNotFound() {
        // Arrange
        when(especieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> especieService.getSpecieById(999L))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should retrieve species by name successfully")
    void testGetSpecieByNombreSuccess() {
        // Arrange
        when(especieRepository.findByNombre("Vulcan")).thenReturn(Optional.of(testEspecie));
        when(especieMapper.toResponse(testEspecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = especieService.getSpecieByNombre("Vulcan");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.nombre()).isEqualTo("Vulcan");
        verify(especieRepository).findByNombre("Vulcan");
    }

    @Test
    @DisplayName("Should throw exception when species not found by name")
    void testGetSpecieByNombreNotFound() {
        // Arrange
        when(especieRepository.findByNombre(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> especieService.getSpecieByNombre("NonExistent"))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should retrieve all species successfully")
    void testGetAllSpeciesSuccess() {
        // Arrange
        List<Especie> especies = List.of(testEspecie);
        when(especieRepository.findAll()).thenReturn(especies);
        when(especieMapper.toResponse(testEspecie)).thenReturn(testResponse);

        // Act
        List<SpecieResponse> result = especieService.getAllSpecies();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).nombre()).isEqualTo("Vulcan");
        verify(especieRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no species exist")
    void testGetAllSpeciesEmpty() {
        // Arrange
        when(especieRepository.findAll()).thenReturn(List.of());

        // Act
        List<SpecieResponse> result = especieService.getAllSpecies();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should check if species exists by ID")
    void testExistsById() {
        // Arrange
        when(especieRepository.existsById(1L)).thenReturn(true);
        when(especieRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThat(especieService.existsById(1L)).isTrue();
        assertThat(especieService.existsById(999L)).isFalse();
    }

    @Test
    @DisplayName("Should retrieve Especie entity by ID successfully")
    void testGetEspecieEntityByIdSuccess() {
        // Arrange
        when(especieRepository.findById(1L)).thenReturn(Optional.of(testEspecie));

        // Act
        Especie result = especieService.getEspecieEntityById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdEspecie()).isEqualTo(1L);
        verify(especieRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when Especie entity not found by ID")
    void testGetEspecieEntityByIdNotFound() {
        // Arrange
        when(especieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> especieService.getEspecieEntityById(999L))
                .isInstanceOf(SpecieNotFoundException.class);
    }
}

