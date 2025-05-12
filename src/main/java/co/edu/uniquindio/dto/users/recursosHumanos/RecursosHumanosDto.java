package co.edu.uniquindio.dto.users.recursosHumanos;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import co.edu.uniquindio.dto.users.common.info.UserDto;

/**
 * DTO para representar los datos de un integrante de {@link co.edu.uniquindio.model.users.RecursosHumanos}
 * <p>
 * Este DTO agrupa la información personal, laboral y de cuenta de usuario necesaria
 * para mostrar o transferir datos del personal de RRHH dentro del sistema.
 *
 * @param id Identificador único del personal de RRHH.
 * @param nombre Nombre completo del personal de RRHH.
 * @param telefono Número de teléfono principal incluyendo el código del país.
 * @param telefonoSecundario Número de teléfono alternativo (opcional).
 * @param imagenPerfil URL o ruta de la imagen de perfil almacenada (por ejemplo, en Cloudinary).
 * @param user Información de cuenta de usuario (correo electrónico y estado).
 * @param datosLaborales Detalles contractuales y laborales como tipo de contrato, fecha de contratación, estado y sueldo.
 *
 */
public record RecursosHumanosDto(

        //Base de la clase abstracta Persona.
        Long id,
        String nombre,
        String telefono,
        String telefonoSecundario,
        String imagenPerfil,
        UserDto user,
        //Base para los Datos
        DatosLaboralesDto datosLaborales

) {
}
