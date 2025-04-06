/*
*Conertir datos de entidad a dto
 */
package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.PersonalBodega.CrearPersonalBodegaDTO;
import co.edu.uniquindio.dto.PersonalBodega.EditarPersonalBodegaDTO;
import co.edu.uniquindio.dto.PersonalBodega.PersonalBodegaDTO;
import co.edu.uniquindio.model.PersonalBodega;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface PersonalBodegaMapper {

    @Mapping(target = "fechaIngreso", expression = "java(java.time.LocalDate.now())")
    PersonalBodega toEntity(CrearPersonalBodegaDTO dto);

    PersonalBodegaDTO toDto(PersonalBodega personalBodega);

    void toEntity(EditarPersonalBodegaDTO editarPersonalBodegaDTO, @MappingTarget PersonalBodega personalBodega);

}
