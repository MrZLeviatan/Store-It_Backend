package co.edu.uniquindio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PersonalBodegaDTO (
    @NotBlank(message = "El id es obligatorio")
    String id,
    @Size(min = 3, max = 25, message = "El nombre debe tener minimo dos caracteres y máximo 25")
    String nombre,
    @Size(min = 3, max = 25, message = "El nombre debe tener minimo dos caracteres y máximo 25")
    String apellido,
    @Email(message = "Debe tener un formato valido")
    String email,

    String telefono,
    String cargo,
    LocalDate fechaIngreso
) {}
