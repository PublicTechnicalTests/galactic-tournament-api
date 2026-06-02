package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.request.specie.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.response.specie.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Specie;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Especie entities and DTOs.
 * <p>
 * Provides utility methods to convert between domain objects and data transfer objects.
 * This separation maintains clean architecture and prevents exposure of internal entities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Component
public class SpecieMapper {

    /**
     * Converts a CreateSpecieRequest DTO to an Especie entity.
     *
     * @param request the create species request
     * @return the converted Especie entity
     */
    public Specie toEntity(CreateSpecieRequest request) {
        return Specie.builder()
                .name(request.name())
                .power(request.power())
                .ability(request.ability())
                .build()
                ;
    }

    /**
     * Converts an Especie entity to a SpecieResponse DTO.
     *
     * @param specie the Especie entity
     * @return the converted SpecieResponse DTO
     */
    public SpecieResponse toResponse(Specie specie) {
        return new SpecieResponse(
                specie.getSpecieId(),
                specie.getName(),
                specie.getPower(),
                specie.getAbility()
        );
    }
}
