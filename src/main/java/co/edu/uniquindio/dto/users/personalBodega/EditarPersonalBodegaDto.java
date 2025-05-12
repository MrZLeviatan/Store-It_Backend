package co.edu.uniquindio.dto.users.personalBodega;

import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para la edición de información básica del {@link co.edu.uniquindio.model.users.PersonalBodega}
 *
 * Permite actualizar datos personales como nombre, teléfonos y foto de perfil.
 * La imagen (si se proporciona) será reemplazada en Cloudinary. Los campos son opcionales,
 * excepto el ID, que es necesario para identificar al personal a editar.
 *
 * @param id                 ID único del personal a modificar (obligatorio).
 * @param nombre             Nuevo nombre del personal (máx. 100 caracteres).
 * @param telefono           Teléfono principal actualizado con código de país.
 * @param telefonoSecundario Teléfono secundario con código de país (opcional).
 * @param imagenPerfil       Imagen de perfil nueva (opcional, reemplaza la actual).
 */
@TelefonoValido
public record EditarPersonalBodegaDto(

        @NotNull Long id,

        @Length(max = 100) String nombre,

        @Length(max = 15) String telefono,

        String codigoPais,

        @Length(max = 15) String telefonoSecundario,

        String codigoPaisSecundario,

        MultipartFile imagenPerfil

) {
}