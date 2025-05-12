package co.edu.uniquindio.model.objects.enums;

import co.edu.uniquindio.model.objects.Contrato;

/**
 * Enum que representa los diferentes estados posibles de un {@link Contrato}.
 * <p>
 * El ciclo de vida de un contrato puede estar en uno de los siguientes estados:
 * activo, finalizado, cancelado o inactivo. Cada uno de estos estados
 * tiene un significado y comportamiento específicos en el sistema.
 * <ul> Estados del Contrato: </ul>
 * <li> Activo: El contrato está activo y vigente</li>
 * <li> Finalizado: El contrato ha sido finalizado por vencimiento o cumplimiento del periodo acordado</li>
 * <li> Cancelado: El contrato ha sido cancelado de forma anticipada por alguna de las partes</li>
 * <li> Pendiente: El contrato está pendiente de aprobación o verificación por alguna de las partes</li>
 * </p>
 */
public enum EstadoContrato {

    /**
     *  El contrato está activo y vigente.
     *  Se encuentra en curso y todos los servicios están habilitados.
     */
    ACTIVO,

    /**
     * El contrato ha sido finalizado por vencimiento o cumplimiento del periodo acordado.
     * Ya no genera cargos ni actividades.
     */
    FINALIZADO,

    /**
     * El contrato ha sido cancelado de forma anticipada por alguna de las partes.
     * Puede estar sujeto a penalizaciones o condiciones.
     */
    CANCELADO,

    PENDIENTE_VERIFICACION,

    VERIFICADO_POR_CLIENTE

}
