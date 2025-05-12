package co.edu.uniquindio.dto.users.cliente;

import jakarta.validation.constraints.NotBlank;
import co.edu.uniquindio.model.users.Cliente;

/**
 * DTO para eliminar lógicamente a un {@link Cliente} del sistema.
 * <p>
 * Este objeto es utilizado para validar la identidad del solicitante
 * mediante su contraseña, y especificar la persona a eliminar por medio de su ID.
 * @param id       Identificador único de la persona que se desea eliminar.
 * @param password Contraseña de la persona, utilizada para validar la operación.
 */
public record EliminarClienteDto(
        @NotBlank Long id,
        @NotBlank String password
) {
}
