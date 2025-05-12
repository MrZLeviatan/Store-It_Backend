package co.edu.uniquindio.model.users;

import co.edu.uniquindio.model.users.common.DatosLaborales;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.users.base.Persona;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;


/**
 * Representa un agente de ventas dentro del sistema.
 * Esta entidad extiende la clase {@link Persona}, heredando sus atributos comunes como nombre,
 * teléfonos, correo.
 * <p>
 * Cada agente está asociado a una {@link Sede} específica y puede tener múltiples {@link Contrato} asignados.
 * Además, se incluyen los {@link DatosLaborales} relevantes para su gestión en la empresa.
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code agente_ventas}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agente_ventas")
public class AgenteVentas extends Persona {

    /**
     * Objeto embebido de la entidad {@link DatosLaborales}
     * Información laboral del agente de ventas, incluyendo fechas de contratación,
     * tipo y estado del contrato, email empresarial y sueldo.
     */
    @Embedded
    private DatosLaborales datosLaborales;

    /**
     * {@link Sede} a la que pertenece el agente de ventas.
     * Esta relación es obligatoria (not null).
     */
    @ManyToOne // Relación de uno a muchos a una Sede.
    @JoinColumn(name = "id_sede", nullable = false)
    @Comment("Sede en la que trabaja el agente de ventas.")
    private Sede sede;

    /**
     * Lista de {@link Contrato} gestionados o asignados al agente de ventas.
     * Un agente puede estar asociado a múltiples contratos.
     */
    @OneToMany(mappedBy = "agenteVentas", cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // Relación de muchos a un Contrato.
    @Comment("Contratos asociadas al agente de ventas.")
    private List<Contrato> contratos;


}

