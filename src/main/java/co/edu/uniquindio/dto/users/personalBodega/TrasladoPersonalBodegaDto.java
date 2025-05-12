package co.edu.uniquindio.dto.users.personalBodega;

import jakarta.validation.constraints.NotNull;

/**
 * DTO que contiene la información necesaria para realizar el traslado
 * de un miembro del {@link co.edu.uniquindio.model.users.PersonalBodega} a otra {@link co.edu.uniquindio.model.objects.Bodega} o ubicación.
 * <p>
 * @param idPersonal Identificador único del {@link co.edu.uniquindio.model.users.PersonalBodega}
 * @param idBodega Identificador único de la {@link co.edu.uniquindio.model.objects.Bodega}
 *
 * @author MrZ.Leviatan
 */
public record TrasladoPersonalBodegaDto(

        @NotNull Long idPersonal,
        @NotNull Long idBodega

) {
}
