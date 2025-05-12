package co.edu.uniquindio.dto.objects.movimientos;

import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.model.objects.enums.TipoMovimiento;

import java.time.LocalDateTime;

public record MovimientoDto(

        Long id,
        TipoMovimiento tipoMovimiento,
        LocalDateTime fechaMovimiento,
        String detalle



) {
}
