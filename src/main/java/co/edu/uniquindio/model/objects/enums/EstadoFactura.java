package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Factura;

/**
 * Enum que representa los diferentes estados en los que puede encontrarse una {@link Factura} dentro del sistema.
 *
 * <ul>
 *   <li>{@link #PENDIENTE} - La factura ha sido generada pero aún no ha sido pagada.</li>
 *   <li>{@link #PAGADA} - La factura ha sido pagada completamente por el cliente.</li>
 *   <li>{@link #CANCELADA} - La factura ha sido anulada y no requiere pago.</li>
 * </ul>
 * Este enum se utiliza para el control del ciclo de vida de las facturas emitidas por el sistema.
 */
public enum EstadoFactura {

    PENDIENTE,
    PAGADA,
    CANCELADA;
}

