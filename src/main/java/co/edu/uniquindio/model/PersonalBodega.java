package co.edu.uniquindio.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDate;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data //Generar get,set, equals, lo básico
@AllArgsConstructor //Constructor sin arg
@NoArgsConstructor //Constructor con arg
@Document(collation = "personal bodega") //Indica que se almacena en mongoDB

public class PersonalBodega {
    @Id
    private String id;

    @NotBlank(message = "Nombre obligatorio")
    @Size(min = 3, max = 25, message = "El nombre debe tener entre 3 y 25 caracteres")
    private String nombre;

    @Size(min = 3, max = 25, message = "El apellido debe tener entre 3 y 25 caracteres")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Debe ser un email con formato valido")
    @NotBlank(message = "Email obligatorio")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    @NotBlank(message = "Cargo obligatorio")
    private String cargo;

    @PastOrPresent (message = "La fecha no puede ser mayor a la de hoy")
    private LocalDate fecha;





}
