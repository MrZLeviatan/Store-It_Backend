package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses =
        {QuantityAreaConverter.class})
public interface SedeMapper {



    SedeDto toDto(Sede sede);




}
