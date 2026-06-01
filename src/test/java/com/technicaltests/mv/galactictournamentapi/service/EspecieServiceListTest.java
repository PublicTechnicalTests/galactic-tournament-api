package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import com.technicaltests.mv.galactictournamentapi.mapper.EspecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.EspecieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for EspecieService listing functionality.
 *
 * Tests the pagination, filtering, and sorting features.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EspecieService List Functionality Tests")
class EspecieServiceListTest {

    @Mock
    private EspecieRepository especieRepository;

    @Mock
    private EspecieMapper especieMapper;

    @InjectMocks
    private EspecieService especieService;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private Especie testEspecie1;
    private Especie testEspecie2;
    private SpecieListQuery testQuery;

    @BeforeEach
    void setUp() {
        testEspecie1 = new Especie("Vulcan", 100, "Mind meditation");
        testEspecie1.setIdEspecie(1L);
        testEspecie1.setFechaCreacion(LocalDateTime.now());

        testEspecie2 = new Especie("Klingon", 150, "Warrior strength");
        testEspecie2.setIdEspecie(2L);
        testEspecie2.setFechaCreacion(LocalDateTime.now());

        testQuery = new SpecieListQuery(0, 20, "poder", "DESC", null, null, null);
    }

    @Test
    @DisplayName("Should list species with default pagination")
    void testListSpeciesWithDefaults() {
        // Arrange
        List<Especie> species = List.of(testEspecie1, testEspecie2);
        Page<Especie> page = new PageImpl<>(species);

        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(testQuery);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);
        verify(especieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should apply search filter correctly")
    void testListSpeciesWithSearchFilter() {
        // Arrange
        SpecieListQuery queryWithSearch = new SpecieListQuery(0, 20, "poder", "DESC", "Vulcan", null, null);
        List<Especie> filteredSpecies = List.of(testEspecie1);
        Page<Especie> page = new PageImpl<>(filteredSpecies);

        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(queryWithSearch);

        // Assert
        assertThat(result.content()).hasSize(1);
        verify(especieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should apply power range filters")
    void testListSpeciesWithPowerFilter() {
        // Arrange
        SpecieListQuery queryWithPowerFilter = new SpecieListQuery(0, 20, "poder", "DESC", null, 100, 200);
        List<Especie> filteredSpecies = List.of(testEspecie2);
        Page<Especie> page = new PageImpl<>(filteredSpecies);

        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(queryWithPowerFilter);

        // Assert
        assertThat(result.content()).hasSize(1);
        verify(especieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should enforce maximum page size")
    void testMaximumPageSizeEnforcement() {
        // Arrange
        SpecieListQuery queryWithLargePageSize = new SpecieListQuery(0, 500, "poder", "DESC", null, null, null);
        List<Especie> species = List.of(testEspecie1);
        Page<Especie> page = new PageImpl<>(species);

        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(queryWithLargePageSize);

        // Assert
        verify(especieRepository).findAll(any(Specification.class), pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageSize()).isLessThanOrEqualTo(100);
    }

    @Test
    @DisplayName("Should handle empty results")
    void testListSpeciesEmptyResults() {
        // Arrange
        Page<Especie> emptyPage = new PageImpl<>(List.of());
        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(testQuery);

        // Assert
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.hasNextPage()).isFalse();
        assertThat(result.hasPreviousPage()).isFalse();
    }

    @Test
    @DisplayName("Should calculate pagination metadata correctly")
    void testPaginationMetadata() {
        // Arrange
        List<Especie> species = List.of(testEspecie1, testEspecie2);
        Page<Especie> page = new PageImpl<>(species, PageRequest.of(0, 2), 5);

        when(especieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = especieService.listSpecies(testQuery);

        // Assert
        assertThat(result.currentPage()).isZero();
        assertThat(result.pageSize()).isGreaterThan(0);
        assertThat(result.totalElements()).isEqualTo(5);
        assertThat(result.hasNextPage()).isTrue();
        assertThat(result.hasPreviousPage()).isFalse();
    }
}






