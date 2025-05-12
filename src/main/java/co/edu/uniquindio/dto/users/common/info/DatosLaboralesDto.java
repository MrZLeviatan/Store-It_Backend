package co.edu.uniquindio.dto.users.common.info;

import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO que contiene la información laboral de una persona dentro de la empresa.
 * Se utiliza para representar datos relacionados con el contrato, salario y contacto empresarial.
 *
 * @param fechaContratacion Fecha en la que se inició el contrato laboral. Campo obligatorio.
 * @param fechaFinContrato Fecha de finalización del contrato, si aplica. Campo opcional.
 * @param sueldo Monto del salario acordado para el contrato. Campo obligatorio.
 * @param emailEmpresarial Correo electrónico empresarial asignado al empleado. Campo obligatorio y debe tener formato válido.
 * @param tipoContrato Tipo de contrato laboral (por ejemplo: {@code TÉRMINO_FIJO}, {@code INDEFINIDO}, {@code PRESTACIÓN_SERVICIOS}). Campo obligatorio.
 */
public record DatosLaboralesDto(

        LocalDate fechaContratacion,

        LocalDate fechaFinContrato,

        @NotNull BigDecimal sueldo,

        @NotBlank @Email String emailEmpresarial,

        @NotNull TipoContrato tipoContrato,

        EstadoContratoLaboral estadoContratoLaboral

) {
}
