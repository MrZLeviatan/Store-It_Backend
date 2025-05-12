package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) utilizado para manejar la información de inicio de sesión de un usuario.
 * @param email Dirección de correo electrónico del usuario.
 * @param password Contraseña asociada al usuario.
 */
public record LoginDto(

        @NotBlank @Email String email, // Correo electrónico del usuario
        @NotBlank String password // Contraseña del usuario
) {
}
