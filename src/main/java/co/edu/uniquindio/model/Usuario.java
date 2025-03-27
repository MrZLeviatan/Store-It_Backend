package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass // Indica que esta clase es una superclase de entidades, pero no se mapea como tabla.
public class Usuario {

    @Id
    @Column(name = "id", nullable = false, unique = true) // Columna para el ID. No puede ser nula y debe ser única.
    private String id;

    @Column(name = "nombre", nullable = false) // El nombre no puede ser nulo.
    private String name;

    @Column(name = "email", nullable = false, unique = true) // El email no puede ser nulo y debe ser único.
    private String gmail;

    @Column(name = "password",nullable = false) // La contraseña del usuario. No puede ser nula.
    private String password;

    @Enumerated(EnumType.STRING) // Convierte el enum Rol en un String en la base de datos.
    private Rol rol;

}
