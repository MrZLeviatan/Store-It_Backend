package co.edu.uniquindio.model;

import java.util.Date;

import co.edu.uniquindio.model.enums.EstadoContrato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contrato {
    private  int espacioAsociados,numeroContratos;
    private EstadoContrato estadoContrato;
    private String idCliente;
    private Date fechaIncio,fechaFinal;
}
