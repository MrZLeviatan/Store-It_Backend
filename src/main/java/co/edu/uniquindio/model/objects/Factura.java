package co.edu.uniquindio.model.objects;

import co.edu.uniquindio.model.objects.enums.EstadoFactura;
import co.edu.uniquindio.model.users.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase que representa una factura emitida a un {@link Cliente} en el sistema Store-It.
 * <p>
 * Esta entidad contiene toda la información necesaria para el registro y control de facturación,
 * incluyendo el cliente asociado, fechas clave, el porcentaje de IVA aplicado, el valor total,
 * el estado de la factura y los detalles facturados. Es fundamental para los procesos de
 * contabilidad y control financiero del sistema.
 *
 * <p><b>Características principales:</b></p>
 * <ul>
 *   <li>Relación con cliente y detalles de factura.</li>
 *   <li>Cálculo automático del valor total con IVA.</li>
 *   <li>Asignación automática de fecha de pago basada en la fecha de emisión.</li>
 * </ul>
 *
 * Esta clase forma parte del módulo de facturación del sistema y está directamente relacionada con
 * entidades como {@link Cliente}, {@link DetalleFactura}, y el enum {@link EstadoFactura}.
 *
 * <p> La clase está anotada como {@link Entity} y se mapea a la tabla {@code facturas}.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "facturas")
public class Factura {

    /**
     * ID único de la factura.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("ID único de la factura")
    private Long id;

    /**
     * {@link Cliente} asociado a esta factura.
     */
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @Comment("Cliente asociado a esta factura")
    private Cliente cliente;

    /**
     * Fecha en la que se emitió la factura.
     */
    @Column(name = "fecha_emision", nullable = false)
    @Comment("Fecha en la que se emitió la factura")
    private LocalDate fechaEmision;

    /**
     * Fecha en que se realizó el pago de la factura (si ya fue pagada).
     */
    @Column(name = "fecha_pago")
    @Comment("Fecha en que se realizó el pago de la factura (si ya fue pagada)")
    private LocalDate fechaPago;

    /**
     * Porcentaje de IVA aplicado a la factura.
     */
    @Column(name = "iva", nullable = false)
    @Comment("Porcentaje de IVA aplicado a la factura")
    private BigDecimal iva;

    /**
     * Monto total a pagar por el contrato incluyendo IVA.
     */
    @Column(name = "valor", nullable = false)
    @Comment("Monto total a pagar por el contrato incluyendo IVA")
    private BigDecimal valorTotal;

    /**
     * Estado de la factura (pendiente, pagada, cancelada).
     */
    @Enumerated(EnumType.STRING)
    @Comment("Estado del contrato (pendiente, pagada, cancelada)")
    private EstadoFactura estadoFactura;

    /**
     * Lista de {@link DetalleFactura} incluidos en la factura.
     */
    @OneToMany(mappedBy = "factura", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<DetalleFactura> detallesFactura;

    /**
     * Calcula el valor total de la factura sumando los valores de los detalles y aplicando el IVA.
     * @return valor total con IVA incluido
     */
    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleFactura detalle : detallesFactura) {
            total = total.add(detalle.getValor());
        }
        // Se aplica IVA al valor total
        return total.add(total.multiply(iva));
    }


    /**
     * Establece la fecha de emisión y automáticamente calcula la fecha de pago como un mes después.
     * @param fechaEmision fecha en la que se emite la factura
     */
    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
        this.fechaPago = this.fechaEmision.plusMonths(1);
    }
}