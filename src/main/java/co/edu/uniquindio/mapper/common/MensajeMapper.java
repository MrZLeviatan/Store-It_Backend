package co.edu.uniquindio.mapper.common;

import co.edu.uniquindio.dto.common.chat.MensajeChatDto;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.model.common.Mensaje;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {QuantityAreaConverter.class, SedeMapper.class})
public interface MensajeMapper {

    MensajeChatDto toMensajeDTO(Mensaje mensaje);

}
