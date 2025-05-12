package co.edu.uniquindio.model.objects;


import co.edu.uniquindio.model.common.Ubicacion;
import co.edu.uniquindio.model.objects.enums.EstadoBodega;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import co.edu.uniquindio.model.users.PersonalBodega;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.measure.Quantity;
import javax.measure.quantity.Area;
import java.util.List;

/**
 * Representa una bodega física que contiene {@link Espacio} de almacenamiento y {@link PersonalBodega} asignado.
 * <p>
 * Cada bodega puede tener múltiples espacios y personal de bodega relacionado.
 * Se registra su ubicación, contacto, dimensiones y ocupación.
 * </p>
 * <p>El área se maneja con unidades del sistema métrico (m²) usando la librería {@link Quantity}.</p>
 *
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code bodegas}.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bodegas")
public class Bodega {


    /**
     * Identificador único generado automáticamente por la base de datos.
     * No puede ser modificado una vez creado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único de la bodega.")
    private Long id;

    /**
     * Objeto embebido de la entidad {@link Ubicacion}
     * Ubicación geográfica de la bodega (país, ciudad, dirección, etc.).
     * Esta clase es embebida, por lo que sus atributos se almacenan en la misma tabla.
     */
    @Embedded
    private Ubicacion ubicacion;

    /**
     * Dirección física detallada de la bodega.
     * Este campo es obligatorio.
     */
    @Column(name = "direccion")
    @Comment("Dirección física de la sede")
    private String direccion;

    /**
     * Lista de URL de imágenes asociadas a esta bodega.
     * Estas imágenes están guardadas en {@code Cloudinary}
     */
    @ElementCollection
    @CollectionTable(name = "bodega_fotos", joinColumns
            = @JoinColumn(name = "bodega_id"))
    @Column(name = "foto_url")
    @Comment("Lista de imágenes de la bodega (URLs).")
    private List<String> fotos;

    /**
     * Número telefónico principal de la bodega.
     * Este campo es obligatorio.
     */
    @Column(name = "telefono", nullable = false)
    @Comment("Número telefónico principal de la bodega.")
    private String telefono;

    /**
     * Área total disponible de la bodega en metros cuadrados.
     * <p>Usa la librería {@link Quantity}</p>
     * Este campo es obligatorio
     */
    @Convert(converter = QuantityAreaConverter.class)
    @Column(name = "area_total", nullable = false)
    @Comment("Área total de la bodega en metros cuadrados.")
    private Quantity<Area> areaTotal;

    /**
     * Altura máxima disponible en la bodega en metros.
     * Este campo es obligatorio
     */
    @Column(name = "altura", nullable = false)
    @Comment("Altura de la bodega en metros.")
    private Double altura;

    /**
     * Estado de la bodega asociada.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_bodega")
    @Comment("Estado de la bodega asociada")
    private EstadoBodega estadoBodega;

    /**
     * Lista de {@link Espacio} que pertenecen a esta bodega.
     */
    @OneToMany(mappedBy = "bodega", cascade = CascadeType.ALL)
    @Comment("Lista de espacios dentro de esta bodega.")
    private List<Espacio> espacios;

    /**
     * Lista del {@link PersonalBodega} asignado a esta bodega.
     */
    @OneToMany(mappedBy = "bodega", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @Comment("Personal de bodega asignado a esta bodega.")
    private List<PersonalBodega> personalBodega;


    /**
     * Calcula el área ocupada por los espacios contratados dentro de la bodega.
     * Solo se suman los espacios que no están libres.
     *
     * @return el área ocupada en metros cuadrados.
     */
    public double calcularAreaOcupada() {
        return espacios.stream()
                .filter(e -> e.getEstadoEspacio() != EstadoEspacio.LIBRE) // Filtramos los espacios que están ocupados
                .mapToDouble(e -> e.getAreaDisponible().to(tech.units.indriya.unit.Units.SQUARE_METRE).getValue().doubleValue())
                .sum(); // Sumamos todas las áreas ocupadas y retornamos el valor total
    }

    /**
     * Calcula el área disponible restante en la bodega.
     *
     * @return metros cuadrados disponibles.
     */
    public double areaDisponible() {
        return areaTotal
                .to(tech.units.indriya.unit.Units.SQUARE_METRE) // Convertimos el área total a metros cuadrados (si no lo está ya)
                .getValue() // Obtenemos el valor numérico
                .doubleValue()  //  Lo convertimos explícitamente a un tipo double
                - // Esto es un 'menos XD'
                calcularAreaOcupada(); // Restamos el área ocupada por los espacios
    }


    /**
     * Verifica si la bodega está completamente llena comparando el área ocupada con el área total.
     * Si está llena, se actualiza automáticamente el estado de la bodega a {@code EstadoEspacioBodega.LLENA}.
     *
     * @return {@code true} si la bodega está completamente llena, {@code false} en caso contrario.
     */
    public boolean estaLlena() {
        double areaTotalMetros = areaTotal
                .to(tech.units.indriya.unit.Units.SQUARE_METRE) // Convertimos el área total a m²
                .getValue()
                .doubleValue();

        double areaOcupada = calcularAreaOcupada();

        boolean llena = areaOcupada >= areaTotalMetros;

        // Si está llena, actualizamos su estado
        if (llena) {
            this.estadoBodega = EstadoBodega.OCUPADA;}

        return llena;
    }


    /**
     * Indica si la bodega está completamente vacía.
     *
     * @return true si no hay ningún espacio ocupado.
     */
    public boolean estaVacia() {
        return calcularAreaOcupada() == 0.0;
    }

    /**
     * Verifica si la bodega tiene al menos un poco de espacio disponible.
     *
     * @return true si aún se puede ocupar espacio, false si está completamente llena.
     */
    public boolean tieneEspacioDisponible(){
        return areaDisponible() > 0;
    }


}
