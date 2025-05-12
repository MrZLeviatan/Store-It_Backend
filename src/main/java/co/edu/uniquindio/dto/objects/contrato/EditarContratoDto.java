package co.edu.uniquindio.dto.objects.contrato;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EditarContratoDto(

        @NotNull Long id,
        LocalDate fechaFin,
        BigDecimal valor,
        @Length(max = 500) String descripcion

) {
}
