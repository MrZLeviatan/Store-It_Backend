package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar la contraseña de un usuario tras la validación del código.
 * @param email Correo Electrónico del usuario
 * @param nuevaPassword Nueva contraseña del usuario.
 */
public record ActualizarPasswordDto(
        // Email del usuario
        String email,
        // Nueva contraseña
        @NotBlank @Size(min = 6) String nuevaPassword
) {
}
