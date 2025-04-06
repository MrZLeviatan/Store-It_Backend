package co.edu.uniquindio.dto.Cliente;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


public record CrearClienteDTO(
        @NotBlank @Length(max = 100) String cedula,
        @NotBlank @Length(max = 100) String nombre,
        @NotBlank @Length(max = 50) @Email String email,
        @NotBlank @Length(min = 7, max = 20) String password
) {
}

