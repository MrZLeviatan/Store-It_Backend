package co.edu.uniquindio.dto.common.chat;

import co.edu.uniquindio.model.common.enums.RemitenteChat;

import java.time.LocalDateTime;

public record MensajeChatDto(

        Long id,
        ChatSessionDto chatSession,
        RemitenteChat remitente,
        String contenido,
        LocalDateTime fechaEnvio
) {
}
