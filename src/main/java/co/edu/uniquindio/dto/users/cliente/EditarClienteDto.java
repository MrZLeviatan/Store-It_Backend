package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para editar la información de un {@link Cliente} existente.
 * <p>
 * Este objeto se utiliza para actualizar los datos de un cliente en el sistema,
 * permitiendo modificar los campos de nombre, teléfono, imagen de perfil y ubicación.
 * El ID del cliente no debe ser modificado, ya que es único para cada cliente.
 * Además, los campos de teléfono y ubicación son opcionales en cuanto a su actualización.
 *
 * @param id              Identificador único del cliente. Este valor no puede ser modificado.
 * @param nombre          Nombre del cliente, el cual puede ser modificado.
 * @param telefono        Teléfono principal del cliente. Este valor es obligatorio y puede ser modificado.
 * @param telefonoSecundario Teléfono secundario del cliente, opcional y puede ser modificado o agregado.
 * @param imagenPerfil    Imagen de perfil del cliente, obligatoria y puede ser modificada.
 * @param ubicacion       Información de la ubicación del cliente, la cual puede ser modificada.
 */
@TelefonoValido
public record EditarClienteDto(

        // Id que no se debe modificar
        @NotBlank Long id,

        // Nombre que puede ser modificado
        @Length(max = 100) String nombre,

        // Teléfonos, puede ser modificado
        @Length(max = 15) String telefono,

        String codigoPais,

        // Teléfono secundario opcional, se puede agregar o modificar
        @Length(max = 15) String telefonoSecundario,

        String codigoPaisSecundario,

        // Imagen de perfil que puede ser modificada
        MultipartFile imagenPerfil,

        // Ubicación, la cual también se puede modificar
        UbicacionDto ubicacion

) {
}
