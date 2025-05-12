package co.edu.uniquindio.dto.objects.contrato;

import co.edu.uniquindio.dto.objects.detalleFactura.DetalleFacturaDto;

import co.edu.uniquindio.model.objects.enums.EstadoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContratoDto(

        Long id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDateTime fechaFirmaCliente,
        EstadoContrato estadoContrato,
        BigDecimal valor

) {
}
