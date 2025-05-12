package co.edu.uniquindio.dto.users.agenteVentas;

import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.users.common.info.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.common.info.UserDto;

import java.util.List;

/**
 * DTO que representa la información completa de un {@link co.edu.uniquindio.model.users.AgenteVentas}
 * Combina los datos personales, laborales, de usuario y la sede asignada,
 * junto con los contratos relacionados con el agente.
 *
 * @param id Identificador único del agente.
 * @param nombre Nombre completo del agente de ventas.
 * @param telefono Número de teléfono principal del agente.
 * @param telefonoSecundario Número de teléfono secundario del agente.
 * @param imagenPerfil URL de la imagen de perfil del agente almacenada (por ejemplo, en Cloudinary).
 * @param user Datos del usuario del agente, incluyendo correo electrónico, estado de cuenta y credenciales.
 * @param datosLaborales Información laboral y contractual del agente (tipo de contrato, salario, fecha de ingreso, etc.).
 */
public record AgenteVentasDto(


        //Base de la clase abstracta Persona.
        Long id,
        String nombre,
        String telefono,
        String telefonoSecundario,
        String imagenPerfil,
        UserDto user,
        //Base para los Datos Agente Ventas
        DatosLaboralesDto datosLaborales


){
}
