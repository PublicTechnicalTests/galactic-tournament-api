package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.specie.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.specie.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieAlreadyExistsException;
import com.technicaltests.mv.galactictournamentapi.exception.SpecieNotFoundException;
import com.technicaltests.mv.galactictournamentapi.mapper.SpecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.SpecieRepository;
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
class SpecieServiceImplTest {

    @Mock
    private SpecieRepository specieRepository;

    @Mock
    private SpecieMapper specieMapper;

    @InjectMocks
    private SpecieService specieService;

    private CreateSpecieRequest createRequest;
    private Specie testSpecie;
    private SpecieResponse testResponse;

    @BeforeEach
    void setUp() {
        createRequest = new CreateSpecieRequest("Vulcan", 100, "Mind meditation");

        testSpecie = new Specie("Vulcan", 100, "Mind meditation");
        testSpecie.setSpecieId(1L);
        testSpecie.setCreationDate(LocalDateTime.now());

        testResponse = new SpecieResponse(1L, "Vulcan", 100, "Mind meditation", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a new species successfully")
    void testCreateSpecieSuccess() {
        // Arrange
        when(specieRepository.existsByName(createRequest.name())).thenReturn(false);
        when(specieMapper.toEntity(createRequest)).thenReturn(testSpecie);
        when(specieRepository.save(any(Specie.class))).thenReturn(testSpecie);
        when(specieMapper.toResponse(testSpecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = specieService.createSpecie(createRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.specieId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Vulcan");
        verify(specieRepository).save(any(Specie.class));
        verify(specieMapper).toResponse(testSpecie);
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate species")
    void testCreateSpecieDuplicate() {
        // Arrange
        when(specieRepository.existsByName(createRequest.name())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> specieService.createSpecie(createRequest))
                .isInstanceOf(SpecieAlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(specieRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve species by ID successfully")
    void testGetSpecieByIdSuccess() {
        // Arrange
        when(specieRepository.findById(1L)).thenReturn(Optional.of(testSpecie));
        when(specieMapper.toResponse(testSpecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = specieService.getSpecieById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.specieId()).isEqualTo(1L);
        verify(specieRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when species not found by ID")
    void testGetSpecieByIdNotFound() {
        // Arrange
        when(specieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> specieService.getSpecieById(999L))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should retrieve species by name successfully")
    void testGetSpecieByNombreSuccess() {
        // Arrange
        when(specieRepository.findByName("Vulcan")).thenReturn(Optional.of(testSpecie));
        when(specieMapper.toResponse(testSpecie)).thenReturn(testResponse);

        // Act
        SpecieResponse result = specieService.getSpecieByNombre("Vulcan");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Vulcan");
        verify(specieRepository).findByName("Vulcan");
    }

    @Test
    @DisplayName("Should throw exception when species not found by name")
    void testGetSpecieByNombreNotFound() {
        // Arrange
        when(specieRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> specieService.getSpecieByNombre("NonExistent"))
                .isInstanceOf(SpecieNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should retrieve all species successfully")
    void testGetAllSpeciesSuccess() {
        // Arrange
        List<Specie> especies = List.of(testSpecie);
        when(specieRepository.findAll()).thenReturn(especies);
        when(specieMapper.toResponse(testSpecie)).thenReturn(testResponse);

        // Act
        List<SpecieResponse> result = specieService.getAllSpecies();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Vulcan");
        verify(specieRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no species exist")
    void testGetAllSpeciesEmpty() {
        // Arrange
        when(specieRepository.findAll()).thenReturn(List.of());

        // Act
        List<SpecieResponse> result = specieService.getAllSpecies();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should check if species exists by ID")
    void testExistsById() {
        // Arrange
        when(specieRepository.existsById(1L)).thenReturn(true);
        when(specieRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThat(specieService.existsById(1L)).isTrue();
        assertThat(specieService.existsById(999L)).isFalse();
    }

    @Test
    @DisplayName("Should retrieve Especie entity by ID successfully")
    void testGetEspecieEntityByIdSuccess() {
        // Arrange
        when(specieRepository.findById(1L)).thenReturn(Optional.of(testSpecie));

        // Act
        Specie result = specieService.getEspecieEntityById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSpecieId()).isEqualTo(1L);
        verify(specieRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when Especie entity not found by ID")
    void testGetEspecieEntityByIdNotFound() {
        // Arrange
        when(specieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> specieService.getEspecieEntityById(999L))
                .isInstanceOf(SpecieNotFoundException.class);
    }
}
