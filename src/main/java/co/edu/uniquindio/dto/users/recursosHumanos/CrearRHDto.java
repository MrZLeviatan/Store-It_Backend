package co.edu.uniquindio.dto.users.recursosHumanos;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.common.CrearUserDto;
import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para el registro de un nuevo integrante del equipo de {@link co.edu.uniquindio.model.users.RecursosHumanos}
 * Contiene toda la información necesaria para crear la entidad, incluyendo datos personales,
 * laborales, credenciales de acceso y la sede asignada.
 *
 * @param nombre Nombre completo del miembro de RRHH. Campo obligatorio, máximo 100 caracteres.
 * @param telefono Número de teléfono principal incluyendo el código del país. Campo obligatorio, máximo 15 caracteres.
 * @param telefonoSecundario Número de teléfono alternativo. Campo opcional, máximo 15 caracteres.
 * @param imagenPerfil Imagen de perfil del personal en formato MultipartFile. Debe ser subida a un servicio como Cloudinary.
 * @param user Objeto embebido con la información del usuario (correo y contraseña).
 * @param datosLaborales Información contractual y laboral del personal de RRHH.
 * @param sede Sede a la que será asignado el personal de RRHH.
 *
 * @author MrZ.Leviatan
 */
public record CrearRHDto(

        // Campo obligatorio con longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal con el código de país incluido.
        @NotBlank @Length(max = 15) String telefono,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        // URL de la imagen de perfil almacenada en Cloudinary.
        @NotNull MultipartFile imagenPerfil,

        // Información del usuario (correo y contraseña).
        @NotNull CrearUserDto user,

        @NotNull DatosLaboralesDto datosLaborales,

        @NotNull SedeDto sede

) {
}
