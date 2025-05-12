package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Producto;

/**
 * Enum que clasifica los {@link Producto} según su tipo o naturaleza física.
 *
 * <ul>
 *   <li>{@link #FRAGIL} - Producto que requiere cuidados especiales durante su manipulación y almacenamiento.</li>
 *   <li>{@link #NO_FRAGIL} - Producto resistente que no requiere precauciones adicionales.</li>
 * </ul>
 *
 * Esta clasificación permite aplicar políticas diferenciadas de manejo dentro del sistema de bodegas.
 *
 */
public enum TipoProducto {
    FRAGIL,
    NO_FRAGIL
}

