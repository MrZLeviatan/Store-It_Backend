/*
*Conertir datos de entidad a dto
 */
package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.model.mod.PersonalBodega;

import org.springframework.stereotype.Component;


@Component
public interface PersonalBodegaMapper {
    PersonalBodega toEntity(PersonalBodegaDTO dto);
    PersonalBodegaDTO toDTO(PersonalBodega entity);
}
