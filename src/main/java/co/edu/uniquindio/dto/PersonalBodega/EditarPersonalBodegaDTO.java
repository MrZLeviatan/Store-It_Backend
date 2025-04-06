package co.edu.uniquindio.dto.PersonalBodega;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditarPersonalBodegaDTO(

        @NotBlank String id,
        @NotBlank @Length(max = 100) String nombre,
        @NotBlank @Length(max = 100) String apellido,
        @NotBlank @Length(min = 10, max = 15) String telefono

) {
}
