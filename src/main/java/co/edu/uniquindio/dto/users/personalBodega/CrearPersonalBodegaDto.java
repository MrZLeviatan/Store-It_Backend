package co.edu.uniquindio.dto.users.personalBodega;

import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.common.CrearUserDto;
import co.edu.uniquindio.model.users.enums.TipoCargo;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO utilizado para la creación de un nuevo miembro del personal de bodega dentro del sistema Store-It.
 * <p>
 * Este objeto encapsula todos los datos necesarios para registrar.
 * Los datos recolectados incluyen información personal, contacto, imagen de perfil, datos laborales,
 * credenciales de acceso y asociación con una bodega existente.
 * @param nombre              Nombre completo del miembro del personal. Es obligatorio y tiene una longitud máxima de 100 caracteres.
 * @param telefono            Número telefónico principal, incluyendo el código de país. Validado mediante la anotación personalizada {@code @TelefonoValido}.
 * @param telefonoSecundario  Número telefónico secundario opcional. También debe incluir el código de país y no exceder 15 caracteres.
 * @param imagenPerfil        Imagen de perfil del personal. Debe ser una imagen válida (JPG o PNG) que será almacenada en la nube (Cloudinary).
 * @param user                Objeto que contiene las credenciales del usuario, como correo electrónico y contraseña.
 * @param datosLaborales      Información laboral del personal, como salario, horario, tipo de contrato, etc.
 * @param tipoCargo           Enumeración que indica el tipo de cargo desempeñado (Ej. RECEPCIONISTA, SUPERVISOR, TRANSPORTISTA, etc.).
 */
public record CrearPersonalBodegaDto(

        // Campo obligatorio con longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal con el código de país incluido.
        @NotBlank @Length(max = 15) String telefono,

        @NotNull String codigoTelefono,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        String codigoTelefonoSecundario,

        // URL de la imagen de perfil almacenada en Cloudinary.
        @NotNull MultipartFile imagenPerfil,

        // Información del usuario (correo y contraseña).
        @NotNull CrearUserDto user,

        // Datos laborales del personal
        @NotNull DatosLaboralesDto datosLaborales,

        // Bodega en la que trabaja el personal de bodega.
        @NotNull Long idBodega,

        // Tipo de cargo del personal de bodega.
        @NotNull TipoCargo tipoCargo

) {
}
