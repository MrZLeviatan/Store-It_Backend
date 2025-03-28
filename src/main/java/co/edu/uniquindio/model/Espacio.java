package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.EstadoEspacio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Espacio")
public class Espacio {

    @Id
    @Column(name = "id", nullable = false, unique = true) // Columna para el ID. No puede ser nula y debe ser única.
    private String id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Ajusta según la clave foránea real
    private Cliente cliente;


    private double alto, largo, ancho;
    private EstadoEspacio estadoEspacio;
    private String codigoUbicacion, ubicacion;

}
