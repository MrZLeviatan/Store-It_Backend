package co.edu.uniquindio.dto.users.common;

import co.edu.uniquindio.model.users.base.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para editar la información del {@link User} de una persona.
 * <p>
 * Este objeto se utiliza para actualizar los datos del usuario (como correo y contraseña)
 * de una persona en el sistema, identificada por su ID. El DTO también válido que la contraseña
 * actual coincida antes de permitir la actualización de los datos del usuario, y asegura que
 * la nueva contraseña cumpla con las condiciones requeridas.
 *
 * @param id               Identificador único de la persona cuyo usuario se desea editar.
 * @param password         Contraseña actual de la persona, utilizada para validar que la operación de edición es legítima.
 * @param email            Nuevo correo electrónico que se desea asignar al usuario.
 * @param passwordModificado Nueva contraseña que se desea asignar al usuario, debe tener al menos 6 caracteres.
 */
public record EditarUserPersona(
        @NotNull Long id,
        @NotNull String password,
        @Email String email,
        @Size(min = 6) String passwordModificado
) {
}
