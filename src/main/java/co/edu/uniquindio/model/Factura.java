package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.PeriodoFactura;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factura {
    private int capacidadContratada,contratoAsociado,numeroFactura;
    private float cargosAdicionales,total;
    private Date fechaPago;
    private PeriodoFactura periodoFactura;

    private String idCliente,detalle;

}
