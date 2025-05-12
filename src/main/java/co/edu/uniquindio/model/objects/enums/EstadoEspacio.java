package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.Bodega;

/**
 * Enum que representa los posibles estados de un {@link  Espacio} dentro de una {@link Bodega}.
 * <p>
 * Este enum se utiliza para definir el estado actual de un espacio específico en la bodega.
 * Los estados indican si el espacio está disponible para ser contratado, si está contratado, pero aún tiene espacio libre,
 * o si está completamente ocupado.
 * </p>
 */
public enum EstadoEspacio {

    /**
     * Estado que indica que el espacio está disponible para ser contratado.
     * <p>
     * Este estado significa que el espacio está libre y no está siendo utilizado por ningún cliente.
     * </p>
     */
    LIBRE,               // Disponible para ser contratado

    /**
     * Estado que indica que el espacio ha sido contratado, pero aún tiene espacio disponible.
     * <p>
     * Este estado implica que el espacio ha sido reservado por un cliente, pero no se encuentra completamente ocupado.
     * Aún es posible que el cliente utilice el espacio adicional.
     * </p>
     */
    CONTRATADO_DISPONIBLE, // Contratado, pero aun con espacio libre

    /**
     * Estado que indica que el espacio está completamente ocupado tras haber sido contratado.
     * <p>
     * Este estado significa que el cliente ha utilizado todo el espacio contratado, y no hay espacio libre disponible.
     * </p>
     */
    CONTRATADO_LLENO,// Contratado y totalmente ocupado

    /**
     * Estado que índica que el espacio inactivo (por la bodega o x razón)
     */
    INACTIVO
}
