package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


/**
 * Data Transfer Object (DTO) utilizado para manejar solicitudes relacionadas con un correo electrónico específico.
 * @param email Dirección de correo electrónico asociada a la solicitud.
 */
public record SolicitudEmailDto(
        @NotNull @Email String email

) {
}
