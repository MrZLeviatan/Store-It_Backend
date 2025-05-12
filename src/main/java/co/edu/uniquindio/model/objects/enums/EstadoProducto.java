package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Producto;

/**
 * Enum que representa los estados posibles de un {@link Producto} dentro del sistema de gestión de bodegas.
 *
 * <ul>
 *   <li>{@link #EN_BODEGA} - El producto se encuentra actualmente almacenado en una bodega.</li>
 *   <li>{@link #RETIRADO} - El producto ha sido retirado del sistema o entregado al cliente.</li>
 * </ul>
 *
 * Este estado permite controlar y rastrear la ubicación y condición operativa de los productos.
 *
 */
public enum EstadoProducto {

    EN_BODEGA,
    RETIRADO
}
