/*
 *La clase DTO se encarga de permitir transferencia de datos, son el puente
 */

package co.edu.uniquindio.dto.PersonalBodega;

import jakarta.validation.constraints.*;

public record CrearPersonalBodegaDTO(
        @NotBlank(message = "El id es obligatorio")
    String id,
        @Size(min = 3, max = 25, message = "El nombre debe tener minimo 3 caracteres y máximo 25")
    String nombre,
        @Size(min = 3, max = 25, message = "El nombre debe tener minimo 3 caracteres y máximo 25")
    String apellido,
        @Email(message = "Debe tener un formato valido")
    String email,
        @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    String telefono,
        @NotBlank(message = "El cargo es obligatorio")
        String cargo
) {}
