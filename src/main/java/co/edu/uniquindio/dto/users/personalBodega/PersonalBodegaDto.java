package co.edu.uniquindio.dto.users.personalBodega;

import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.movimientos.MovimientoDto;
import co.edu.uniquindio.dto.users.common.info.UserDto;
import co.edu.uniquindio.model.users.enums.TipoCargo;

import java.util.List;

/**
 * DTO que representa la información detallada de un miembro del {@link co.edu.uniquindio.model.users.PersonalBodega}
 *
 * Contiene los datos personales, credenciales de usuario, datos laborales y relaciones
 * con la bodega y los movimientos de productos.
 *
 * @param id                  Identificador único del personal.
 * @param nombre              Nombre completo del personal.
 * @param telefono            Teléfono principal con código internacional.
 * @param telefonoSecundario  Teléfono secundario (opcional).
 * @param imagenPerfil        URL de la imagen de perfil almacenada en Cloudinary.
 * @param user                Información del usuario (correo, autenticación, etc.).
 * @param datosLaborales      Información contractual y de asignación laboral.
 * @param movimientosProducto Lista de movimientos de productos realizados o gestionados.
 * @param tipoCargo           Rol o cargo que ocupa dentro de la bodega.
 */
public record PersonalBodegaDto(

        /*
         * Base de la clase abstracta Persona.
         */
        Long id,
        String nombre,
        String telefono,
        String telefonoSecundario,
        String imagenPerfil,
        UserDto user,

        /*
         * Base para los Datos Personal Bodega
         */
        DatosLaboralesDto datosLaborales,
        TipoCargo tipoCargo
) {
}
