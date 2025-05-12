package co.edu.uniquindio.model.users.common;

import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import co.edu.uniquindio.model.users.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa los datos laborales de una persona dentro del sistema.
 * Esta clase es una clase embebida {@link Embeddable}, utilizada en entidades
 * como {@link AgenteVentas} o {@link PersonalBodega}
 * para almacenar información relacionada con el contrato de trabajo.
 * <p>
 * Incluye detalles como {@code fechas de contratación}, {@link TipoContrato} y {@link  EstadoContratoLaboral},
 * {@code salario} y {@code correo empresarial}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DatosLaborales {

    // Fecha en la que inició la relación laboral con la empresa.
    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    /**
     * Fecha de finalización del contrato laboral.
     * Este campo es opcional y solo aplica en casos donde el contrato tiene un fin definido.
     */
    @Column(name = "fecha_fin_contrato", nullable = true)
    @Comment("Fecha de finalización del contrato (si aplica).")
    private LocalDate fechaFinContrato;

    /**
     * Sueldo mensual del empleado.
     * Este valor se almacena como un número decimal de alta precisión.
     */
    @Column(name = "sueldo", nullable = false)
    private BigDecimal sueldo;

    /**
     * Dirección de correo empresarial única utilizada para autenticación y contacto interno.
     * Debe tener formato válido de correo electrónico.
     */
    @Column(name = "email_empresarial", nullable = false, unique = true)
    @Comment("Dirección de correo electrónico empresarial única para autenticación.")
    @Email
    private String emailEmpresarial;

    /**
     * Tipo de contrato laboral del empleado.
     * Puede ser INDEFINIDO, FIJO, TEMPORAL o PRESTACIÓN_SERVICIOS.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    @Comment("Tipo de contrato: INDEFINIDO, FIJO, TEMPORAL, PRESTACION_SERVICIOS.")
    private TipoContrato tipoContrato;

    /**
     * Estado actual del contrato laboral.
     * Puede ser ACTIVO, SUSPENDIDO o FINALIZADO.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contrato")
    @Comment("Estado del contrato: ACTIVO, SUSPENDIDO, FINALIZADO.")
    private EstadoContratoLaboral estadoContratoLaboral;
}

