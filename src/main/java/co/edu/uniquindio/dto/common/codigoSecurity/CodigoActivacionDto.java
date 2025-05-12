package co.edu.uniquindio.dto.common.codigoSecurity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CodigoActivacionDto(

        @NotBlank String codigoActivacion,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fechaCreacionCodigoActivacion
) {
}
