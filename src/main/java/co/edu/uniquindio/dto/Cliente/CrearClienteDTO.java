package co.edu.uniquindio.dto.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

// DTO (Data Transfer Object) para la creación de un nuevo cliente.
// Se utiliza para recibir y validar los datos desde el cliente (por ejemplo, desde una solicitud HTTP).
public record CrearClienteDTO(

        // Campo obligatorio (no puede estar en blanco) con una longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String cedula,

        // Campo obligatorio (no puede estar en blanco) con una longitud máxima de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Campo obligatorio, debe ser un correo electrónico válido y tener una longitud máxima de 50 caracteres.
        @NotBlank @Length(max = 50) @Email String email,

        // Campo obligatorio, con una longitud mínima de 7 y máxima de 20 caracteres.
        @NotBlank @Length(min = 7, max = 20) String password

) {
}
