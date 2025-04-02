package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import org.springframework.stereotype.Component;

import co.edu.uniquindio.model.*;

//MapStruct opcion auotomatica
@Component
public class PersonalBodegaMapper {

    public PersonalBodegaDTO toDto(PersonalBodega personalBodega) {

        if(personalBodega == null) {
            return null;
        }
        return new PersonalBodegaDTO(
                personalBodega.getId(),
                personalBodega.getNombre(),
                personalBodega.getApellido(),
                personalBodega.getEmail(),
                personalBodega.getTelefono(),
                personalBodega.getCargo(),
                personalBodega.getFechaIngreso()
        );

    }
    public  PersonalBodega aEntity(PersonalBodegaDTO dto) {
        if(dto == null) {
            return null;
        }
        PersonalBodega personalBodega = new PersonalBodega();
        personalBodega.setId(dto.id());
        personalBodega.setNombre(dto.nombre());
        personalBodega.setApellido(dto.apellido());
        personalBodega.setEmail(dto.email());
        personalBodega.setTelefono(dto.telefono());
        personalBodega.setCargo(dto.cargo());
        personalBodega.setFechaIngreso(dto.fechaIngreso());
        return personalBodega;

    }
}
