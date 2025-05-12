package co.edu.uniquindio.dto.common.email;

/**
 * Record que representa un correo electrónico a ser enviado.
 * Contiene la información esencial como el destinatario, el asunto y el cuerpo del mensaje.
 *
 * @param destinatario Dirección de correo electrónico del receptor del mensaje.
 * @param asunto       Asunto del correo electrónico.
 * @param cuerpo       Contenido del mensaje en texto plano.
 *
 * @author MrZ.Leviatan
 */
public record EmailDto(
        String destinatario,
        String asunto,
        String cuerpo
) {
}
