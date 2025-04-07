package co.edu.uniquindio.dto.Login;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(

        @NotBlank String email,
        @NotBlank String password
) {
}
