/*
 *La clase modelo se encarga de definir la entidad que se guarda
 * en bd
 */

package co.edu.uniquindio.model.mod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity //Mapear en tabla en bd, entidad JPA
@Data //Generar get,set, equals, lo básico
@AllArgsConstructor //Constructor sin arg
@NoArgsConstructor //Constructor con arg
@Table(name = "personal_bodega") //Indica en nombre en base de datoss

public class PersonalBodega {
    @Id //Validaciones
    @NotBlank(message = "El ID es de caracter obligatorio")
    @Column(name = "id", nullable = false, unique = true) // Columna para el ID. No puede ser nula y debe ser única.
    private String id;

    @NotBlank(message = "Nombre obligatorio")
    @Column(name = "nombre", nullable = false) // El nombre no puede ser nulo.
    @Size(min = 3, max = 25, message = "El nombre debe tener entre 3 y 25 caracteres")
    private String nombre;

    @Size(min = 3, max = 25, message = "El apellido debe tener entre 3 y 25 caracteres")
    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "apellido", nullable = false) // El apellido no puede ser nulo.
    private String apellido;

    @Email(message = "Debe ser un email con formato valido")
    @NotBlank(message = "Email obligatorio")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotBlank(message = "Cargo obligatorio")
    @Column(name = "cargo", nullable = false)
    private String cargo;

    @PastOrPresent (message = "La fecha no puede ser mayor a la de hoy")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

}
