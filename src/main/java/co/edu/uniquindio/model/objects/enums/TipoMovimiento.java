package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Movimiento;

/**
 * Enum que define los tipos de {@link Movimiento} que pueden realizarse sobre un producto en el sistema.
 *
 * <ul>
 *   <li>{@link #INGRESO} - El producto entra al sistema y es almacenado en una bodega.</li>
 *   <li>{@link #RETIRO} - El producto es retirado de la bodega, ya sea por el cliente o por traslado.</li>
 * </ul>
 *
 * Se utiliza para registrar y clasificar las acciones sobre los productos durante su ciclo de vida logístico.
 */
public enum TipoMovimiento {


    INGRESO,
    RETIRO
}

