package co.edu.uniquindio.model;

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
public class Producto {

    @Id
    @Column(name = "id", nullable = false, unique = true) // Columna para el ID. No puede ser nula y debe ser única.
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Asegúrate de que este nombre coincide con el de la base de datos
    private Cliente cliente;


    private double alto,ancho,largo;

}
