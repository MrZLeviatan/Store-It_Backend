package co.edu.uniquindio.dto.Cliente;

import co.edu.uniquindio.dto.Espacio.EspacioDto;
import co.edu.uniquindio.dto.Producto.ProductoDto;
import co.edu.uniquindio.model.enums.Rol;

import java.util.List;

public record ClienteDto(

        String cedula,
        String nombre,
        String email,
        String password,
        Rol rol
       // List<ProductoDto> listProducto,
       // List<EspacioDto> listEspacios
) {
}
