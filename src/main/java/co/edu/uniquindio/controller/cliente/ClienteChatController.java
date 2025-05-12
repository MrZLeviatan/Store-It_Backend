package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.common.chat.ChatSessionDto;
import co.edu.uniquindio.dto.common.chat.EnviarMensajeDto;
import co.edu.uniquindio.dto.common.chat.MensajeChatDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.common.enums.RemitenteChat;
import co.edu.uniquindio.service.common.ChatSessionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente/chat")
@RequiredArgsConstructor
public class ClienteChatController {


    private final ChatSessionServicio chatService;


    @PostMapping("/iniciar/{clienteId}")
    public ResponseEntity<MensajeDTO<ChatSessionDto>> iniciarChat(@PathVariable Long clienteId)
            throws ElementoNoEncontradoException {

        ChatSessionDto chatSession = chatService.iniciarChat(clienteId);
        return ResponseEntity.ok(new MensajeDTO<>(false, chatSession));
    }

    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO<MensajeChatDto>> enviarMensaje(@RequestBody EnviarMensajeDto dto)
            throws ElementoNoEncontradoException {

        MensajeChatDto mensaje = chatService.enviarMensaje(dto);
        return ResponseEntity.ok(new MensajeDTO<>(false,mensaje));
    }


    @PostMapping("/finalizar/{chatSessionId}")
    public ResponseEntity<MensajeDTO<String>> finalizarChat(@PathVariable Long chatSessionId)
            throws ElementoNoEncontradoException {

        chatService.finalizarChat(chatSessionId, RemitenteChat.CLIENTE);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Chat finalizado correctamente"));
    }


}