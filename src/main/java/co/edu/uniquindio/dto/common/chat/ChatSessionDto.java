package co.edu.uniquindio.dto.common.chat;

import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.model.common.enums.EstadoChat;

import java.time.LocalDateTime;
import java.util.List;

public record ChatSessionDto(

        Long id,
        ClienteDto cliente,
        AgenteVentasDto agenteVentas,
        EstadoChat estado,
        LocalDateTime inicioChat,
        LocalDateTime finChat,
        List<MensajeChatDto> mensajes

) {
}
