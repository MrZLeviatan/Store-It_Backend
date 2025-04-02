package co.edu.uniquindio.dto;

import java.time.LocalDate;

public record PersonalBodegaDTO (
    String id,
    String nombre,
    String apellido,
    String email,
    String telefono,
    String cargo,
    LocalDate fechaIngreso
) {}
