package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.common.chat.ChatSessionDto;
import co.edu.uniquindio.dto.common.chat.EnviarMensajeDto;
import co.edu.uniquindio.dto.common.chat.MensajeChatDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.common.enums.RemitenteChat;

public interface ChatSessionServicio {


    ChatSessionDto iniciarChat(Long idCliente) throws ElementoNoEncontradoException;

    MensajeChatDto enviarMensaje(EnviarMensajeDto mensaje) throws ElementoNoEncontradoException;

    void finalizarChat(Long idChatSession, RemitenteChat remitente) throws ElementoNoEncontradoException;

}
