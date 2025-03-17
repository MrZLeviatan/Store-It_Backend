package co.edu.uniquindio.model;

import co.edu.uniquindio.model.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    private String id,gmail,passaword,cedula,nombre;
    private Rol rol;

}
