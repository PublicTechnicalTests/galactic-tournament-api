package com.technicaltests.mv.galactictournamentapi.service;

import com.technicaltests.mv.galactictournamentapi.dto.request.specie.PaginatedSpecieResponse;
import com.technicaltests.mv.galactictournamentapi.dto.request.specie.SpecieListQuery;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import com.technicaltests.mv.galactictournamentapi.mapper.SpecieMapper;
import com.technicaltests.mv.galactictournamentapi.repository.SpecieRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
class SpecieServiceImplListTest {

    @Mock
    private SpecieRepository specieRepository;

    @Mock
    private SpecieMapper specieMapper;

    @InjectMocks
    private SpecieServiceImpl specieService;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private Specie testSpecie1;
    private Specie testSpecie2;
    private SpecieListQuery testQuery;

    @BeforeEach
    void setUp() {
        testSpecie1 = Specie.builder()
                .specieId(1L)
                .name("Vulcan")
                .power(100)
                .ability("Mind meditation")
                .creationDate(LocalDateTime.now())
                .build();

        testSpecie2 = Specie.builder()
                .specieId(2L)
                .name("Klingon")
                .power(150)
                .ability("Warrior strength")
                .creationDate(LocalDateTime.now())
                .build();

        testQuery = new SpecieListQuery(0, 20, "power", "DESC", null, null, null);
    }

    @Test
    @DisplayName("Should list species with default pagination")
    void testListSpeciesWithDefaults() {
        // Arrange
        List<Specie> species = List.of(testSpecie1, testSpecie2);
        Page<Specie> page = new PageImpl<>(species);

        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(testQuery);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);
        verify(specieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should apply search filter correctly")
    void testListSpeciesWithSearchFilter() {
        // Arrange
        SpecieListQuery queryWithSearch = new SpecieListQuery(0, 20, "power", "DESC", "Vulcan", null, null);
        List<Specie> filteredSpecies = List.of(testSpecie1);
        Page<Specie> page = new PageImpl<>(filteredSpecies);

        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(queryWithSearch);

        // Assert
        assertThat(result.content()).hasSize(1);
        verify(specieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should apply power range filters")
    void testListSpeciesWithPowerFilter() {
        // Arrange
        SpecieListQuery queryWithPowerFilter = new SpecieListQuery(0, 20, "power", "DESC", null, 100, 200);
        List<Specie> filteredSpecies = List.of(testSpecie2);
        Page<Specie> page = new PageImpl<>(filteredSpecies);

        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(queryWithPowerFilter);

        // Assert
        assertThat(result.content()).hasSize(1);
        verify(specieRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should enforce maximum page size")
    void testMaximumPageSizeEnforcement() {
        // Arrange
        SpecieListQuery queryWithLargePageSize = new SpecieListQuery(0, 500, "power", "DESC", null, null, null);
        List<Specie> species = List.of(testSpecie1);
        Page<Specie> page = new PageImpl<>(species);

        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(queryWithLargePageSize);

        // Assert
        verify(specieRepository).findAll(any(Specification.class), pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageSize()).isLessThanOrEqualTo(100);
    }

    @Test
    @DisplayName("Should handle empty results")
    void testListSpeciesEmptyResults() {
        // Arrange
        Page<Specie> emptyPage = new PageImpl<>(List.of());
        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(testQuery);

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
        List<Specie> species = List.of(testSpecie1, testSpecie2);
        Page<Specie> page = new PageImpl<>(species, PageRequest.of(0, 2), 5);

        when(specieRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // Act
        PaginatedSpecieResponse result = specieService.listSpecies(testQuery);

        // Assert
        assertThat(result.currentPage()).isZero();
        assertThat(result.pageSize()).isGreaterThan(0);
        assertThat(result.totalElements()).isEqualTo(5);
        assertThat(result.hasNextPage()).isTrue();
        assertThat(result.hasPreviousPage()).isFalse();
    }
}






