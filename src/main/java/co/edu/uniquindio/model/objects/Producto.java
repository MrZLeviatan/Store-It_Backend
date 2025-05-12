package co.edu.uniquindio.model.objects;

import co.edu.uniquindio.model.objects.enums.EstadoProducto;
import co.edu.uniquindio.model.objects.enums.TipoProducto;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import co.edu.uniquindio.model.users.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.measure.Quantity;
import javax.measure.quantity.Area;
import java.util.List;

/**
 * Entidad que representa un producto almacenado en el sistema Store-It.
 * <p>
 * Contiene datos como nombre, descripción, dimensiones (área y altura), tipo y estado del producto.
 * <p>
 * Relaciones:
 * <ul>
 *   <li>Ubicado en un {@link Espacio} (relación muchos a uno).</li>
 *   <li>Asociado opcionalmente a un {@link Cliente}.</li>
 *   <li>Posee un historial de {@link Movimiento} (relación uno a muchos).</li>
 * </ul>
 * <p>
 * Anotada con {@link Entity} y utiliza Lombok para generar constructores y métodos de acceso.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto {

    /**
     * ID único del producto.
     * <br> Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único del producto")
    private Long id;

    /**
     * Nombre del producto.
     * <br> Product name.
     */
    @Column(name = "nombre", nullable = false)
    @Comment("Nombre del producto")
    private String nombre;

    /**
     * Descripción detallada del producto.
     * <br> Detailed product description.
     */
    @Column(name = "descripcion")
    @Comment("Descripción detallada del producto")
    private String descripcion;

    /**
     * Área ocupada por el producto (m²), usando una unidad de medida estándar.
     * <br> Area occupied by the product in square meters.
     */
    @Convert(converter = QuantityAreaConverter.class)
    @Column(name = "area_ocupada", nullable = false)
    @Comment("Área que ocupa el producto en metros cuadrados.")
    private Quantity<Area> areaOcupada;

    /**
     * Altura del producto en metros.
     * <br> Product height in meters.
     */
    @Column(name = "altura", nullable = false)
    @Comment("Altura del producto en metros.")
    private Double altura;

    /**
     * Tipo de producto: puede ser FRÁGIL o NO_FRÁGIL.
     * <br> Product type: FRAGILE or NON_FRAGILE.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    @Comment("Tipo de producto: FRÁGIL, NO FRÁGIL")
    private TipoProducto tipoProducto;

    /**
     * Estado actual del producto: en bodega, trasladado o retirado.
     * <br> Current product status: in storage, moved, or withdrawn.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_producto", nullable = false)
    @Comment("Estado del producto: en bodega, trasladado o retirado")
    private EstadoProducto estadoProducto;

    /**
     * Espacio al que pertenece el producto.
     * <br> Storage space where the product is located.
     */
    @ManyToOne
    @JoinColumn(name = "espacio_id", nullable = false)
    @Comment("Espacio al que pertenece este producto")
    private Espacio espacio;

    /**
     * Cliente asociado al producto (opcional).
     * <br> Client associated with the product (optional).
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Comment("Cliente asociado al producto.")
    private Cliente cliente;

    /**
     * Lista de movimientos registrados del producto.
     * <br> Movement history of the product.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Historial de movimientos del producto.")
    private List<Movimiento> historialMovimientos;

}