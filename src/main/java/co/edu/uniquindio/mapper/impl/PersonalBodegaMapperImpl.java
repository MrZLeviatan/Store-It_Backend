package co.edu.uniquindio.mapper.impl;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.mapper.PersonalBodegaMapper;
import co.edu.uniquindio.model.mod.PersonalBodega;
import org.springframework.stereotype.Component;

@Component
//Manual, se puede hacer autmaticametne con un MapStructud
public class PersonalBodegaMapperImpl implements PersonalBodegaMapper {
    @Override
    public PersonalBodega toEntity(PersonalBodegaDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PersonalBodega(
                dto.id(),
                dto.nombre(),
                dto.apellido(),
                dto.email(),
                dto.telefono(),
                dto.cargo(),
                dto.fechaIngreso()
        );
    }

    @Override
    public PersonalBodegaDTO toDTO(PersonalBodega entity) {
        if (entity == null) {
            return null;
        }
        return new PersonalBodegaDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getCargo(),
                entity.getFechaIngreso()
        );
    }
}

