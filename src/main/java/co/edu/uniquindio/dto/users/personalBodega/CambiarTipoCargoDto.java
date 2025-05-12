package co.edu.uniquindio.dto.users.personalBodega;

import co.edu.uniquindio.model.users.enums.TipoCargo;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para cambiar el {@link TipoCargo} de un {@link co.edu.uniquindio.model.users.PersonalBodega}
 * Este cambio solo puede ser realizado por un Encargado de la misma bodega.
 *
 * @param idPersonalBodega   ID del personal cuyo cargo será actualizado.
 * @param nuevoTipoCargo     Nuevo cargo que se asignará al personal.
 *
 * @author MrZ.Leviatan
 */
public record CambiarTipoCargoDto(
        @NotNull Long idPersonalBodega,
        @NotNull TipoCargo nuevoTipoCargo
) {

}
