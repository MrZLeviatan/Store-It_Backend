package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;

public record LoginGoogleDto(
        @Email String email
) {}
