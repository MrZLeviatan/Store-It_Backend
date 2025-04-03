
package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.model.*;


public interface PersonalBodegaMapperImp {
    PersonalBodega toEntity(PersonalBodegaDTO pbDto);
    PersonalBodegaDTO toDTO(PersonalBodega pb);
}
