package co.edu.uniquindio.model.users.enums;

import co.edu.uniquindio.model.users.common.DatosLaborales;

/**
 * Enumeración que representa el estado actual de un contrato laboral.
 * Se utiliza en clases como {@link DatosLaborales}
 * para determinar si un contrato está activo, suspendido o finalizado.
 * <p>
 * Estados del contrato laboral:
 * <ul>
 * <li> ACTIVO: El contrato está vigente y en curso.</li>
 * <li> SUSPENDIDO: El contrato fue pausado temporalmente, pero no está finalizado.</li>
 * <li> FINALIZADO: El contrato ha finalizado definitivamente.</li>
 */
public enum EstadoContratoLaboral {

    ACTIVO,
    SUSPENDIDO,
    FINALIZADO
}
