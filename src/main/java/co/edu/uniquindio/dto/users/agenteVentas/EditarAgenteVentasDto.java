package co.edu.uniquindio.dto.users.agenteVentas;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para la edición de los datos personales de un {@link co.edu.uniquindio.model.users.AgenteVentas}.
 * Permite modificar el nombre, los teléfonos y la imagen de perfil del agente.
 *
 * @param id Identificador único del agente que se desea editar. Campo obligatorio.
 * @param nombre Nuevo nombre del agente. Campo opcional, máximo 100 caracteres.
 * @param telefono Nuevo número de teléfono principal, incluyendo el código de país. Campo opcional, máximo 15 caracteres.
 * @param telefonoSecundario Nuevo número de teléfono secundario. Campo opcional, máximo 15 caracteres.
 * @param imagenPerfil Nueva imagen de perfil del agente, enviada como archivo MultipartFile. Campo opcional.
 */
public record EditarAgenteVentasDto(

        @NotNull Long id,
        @Length(max = 100) String nombre,
        @Length(max = 15) String telefono,
        String codigoPais,
        @Length(max = 15) String telefonoSecundario,
        String codigoPaisSecundario,
        MultipartFile imagenPerfil

) {
}
