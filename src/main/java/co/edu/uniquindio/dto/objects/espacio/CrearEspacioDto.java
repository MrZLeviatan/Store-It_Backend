package co.edu.uniquindio.dto.objects.espacio;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO (Data Transfer Object) utilizado para crear un nuevo espacio en el sistema.
 * Contiene los datos necesarios para la creación de un espacio dentro de una bodega.
 *
 * @param areaTotal El área total del espacio en metros cuadrados. Debe ser un valor positivo.
 * @param altura La altura total del espacio en metros. Debe ser un valor positivo.
 * @param bodega La bodega en la que se encuentra el espacio, representada como un {@link BodegaDto}.
 *
 * @author MrZ.Leviatan
 */
public record CrearEspacioDto(

        @NotNull @Positive Double areaTotal,
        @NotNull @Positive Double altura,

        Long idBodega
) {
}
