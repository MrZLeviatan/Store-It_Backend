package co.edu.uniquindio.dto.users.common;

import co.edu.uniquindio.model.users.base.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * DTO para la creación de un nuevo {@link User}
 * <p>
 * Este objeto se utiliza para recibir los datos necesarios para registrar un nuevo usuario en el sistema.
 * Los parámetros incluyen el correo electrónico y la contraseña del usuario, ambos elementos son requeridos
 * para completar la creación de la cuenta.
 *
 * @param email    Correo electrónico del usuario, debe ser un valor válido y no estar vacío.
 * @param password Contraseña del usuario, debe tener al menos 6 caracteres.
 */
public record CrearUserDto(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password
) {
}
