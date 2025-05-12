package co.edu.uniquindio.controller.agenteVentas;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.common.chat.EnviarMensajeDto;
import co.edu.uniquindio.dto.common.chat.MensajeChatDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.common.enums.RemitenteChat;
import co.edu.uniquindio.service.common.ChatSessionServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agente-ventas/chat")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class AgenteChatController {

    private final ChatSessionServicio chatService;

    @PostMapping("/enviar")
    public ResponseEntity<MensajeDTO<MensajeChatDto>> enviarMensaje(@RequestBody EnviarMensajeDto dto)
            throws ElementoNoEncontradoException {

        MensajeChatDto mensaje = chatService.enviarMensaje(dto);
        return ResponseEntity.ok(new MensajeDTO<>(false, mensaje));
    }


    @PostMapping("/finalizar/{chatSessionId}")
    public ResponseEntity<MensajeDTO<String>> finalizarChat(@PathVariable Long chatSessionId)
            throws ElementoNoEncontradoException {

        chatService.finalizarChat(chatSessionId, RemitenteChat.AGENTE);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Chat finalizado correctamente"));
    }


}
