package co.edu.uniquindio.model.objects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * Representa un contrato en el sistema, que vincula un {@link Cliente} con un {@link AgenteVentas} y un {@link Espacio}.
 * El contrato contiene detalles sobre su duración, estado, valor económico y facturas asociadas.
 * <p>
 * Se transformara en PDF para ser enviado mediante un Email
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code contratos}.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contratos")
public class Contrato {

    /**
     * ID único del contrato generado automáticamente.
     * Este campo se genera automáticamente y no se puede actualizar una vez asignado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("ID único del contrato generado automáticamente.")
    private Long id;

    /**
     * Fecha de inicio del contrato.
     * Representa la fecha en que entra en vigencia el contrato.
     */
    @Column(name = "fecha_inicio", nullable = false)
    @Comment("Fecha de inicio del contrato.")
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del contrato.
     * Representa la fecha en que el contrato termina o es cancelado.
     */
    @Column(name = "fecha_fin", nullable = false)
    @Comment("Fecha de finalización del contrato.")
    private LocalDate fechaFin;


    /**
     * Fecha de firma del Cliente en el contrato
     * Representa la fecha en la que el contrato fue firmado por el cliente
     */
    @Column(name = "fecha_firmaCliente")
    @Comment("Fecha en que el Cliente firma el contrato")
    private LocalDateTime fechaFirmaCliente;

    /**
     * Estado actual del contrato.
     * Los posibles valores son: ACTIVO, FINALIZADO, CANCELADO, INACTIVO.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Comment("Estado actual del contrato: ACTIVO, FINALIZADO, CANCELADO")
    private EstadoContrato estadoContrato;

    /**
     * Valor económico total del contrato.
     * Este campo representa el monto total asociado al contrato, en formato decimal.
     */
    @Column(name = "valor", nullable = false)
    @Comment("Valor económico total del contrato.")
    private BigDecimal valor;

    /**
     * Descripción del contrato.
     * Este campo proporciona información adicional o detalles acerca del contrato.
     */
    @JoinColumn(name = "descripcion")
    @Comment("Descripción del contrato")
    private String descripcion;

    /**
     * Cliente al que pertenece este contrato.
     * Relación muchos a uno con la entidad {@link Cliente}, donde cada contrato está asociado a un único cliente.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @Comment("Cliente al que pertenece este contrato.")
    private Cliente cliente;

    /**
     * Agente de ventas asociado a este contrato.
     * Relación muchos a uno con la entidad {@link AgenteVentas}, donde cada contrato está asociado a un agente de ventas.
     */
    @ManyToOne
    @JoinColumn(name = "id_agente_ventas", nullable = false)
    @Comment("Agente de ventas asociado a esta factura")
    private AgenteVentas agenteVentas;

    /**
     * Espacio asignado a este contrato.
     * <p>
     * Relación uno a uno con la entidad {@link Espacio}, donde cada contrato está asociado a un único espacio.
     * </p>
     */
    @OneToOne
    @JoinColumn(name = "espacio_id", nullable = false, unique = true)
    @Comment("Espacio asignado a este contrato.")
    private Espacio espacio;


    /**
     * Imagen de la firma del cliente.
     */
    @Lob // Large Object para almacenar imágenes
    @Column(name = "firma_cliente")
    private byte[] firmaCliente; // Client signature image

    /**
     * Imagen de la firma del agente de ventas
     */
    @Lob
    @Column(name = "firma_agenteVentas")
    private byte[] firmaAgenteVentas;

    /**
     * Facturas asignadas a este contrato.
     * Relación uno a muchos con la entidad {@link DetalleFactura}, donde un contrato puede estar presente en varias facturas.
     */
    @OneToMany(mappedBy = "contrato", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Facturas asignadas a este contrato.")
    private List<DetalleFactura> detallesFactura;

}
