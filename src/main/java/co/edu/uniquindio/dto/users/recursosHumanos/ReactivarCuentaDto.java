package co.edu.uniquindio.dto.users.recursosHumanos;

import co.edu.uniquindio.model.users.common.DatosLaborales;
import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa la información necesaria para reactivar una {@link DatosLaborales}
 * <p>
 * Se utiliza para identificar el tipo de cuenta (agente, recursos humanos, personal, etc.)
 * y el ID correspondiente de la persona a reactivar.
 *
 * @author MrZ.Leviatan
 */
public record ReactivarCuentaDto(

        @NotNull String tipo,
        @NotNull Long id

) {
}
