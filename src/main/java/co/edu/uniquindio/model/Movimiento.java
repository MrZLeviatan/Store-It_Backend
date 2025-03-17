package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movimiento {
    private Date fecha;
    private String ubicacion;
    private int cantidad;

    TipoMovimiento tipoMovimiento;
}
