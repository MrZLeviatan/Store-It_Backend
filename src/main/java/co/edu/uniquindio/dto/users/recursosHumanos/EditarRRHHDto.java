package co.edu.uniquindio.dto.users.recursosHumanos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para editar la información personal de un integrante del área de {@link co.edu.uniquindio.model.users.RecursosHumanos}.
 * <p>
 * Este DTO permite actualizar los datos básicos como nombre, teléfonos e imagen de perfil,
 * sin modificar los datos laborales ni las credenciales de acceso del usuario.
 *
 * @param id Identificador único del personal de RRHH. Campo obligatorio.
 * @param nombre Nuevo nombre del personal. Máximo 100 caracteres. Campo opcional.
 * @param telefono Nuevo número de teléfono principal. Máximo 15 caracteres. Campo opcional.
 * @param telefonoSecundario Nuevo número de teléfono alternativo. Máximo 15 caracteres. Campo opcional.
 * @param imagenPerfil Nueva imagen de perfil en formato MultipartFile. Puede ser cargada a servicios como Cloudinary.
 *
 * @author MrZ.Leviatan
 */
public record EditarRRHHDto(

        @NotNull Long idRRHH,
        @Length(max = 100) String nombre,
        @Length(max = 15) String telefono,
        @Length(max = 15) String telefonoSecundario,
         MultipartFile imagenPerfil,
        String codigoPais,
        String codigoPaisSecundario
) {
}
