package com.technicaltests.mv.galactictournamentapi.mapper;

import com.technicaltests.mv.galactictournamentapi.dto.CreateSpecieRequest;
import com.technicaltests.mv.galactictournamentapi.dto.SpecieResponse;
import com.technicaltests.mv.galactictournamentapi.entity.Especie;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Especie entities and DTOs.
 *
 * Provides utility methods to convert between domain objects and data transfer objects.
 * This separation maintains clean architecture and prevents exposure of internal entities.
 *
 * @author Backend Team
 * @version 1.0
 * @since 2026
 */
@Component
public class EspecieMapper {

    /**
     * Converts a CreateSpecieRequest DTO to an Especie entity.
     *
     * @param request the create species request
     * @return the converted Especie entity
     */
    public Especie toEntity(CreateSpecieRequest request) {
        return new Especie(
                request.nombre(),
                request.poder(),
                request.habilidad()
        );
    }

    /**
     * Converts an Especie entity to a SpecieResponse DTO.
     *
     * @param especie the Especie entity
     * @return the converted SpecieResponse DTO
     */
    public SpecieResponse toResponse(Especie especie) {
        return new SpecieResponse(
                especie.getIdEspecie(),
                especie.getNombre(),
                especie.getPoder(),
                especie.getHabilidad(),
                especie.getFechaCreacion()
        );
    }
}
