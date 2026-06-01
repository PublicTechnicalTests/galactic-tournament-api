package com.technicaltests.mv.galactictournamentapi.specification;

import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification for building dynamic queries on Especie entity.
 *
 * Provides fluent API for constructing complex WHERE clauses with filters.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
public class EspecieSpecification {

    private EspecieSpecification() {
        // Utility class
    }

    /**
     * Creates a specification for filtering species by multiple criteria.
     *
     * @param searchTerm optional search term for species name (like query)
     * @param minPower   optional minimum power level (inclusive)
     * @param maxPower   optional maximum power level (inclusive)
     * @return a Specification that can be used with QueryDSL or Spring Data JPA
     */
    public static Specification<Especie> filterBy(String searchTerm, Integer minPower, Integer maxPower) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search term filter (case-insensitive like on nombre)
            if (searchTerm != null && !searchTerm.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nombre")),
                                "%" + searchTerm.toLowerCase() + "%"
                        )
                );
            }

            // Minimum power filter
            if (minPower != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("poder"), minPower)
                );
            }

            // Maximum power filter
            if (maxPower != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("poder"), maxPower)
                );
            }

            // Return combination of all predicates with AND logic
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

