package co.edu.uniquindio.model.objects;

import co.edu.uniquindio.model.common.Ubicacion;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.RecursosHumanos;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Clase que representa una sede dentro del sistema.
 * <p>
 * Una sede es una instalación física asociada a la empresa, donde trabajan diversas personas
 * como {@link AgenteVentas} y {@link RecursosHumanos}. Cada sede tiene una {@link Ubicacion}
 * y puede tener múltiples empleados asignados.
 *<p>
 * La clase está anotada como {@link Entity} y se mapeaba como una entidad en la base de datos con la tabla {@code sedes}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sedes")
public class Sede {

    /**
     * ID único de la sede.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único de la sede")
    private Long id;

    /**
     * Nombre de la sede.
     */
    @Column(name = "nombre", nullable = false)
    @Comment("Nombre de la sede")
    private String nombre;

    /**
     * {@link Ubicacion} geográfica de la sede, representada como objeto embebido.
     */
    @Embedded
    @Comment("Ubicación geográfica de la Sede.")
    private Ubicacion ubicacion;

    /**
     * Lista de URL de imágenes asociadas a esta sede.
     * Estas imágenes están guardadas en {@code Cloudinary}
     */
    @ElementCollection
    @CollectionTable(name = "sede_fotos", joinColumns
            = @JoinColumn(name = "sede_id"))
    @Column(name = "foto_url")
    @Comment("Lista de imágenes de la sede (URLs).")
    private List<String> fotos;


    /**
     * Dirección física de la sede.
     */
    @Column(name = "direccion")
    @Comment("Dirección física de la sede")
    private String direccion;

    /**
     * Número de contacto telefónico de la sede.
     */
    @Column(name = "telefono")
    @Comment("Número de contacto de la sede")
    private String telefono;

    /**
     * Lista de {@link  AgenteVentas} asignados a esta sede.
     */
    @OneToMany(mappedBy = "sede", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de agentes de ventas que trabajan en esta sede")
    private List<AgenteVentas> agentesVentas;

    /**
     * Lista de {@link RecursosHumanos} a esta sede.
     */
    @OneToMany(mappedBy = "sede", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de personal de recursos humanos que trabajan en esta sede")
    private List<RecursosHumanos> recursosHumanos;

}

