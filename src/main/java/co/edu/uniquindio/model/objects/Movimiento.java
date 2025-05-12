package co.edu.uniquindio.model.objects;

import co.edu.uniquindio.model.objects.enums.TipoMovimiento;
import co.edu.uniquindio.model.users.PersonalBodega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * Entidad que representa un movimiento realizado sobre un {@link Producto} dentro del sistema.
 * <p>
 * Esta clase almacena información detallada sobre cada operación realizada, incluyendo:
 * <ul>
 *   <li>El tipo de movimiento (ingreso, retiro, traslado).</li>
 *   <li>La fecha en que ocurrió el movimiento.</li>
 *   <li>El {@link Espacio} involucrado en la operación.</li>
 *   <li>El personal responsable del movimiento.</li>
 *   <li>Cualquier observación adicional registrada.</li>
 * </ul>
 * <p>
 * Esta entidad forma parte del módulo de control de inventario del sistema Store-It.
 * Está anotada con {@link Entity} y se mapea a la tabla {@code movimientos_producto} en la base de datos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movimientos_producto")
public class Movimiento {


    /**
     * Identificador único generado automáticamente por la base de datos.
     * No puede ser modificado una vez creado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID único del movimiento.")
    private Long id;

    /**
     * {@link Producto} asociado al movimiento ha registras.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    @Comment("Producto al que corresponde este movimiento.")
    private Producto producto;

    /**
     * Tipo de movimiento: Puede ser Ingreso, Retiro
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    @Comment("Tipo de movimiento: ingreso, retiro.")
    private TipoMovimiento tipoMovimiento;

    /**
     * Fecha en que se llevó a cabo el movimiento.
     */
    @Column(name = "fecha_movimiento", nullable = false)
    @Comment("Fecha y hora en que se realizó el movimiento")
    private LocalDateTime fechaMovimiento;

    /**
     * {@link Espacio} donde se llevó el movimiento
     */
    @ManyToOne
    @JoinColumn(name = "espacio_id")
    @Comment("ID del espacio de origen")
    private Espacio espacio;

    /**
     * {@link PersonalBodega} responsable de llevar el movimiento.
     */
    @ManyToOne
    @JoinColumn(name = "personal_bodega_id", nullable = false)
    @Comment("Personal de bodega responsable del movimiento del producto.")
    private PersonalBodega personalResponsable;

    /**
     * Detalles del movimiento
     */
    @Column(name = "detalle")
    @Comment("Observaciones adicionales del movimiento")
    private String detalle;

}
