package co.edu.uniquindio.model.objects;

import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Representa un espacio físico dentro de una {@link Bodega} que puede ser asignado a un {@link Contrato} y contener {@link Producto}.
 * Incluye atributos como área total, área disponible, altura, estado y relaciones con productos y movimientos.
 * <p>
 * El área se maneja con unidades del sistema métrico (m²) usando la librería {@link Quantity}.
 * <p>
 * Contiene métodos utilitarios para calcular área ocupada, verificar disponibilidad y actualizar el estado del espacio.
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code espacios}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

    @Entity
    @Table(name = "espacios")
    public class Espacio {

        /**
         * Identificador único del espacio.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true) // Columna para el ID. No puede ser nula y debe ser única.
        private Long id;

        /**
         *  {@link Contrato} asociado a este espacio (relación uno a uno inversa).
         */
        @OneToOne(mappedBy = "espacio")
        @Comment("Contrato que tiene asignado este espacio.")
        @JsonIgnore // Evitar la serialización de la relación con Bodega
        private Contrato contrato;

        /**
         * Área total del espacio en metros cuadrados.
         * <p>Usa la librería {@link Quantity}</p>
         */
        @Convert(converter = QuantityAreaConverter.class)
        @Column(name = "areaTotal", nullable = false)
        @Comment("Área Total del espacio en metros cuadrados.")
        private Quantity<Area> areaTotal;


        /**
         * Área disponible del espacio en metros cuadrados (área aún no ocupada).
         * <p>Usa la librería {@link Quantity}</p>
         */
        @Convert(converter = QuantityAreaConverter.class)
        @Column(name = "areaDisponible", nullable = false)
        @Comment("Área modificable del espacio en metros cuadrados.")
        private Quantity<Area> areaDisponible;

        /**
         * Altura del espacio en metros.
         */
        @Column(name = "altura", nullable = false)
        @Comment("Altura del espacio en metros.")
        private Double altura;

        /**
         * Estado actual del espacio (libre, contratado disponible o lleno).
         */
        @Enumerated(EnumType.STRING)
        @Column(name = "estado", nullable = false)
        @Comment("Estado del espacio: libre, contratado con disponibilidad o contratado lleno.")
        private EstadoEspacio estadoEspacio;

        /**
         * {@link Bodega} a la que pertenece este espacio.
         */
        @ManyToOne
        @JoinColumn(name = "bodega_id", nullable = false)
        @Comment("Bodega a la que pertenece este espacio")
        @JsonIgnore // Evitar la serialización de la relación con Bodega
        private Bodega bodega;

        /**
         * Lista de {@link Producto} actualmente ubicados en este espacio.
         */
        @OneToMany(mappedBy = "espacio",cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @Comment("Lista de productos que ocupan este espacio")
        @JsonIgnore // Evitar la serialización de la relación con Bodega
        private List<Producto> productos;

        /**
         * {@link Movimiento} de productos registrados en este espacio.
         */
        @OneToMany(mappedBy = "espacio",cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @Comment("Movimientos de productos en este espacio.")
        @JsonIgnore // Evitar la serialización de la relación con Bodega
        private List<Movimiento> movimientos;


        /**
         * Calcula el área total ocupada por los productos dentro del espacio.
         *
         * @return Área ocupada en metros cuadrados (double).
         */
        public double calcularAreaOcupada() {
            return productos.stream()
                    .mapToDouble(p -> p.getAreaOcupada().to(tech.units.indriya.unit.Units.SQUARE_METRE).getValue().doubleValue())
                    .sum(); // Sumamos todas las áreas ocupadas y retornamos el valor total
        }

        /**
         * Verifica si el espacio está completamente lleno.
         *
         * @return true si el área ocupada es igual o mayor al área disponible, false en caso contrario.
         */
        public boolean estaLlena() {
            return calcularAreaOcupada() >= areaDisponible
                    .to(tech.units.indriya.unit.Units.SQUARE_METRE) // Convertimos el área total a metros cuadrados (si no lo está ya)
                    .getValue() // Obtenemos el valor numérico
                    .doubleValue(); // Lo convertimos explícitamente a un tipo double
        }


        /**
         * Actualiza el estado del espacio según el área actualmente ocupada.
         * <ul>
         * <li> CONTRATADO_DISPONIBLE si queda espacio.
         * <li> CONTRATADO_LLENO si el área está completamente ocupada.
         */
        public void actualizarEstadoEspacio() {
            if (estaLlena()){
                estadoEspacio = EstadoEspacio.CONTRATADO_LLENO; // El espacio está completamente lleno
            }else{
                estadoEspacio = EstadoEspacio.CONTRATADO_DISPONIBLE; // El espacio está contratado pero tiene espacio disponible
            }
        }


        /**
         * Calcula el área restante disponible en el espacio, considerando la ocupación actual.
         *
         * @return Área libre restante en metros cuadrados (double).
         */
        public double areaDisponible() {
            return areaDisponible.to(tech.units.indriya.unit.Units.SQUARE_METRE)
                    .getValue()
                    .doubleValue() - calcularAreaOcupada();
        }

        /**
         * Agrega un producto al espacio si hay suficiente área disponible.
         * También actualiza el estado del espacio después de la operación.
         *
         * @param producto el producto a agregar al espacio
         * @throws IllegalStateException si no hay suficiente área disponible para el producto
         */
        public void agregarProducto(Producto producto) {
            // Convertimos el área ocupada del producto a metros cuadrados
            double areaProducto = producto.getAreaOcupada()
                    .to(tech.units.indriya.unit.Units.SQUARE_METRE)
                    .getValue()
                    .doubleValue();

            // Verificamos si el espacio tiene suficiente área disponible
            if (areaProducto > areaDisponible()) {
                throw new IllegalStateException("No hay suficiente área disponible en el espacio para agregar el producto.");
            }
            // Asociamos el producto a este espacio
            producto.setEspacio(this);
            // Agregamos el producto a la lista de productos del espacio
            productos.add(producto);
            // Actualizamos el estado del espacio según la nueva ocupación
            actualizarEstadoEspacio();
        }

}
