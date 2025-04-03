package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.model.*;
//Manual, se puede hacer autmaticametne con un MapStructud
public class PersonalBodegaMapper implements PersonalBodegaMapperImp {
    @Override
    public PersonalBodega toEntity(PersonalBodegaDTO pbDto) {
        if (pbDto == null) {
            return null;
        }
        return new PersonalBodega(
                pbDto.id(),
                pbDto.nombre(),
                pbDto.apellido(),
                pbDto.email(),
                pbDto.telefono(),
                pbDto.cargo(),
                pbDto.fechaIngreso()
        );
    }

    @Override
    public PersonalBodegaDTO toDTO(PersonalBodega pb) {
        if (pb == null) {
            return null;
        }
        return new PersonalBodegaDTO(
                pb.getId(),
                pb.getNombre(),
                pb.getApellido(),
                pb.getEmail(),
                pb.getTelefono(),
                pb.getCargo(),
                pb.getFechaIngreso()
        );
    }
}
