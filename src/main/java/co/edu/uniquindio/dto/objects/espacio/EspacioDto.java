package co.edu.uniquindio.dto.objects.espacio;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.movimientos.MovimientoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;

import javax.measure.Quantity;
import javax.measure.quantity.Area;
import java.util.List;

/**
 * DTO que representa la información de un espacio en el sistema.
 * Se utiliza para transferir los datos relevantes de un espacio entre capas o sistemas.
 *
 * @param id El identificador único del espacio.
 * @param areaTotal El área total del espacio, representado como un {@link Quantity} de tipo {@link Area}.
 * @param areaDisponible El área disponible dentro del espacio, representado como un {@link Quantity} de tipo {@link Area}.
 * @param altura La altura total del espacio, representada como un {@link Double}.
 * @param estadoEspacio El estado actual del espacio, que puede ser {@link EstadoEspacio}.
 */
public record EspacioDto(

        Long id,
        Double areaTotal,
        Double areaDisponible,
        Double altura,
        EstadoEspacio estadoEspacio
) {
}
