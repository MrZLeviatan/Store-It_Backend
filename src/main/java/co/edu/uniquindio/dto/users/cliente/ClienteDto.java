package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.factura.FacturaDto;
import co.edu.uniquindio.dto.users.common.info.UserDto;
import co.edu.uniquindio.model.users.enums.TipoCliente;

import java.util.List;

/**
 * DTO que representa los datos completos de un {@link co.edu.uniquindio.model.users.Cliente} en el sistema.
 * <p>
 * Incluye tanto la información básica heredada de la entidad abstracta Persona,
 * como la información específica relacionada con la ubicación, contratos, facturación y productos asociados al cliente.
 *
 * @param id                 Identificador único del cliente.
 * @param nombre             Nombre completo del cliente.
 * @param telefono           Teléfono principal del cliente.
 * @param telefonoSecundario Teléfono secundario del cliente (opcional).
 * @param imagenPerfil       URL de la imagen de perfil del cliente.
 * @param user               Información del usuario asociado al cliente (correo y autenticación).
 * @param ubicacion          Información de la ubicación geográfica del cliente.
 * @param contratos          Lista de contratos asociados a este cliente.
 * @param facturas           Lista de facturas generadas al cliente.
 * @param productos          Lista de productos almacenados o relacionados con el cliente.
 */
public record ClienteDto(

        //Base de la clase abstracta Persona.
        Long id,
        String nombre,
        String telefono,
        String telefonoSecundario,
        String imagenPerfil,
        UserDto user,

        // Implementación características personales Cliente.
        UbicacionDto ubicacion,
        TipoCliente tipoCliente,
        String nit
) {
}
