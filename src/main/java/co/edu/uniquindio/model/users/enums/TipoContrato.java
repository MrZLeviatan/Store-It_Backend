package co.edu.uniquindio.model.users.enums;

import co.edu.uniquindio.model.users.common.DatosLaborales;

/**
 * Enumeración que representa los distintos tipos de contrato laboral
 * que puede tener un empleado dentro de la organización.
 * <p>
 * Se utiliza para definir formalmente la relación contractual en entidades
 * como {@link DatosLaborales}.
 * <p>
 * Tipos de Contratos:
 * <ul>
 * <li> {@code DEFINIDO}: Contrato con una duración fija previamente acordada. </li>
 * <li> {@code INDETERMINADO}: Contrato por un período limitado, usualmente para reemplazos o temporadas.</li>
 * <li> {@code PRESTACIÓN_SERVICIOS}: Contrato por prestación de servicios, generalmente de tipo independiente. </li>
 */
public enum TipoContrato {

    DEFINIDO,
    INDEFINIDO,
    PRESTACION_SERVICIOS

}
