package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Bodega;

/**
 * Representa los posibles estados en los que puede encontrarse una {@link Bodega} dentro del sistema.
 *
 * <ul>
 *   <li>{@link #INACTIVA} - La bodega está deshabilitada o no disponible para su uso.</li>
 *   <li>{@link #ACTIVA} - La bodega está habilitada y lista para ser utilizada o contratada.</li>
 *   <li>{@link #OCUPADA} - La bodega ya se encuentra en uso actualmente.</li>
 * </ul>
 *
 * Este enum se utiliza para controlar la disponibilidad operativa de cada espacio de almacenamiento.
 *
 */
public enum EstadoBodega {
    INACTIVA,
    ACTIVA,
    OCUPADA
}
