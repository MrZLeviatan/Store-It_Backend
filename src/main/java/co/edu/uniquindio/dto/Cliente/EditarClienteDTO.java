package co.edu.uniquindio.dto.Cliente;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditarClienteDTO(

        @NotBlank String id,
        @NotBlank @Length(max = 100) String nombre
) {

}
