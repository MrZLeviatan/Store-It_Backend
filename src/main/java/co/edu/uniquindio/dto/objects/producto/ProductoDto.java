package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.movimientos.MovimientoDto;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.model.objects.enums.EstadoProducto;
import co.edu.uniquindio.model.objects.enums.TipoProducto;



public record ProductoDto(

        Long id,

        String nombre,

        String descripcion,

        Double areaOcupada,

        Double altura,

        TipoProducto tipoProducto,

        EstadoProducto estadoProducto


) {
}
