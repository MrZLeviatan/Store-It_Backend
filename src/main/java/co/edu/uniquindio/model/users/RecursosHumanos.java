package co.edu.uniquindio.model.users;


import co.edu.uniquindio.model.users.common.DatosLaborales;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.model.users.base.Persona;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * Entidad que representa al personal del área de Recursos Humanos dentro del sistema Store-It.
 * <p>
 * Esta clase hereda los atributos comunes de {@link Persona} e incluye información específica
 * del ámbito laboral, como los datos contractuales ({@link DatosLaborales}) y la sede ({@link Sede})
 * a la que está asignado.
 * </p>
 *
 * <p>
 * El personal de Recursos Humanos tiene permisos administrativos para gestionar traslados
 * de personal, modificar datos laborales, y consultar la información de los empleados.
 * </p>
 *
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code recursos_humanos}.
 * </p>
 *
 * @see Persona Base class for personal information.
 * @see DatosLaborales Contains laboral information like contract details and salary.
 * @see Sede Represents the physical location where the staff works.
 * @see Entity Marks this class as a JPA entity.
 * @see Table Specifies the table name in the database.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recursos_humanos")
public class RecursosHumanos extends Persona {

    /**
     * Objeto embebido de la entidad {@link DatosLaborales}
     * Información laboral del personal de recursos humanos, incluyendo fechas de contratación,
     * tipo y estado del contrato, email empresarial y sueldo.
     */
    @Embedded
    private DatosLaborales datosLaborales;

    /**
     * {@link Sede} a la que pertenece el personal de recursos humanos
     * Esta relación es obligatoria (not null).
     */
    @ManyToOne // Relación de uno a muchos a una Sede.
    @JoinColumn(name = "id_sede", nullable = false)
    @Comment("Sede en la que trabaja el personal de recursos humanos.")
    private Sede sede;

}
