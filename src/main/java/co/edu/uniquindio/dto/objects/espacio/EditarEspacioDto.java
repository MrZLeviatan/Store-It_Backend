package co.edu.uniquindio.dto.objects.espacio;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO (Data Transfer Object) utilizado para editar un espacio existente en el sistema.
 * Contiene los datos necesarios para modificar las características de un espacio en una bodega.
 *
 * @param id El ID único del espacio que se desea editar. No puede ser nulo.
 * @param areaTotal El área total del espacio en metros cuadrados. Debe ser un valor positivo.
 * @param altura La altura total del espacio en metros. Debe ser un valor positivo.
 *
 * @author MrZ.Leviatan
 */
public record EditarEspacioDto(

        @NotNull Long id,
        @Positive Double areaTotal,
        @Positive Double altura

) {
}
