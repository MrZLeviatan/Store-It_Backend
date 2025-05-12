package co.edu.uniquindio.dto.users.agenteVentas;

import co.edu.uniquindio.model.objects.Sede;
import jakarta.validation.constraints.NotNull;

/**
 * DTO utilizado para realizar el traslado de un {@link co.edu.uniquindio.model.users.AgenteVentas} a una nueva {@link Sede}.
 * Contiene los identificadores necesarios para efectuar el cambio de ubicación del agente.
 *
 * @param idAgente Identificador único del {@link co.edu.uniquindio.model.users.AgenteVentas} que será trasladado. Campo obligatorio.
 * @param idSedeNueva Identificador de la nueva {@link Sede} a la que será asignado el agente. Campo obligatorio.
 */
public record TrasladoSedeAgenteDto(

        @NotNull Long idAgente,
        @NotNull Long idSedeNueva

) {
}
