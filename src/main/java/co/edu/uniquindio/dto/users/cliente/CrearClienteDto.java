package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.users.common.CrearUserDto;
import co.edu.uniquindio.model.users.enums.TipoCliente;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import co.edu.uniquindio.model.users.Cliente;

/**
 * DTO para registrar un nuevo {@link Cliente} en el sistema.
 * <p>
 * Contiene datos personales, de contacto, imagen de perfil, credenciales de usuario
 * y ubicación geográfica. Utilizado en solicitudes de creación desde el frontend.
 * <p>
 * Validaciones incluidas para garantizar integridad de los datos.
 *
 * @param nombre Nombre del cliente a registrar.
 * @param telefono Teléfono principal con el código de país incluido.
 * @param telefonoSecundario Teléfono secundario opcional.
 * @param imagenPerfil Archivo png/jpg para almacenar en {@code Cloudinary}.
 * @param user Objeto {@link CrearUserDto}, contiene los parámetros de email, password.
 * @param ubicacion Objeto {@link UbicacionDto}. Contiene los parámetros para una localización.
 */
public record CrearClienteDto(

        // Campo obligatorio con longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal con el código de país incluido.
        @NotBlank @Length(max = 15) String telefono,

        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        String codigoPaisSecundario,

        // URL de la imagen de perfil almacenada en Cloudinary.
        MultipartFile imagenPerfil,

        // Información del usuario (correo y contraseña).
        @NotNull CrearUserDto user,

        // Información de la ubicación del cliente.
        @NotNull UbicacionDto ubicacion,

        // Tipo de cliente.
        @NotNull TipoCliente tipoCliente

) {
}