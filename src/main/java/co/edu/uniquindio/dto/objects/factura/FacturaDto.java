package co.edu.uniquindio.dto.objects.factura;

import co.edu.uniquindio.dto.objects.detalleFactura.DetalleFacturaDto;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.model.objects.enums.EstadoFactura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FacturaDto(

        Long id,
        LocalDate fechaEmision,

        LocalDate fechaPago,

        BigDecimal iva,

        BigDecimal valorTotal,

        EstadoFactura estadoFactura


        ) {
}
