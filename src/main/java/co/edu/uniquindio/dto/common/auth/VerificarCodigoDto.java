package co.edu.uniquindio.dto.common.auth;

import co.edu.uniquindio.dto.common.codigoSecurity.CodigoRestablecerDto;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para confirmar el código enviado al correo para restablecer la contraseña.
 * @param email Correo Electrónico al que se le envía el código
 * @param codigo Código de confirmación,
 */
public record VerificarCodigoDto(
        @NotBlank String email,     // Email al que se envió el código
        String codigo
) {
}
