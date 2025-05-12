package co.edu.uniquindio.model.users;
import co.edu.uniquindio.model.users.common.DatosLaborales;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.Movimiento;
import co.edu.uniquindio.model.users.base.Persona;
import co.edu.uniquindio.model.users.enums.TipoCargo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Representa al personal de bodega dentro del sistema de gestión Store-It.
 * <p>
 * Esta entidad hereda de {@link Persona} los atributos comunes como nombre, teléfonos y user.
 * Cada cliente se encuentra vinculado a unos {@link DatosLaborales}, tiene un {@link TipoCargo}
 * y mantiene relaciones con una {@link Bodega} y una lista de {@link Movimiento}
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code personal_bodega}.
 *
 */
@Getter
@Setter
@Entity //Mapear en tabla en bd, entidad JPA
@AllArgsConstructor //Constructor sin arg
@NoArgsConstructor //Constructor con arg
@Table(name = "personal_bodega") //Indica en nombre en base de datos.
public class PersonalBodega extends Persona {

    /**
     * Objeto embebido de la entidad {@link DatosLaborales}
     * Información laboral del personal de bodegas, incluyendo fechas de contratación,
     * tipo y estado del contrato, email empresarial y sueldo.
     */
    @Embedded
    private DatosLaborales datosLaborales;

    /**
     * {@link Bodega} a la que pertenece el personal de bodega
     * Esta relación es obligatoria (not null).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "bodega_id", nullable = false)
    @Comment("Bodega a la que está asignado el personal.")
    private Bodega bodega;

    /**
     * Lista de {@link Movimiento} gestionados o asignados al personal de bodega.
     * El personal de bodega puede estar asociado a múltiples contratos.
     */
    @OneToMany(mappedBy = "personalResponsable", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de movimientos en los que el personal ha estado involucrado como responsable.")
    private List<Movimiento> movimientosProducto;

    /**
     * Tipo de cargo: puede ser un encargado, operante o pasante
     * Utiliza el enumerado {@link TipoCargo}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    @Comment("Cargo o rol del personal de bodega, como Encargado o Operario.")
    private TipoCargo tipoCargo;

}
