package co.edu.uniquindio.dto.common.chat;

import co.edu.uniquindio.model.common.enums.RemitenteChat;

public record EnviarMensajeDto(

        Long idChatSession,
        RemitenteChat remitente,
        String contenido

) {
}
