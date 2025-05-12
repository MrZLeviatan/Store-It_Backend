package co.edu.uniquindio.dto.users.common;

import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.model.users.common.DatosLaborales;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizado para editar los {@link DatosLaborales} de una persona dentro del sistema.
 * Permite actualizar información contractual como el sueldo, el tipo de contrato y su estado.
 *
 * @param id Identificador único del agente o empleado cuyos datos laborales serán editados. Campo obligatorio.
 * @param fechaFinContratacion Fecha en la que finaliza el contrato, si aplica. Campo opcional.
 * @param sueldo Nuevo valor del sueldo. Debe ser positivo o cero. Campo opcional.
 * @param tipoContrato Tipo de contrato actualizado (por ejemplo: {@code TÉRMINO_FIJO}, {@code INDEFINIDO}, {@code PRESTACIÓN_SERVICIOS}). Campo opcional.
 * @param estadoContratoLaboral Nuevo estado del contrato laboral (por ejemplo: {@code ACTIVO}, {@code FINALIZADO}, {@code SUSPENDIDO}). Campo opcional.
 */
public record EditarDatosLaboralesDto(

        @NotNull Long id,
        LocalDate fechaFinContratacion,
        @PositiveOrZero BigDecimal sueldo,
        TipoContrato tipoContrato,
        EstadoContratoLaboral estadoContratoLaboral
) {
}
