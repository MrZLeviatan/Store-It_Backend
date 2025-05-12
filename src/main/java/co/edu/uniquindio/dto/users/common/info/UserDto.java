package co.edu.uniquindio.dto.users.common.info;

import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import jakarta.validation.constraints.Email;

/**
 * DTO para representar los datos del {@link  co.edu.uniquindio.model.users.base.User} necesarios en procesos de autenticación
 * @param email Correo electrónico del usuario
 * @param password Contraseña del usuario
 * @param estadoCuenta Estado actual de la cuenta (por ejemplo: {@code ACTIVO}, {@code PENDIENTE}, {@code BLOQUEADO)}
 */
public record UserDto(

        @Email String email,
        String password,
        EstadoCuenta estadoCuenta,
        CodigoRestablecimientoDto codigoRestablecimiento

) {
}
