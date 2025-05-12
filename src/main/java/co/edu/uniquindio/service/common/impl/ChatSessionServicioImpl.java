package co.edu.uniquindio.service.common.impl;


import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.chat.ChatSessionDto;
import co.edu.uniquindio.dto.common.chat.EnviarMensajeDto;
import co.edu.uniquindio.dto.common.chat.MensajeChatDto;
import co.edu.uniquindio.exception.ElementoNoActivadoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.common.ChatSessionMapper;
import co.edu.uniquindio.mapper.common.MensajeMapper;
import co.edu.uniquindio.model.common.ChatSession;
import co.edu.uniquindio.model.common.Mensaje;
import co.edu.uniquindio.model.common.enums.EstadoChat;
import co.edu.uniquindio.model.common.enums.RemitenteChat;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.repository.common.ChatSessionRepo;
import co.edu.uniquindio.repository.common.MensajeRepo;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.common.ChatSessionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatSessionServicioImpl implements ChatSessionServicio {


    private final ChatSessionMapper chatSessionMapper;
    private final MensajeMapper mensajeMapper;
    private final ChatSessionRepo chatSessionRepo;
    private final MensajeRepo mensajeRepo;
    private final ClienteRepo clienteRepo;
    private final AgenteVentasRepo agenteVentasRepo;



    @Override
    public ChatSessionDto iniciarChat(Long idCliente) throws ElementoNoEncontradoException {
        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        List<AgenteVentas> agentesDisponibles = agenteVentasRepo.findAgentesDisponibles();


        if (agentesDisponibles.isEmpty()) {
            throw new ElementoNoEncontradoException(MensajeError.AGENTE_NO_DISPONIBLE);}

        // Seleccionar un agente aleatoriamente
        AgenteVentas agenteAsignado = agentesDisponibles.get(new Random().nextInt(agentesDisponibles.size()));

        ChatSession chatSession = new ChatSession();
        chatSession.setCliente(cliente);
        chatSession.setAgenteVentas(agenteAsignado);
        chatSession.setEstado(EstadoChat.ACTIVO);
        chatSession.setInicioChat(LocalDateTime.now());

        chatSessionRepo.save(chatSession);

        return chatSessionMapper.toDTO(chatSession);
    }

    @Override
    public MensajeChatDto enviarMensaje(EnviarMensajeDto mensajeDto) throws ElementoNoEncontradoException {

        ChatSession chatSession = chatSessionRepo.findById(mensajeDto.idChatSession()).
                orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.CHAT_NO_ENCONTRADO));

        if (!chatSession.getEstado().equals(EstadoChat.ACTIVO)) {
            throw new ElementoNoActivadoException("La sesión de chat ya no está activa");
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setChatSession(chatSession);
        mensaje.setContenido(mensajeDto.contenido());
        mensaje.setRemitente(mensajeDto.remitente());
        mensaje.setFechaEnvio(LocalDateTime.now());

        mensajeRepo.save(mensaje);

        return mensajeMapper.toMensajeDTO(mensaje);
    }


    @Override
    public void finalizarChat(Long idChatSession, RemitenteChat remitente) throws ElementoNoEncontradoException {
        ChatSession chatSession = chatSessionRepo.findById(idChatSession)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.CHAT_NO_ENCONTRADO));

        if (!chatSession.getEstado().equals(EstadoChat.ACTIVO)) {
            return; // Ya estaba cerrada
        }

        chatSession.setEstado(EstadoChat.FINALIZADO);
        chatSession.setFinChat(LocalDateTime.now());
        chatSessionRepo.save(chatSession);

        // Opcional: guardar un mensaje del cierre
        Mensaje mensajeCierre = new Mensaje();
        mensajeCierre.setChatSession(chatSession);
        mensajeCierre.setContenido(remitente == RemitenteChat.AGENTE ?
                "El agente finalizó la conversación." : "El cliente finalizó la conversación.");
        mensajeCierre.setFechaEnvio(LocalDateTime.now());
        mensajeCierre.setRemitente(remitente);

        mensajeRepo.save(mensajeCierre);
    }

}
