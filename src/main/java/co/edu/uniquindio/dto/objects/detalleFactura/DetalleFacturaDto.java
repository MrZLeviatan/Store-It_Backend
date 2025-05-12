package co.edu.uniquindio.dto.objects.detalleFactura;

import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.factura.FacturaDto;

import java.math.BigDecimal;

public record DetalleFacturaDto(

        Long id,
        BigDecimal valor

) {
}
