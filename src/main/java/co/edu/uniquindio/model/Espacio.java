package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.EstadoEspacio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Espacio {
    private double alto, largo, ancho;
    private EstadoEspacio estadoEspacio;
    private String codigoUbicacion, ubicacion;

}
