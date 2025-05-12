package co.edu.uniquindio.dto.common.email;

public record MensajeContactoDTO(
        String nombre,
        String email,
        String asunto,
        String mensaje
) {
}
