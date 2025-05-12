package co.edu.uniquindio.dto.users.agenteVentas;

import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.common.CrearUserDto;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para el registro de un nuevo {@link co.edu.uniquindio.model.users.AgenteVentas}
 * Contiene toda la información necesaria para crear la entidad incluyendo datos personales,
 * laborales, credenciales de acceso y sede asignada.
 *
 * @param nombre Nombre completo del agente. Campo obligatorio, máximo 100 caracteres.
 * @param telefono Número de teléfono principal incluyendo el código del país. Campo obligatorio, máximo 15 caracteres.
 * @param telefonoSecundario Número de teléfono alternativo. Campo opcional, máximo 15 caracteres.
 * @param imagenPerfil Imagen de perfil del agente en formato MultipartFile. Debe ser subida a un servicio como Cloudinary.
 * @param user Objeto embebido con la información del usuario (correo y contraseña).
 * @param datosLaborales Información contractual y laboral del agente.
 */
public record CrearAgenteVentasDto(

        // Campo obligatorio con longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal con el código de país incluido.
        @NotBlank @Length(max = 15) String telefono,

        @NotBlank String codigoTelefono,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        String codigoTelefonoSecundario,

        // URL de la imagen de perfil almacenada en Cloudinary.
        @NotNull MultipartFile imagenPerfil,

        // Información del usuario (correo y contraseña).
        @NotNull CrearUserDto user,

        @NotNull DatosLaboralesDto datosLaborales,

        @NotNull Long idSede

){
}
