package co.edu.uniquindio.model.objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

/**
 * Clase que representa un detalle asociado a una factura en el sistema de facturación.
 * <p>
 * Esta entidad se utiliza para desglosar los diferentes contratos o espacios vinculados a una factura
 * específica, proporcionando información detallada sobre cada uno de estos elementos, como su valor
 * individual y el espacio alquilado asociado.
 * </p>
 * Uso típico:
 * Se utiliza junto con la clase {@link Factura} para modelar el desglose de una factura y sus
 * relaciones con contratos y espacios alquilados en el sistema.
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code detalle_factura}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {

    // ID único de la entidad.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único del detalle de factura")
    private Long id;

    /**
     * Representa una relación ManyToOne entre esta entidad y la entidad {@link Factura},
     */
    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    @Comment("Factura a la que pertenece este detalle.")
    private Factura factura;

    /**
     * Representa una relación ManyToOne entre esta entidad y la entidad {@link Contrato},
     */
    @ManyToOne
    @JoinColumn(name = "id_contrato", nullable = false)
    @Comment("Contrato asociado a este detalle.")
    private Contrato contrato;

    // Valor específico del contrato en esta factura
    @Column(name = "valor")
    @Comment("Valor específico del contrato en esta factura")
    private BigDecimal valor;


    /**
     * Constructor que carga directamente los datos desde un contrato.
     * Útil para inicializar automáticamente el detalle desde la lógica de creación de factura.
     */
    public DetalleFactura(Factura factura, Contrato contrato) {
        this.factura = factura;
        this.contrato = contrato;
        this.valor = contrato.getValor();
    }


}
