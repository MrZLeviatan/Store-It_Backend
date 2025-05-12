package co.edu.uniquindio.model.users.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * Clase abstracta que representa una persona dentro del sistema.
 * <p>
 * Esta clase actúa como una superclase para entidades como {@code Cliente}, {@code AgenteVentas},
 * {@code PersonalBodega}, entre otras. Contiene atributos comunes como nombre, teléfono principal y secundario,
 * imagen de perfil, y credenciales de {@link User}
 * <p>
 * Está marcada con {@link jakarta.persistence.MappedSuperclass}, por lo tanto, no se mapea directamente como una tabla en la base de datos,
 * pero sus atributos sí serán heredados por las clases que la extiendan.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass // Indica que esta clase es una superclase de entidades, pero no se mapea como tabla.
public abstract class  Persona {

    /**
     * Identificador único generado automáticamente por la base de datos.
     * No puede ser modificado una vez creado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID creado automáticamente por la base de datos
    @Column(name = "id", nullable = false, updatable = false)
    @Comment("ID interno único de la persona creado automáticamente por el sistema.")
    private Long id;

    /**
     * Nombre completo de la persona (incluye nombres y apellidos).
     * Este campo es obligatorio.
     */
    @Column(name = "nombre", nullable = false) // El nombre no puede ser nulo.
    @Comment("Nombre completo de la persona (nombres y apellidos). Si es empresa, nombre de la empresa.")
    private String nombre;

    /**
     * Número telefónico principal de la persona.
     * Este campo es obligatorio.
     */
    @Column(name = "telefono", nullable = false)
    @Comment("Número telefónico principal de contacto.")
    private String telefono;

    /**
     * Número telefónico secundario o alternativo de contacto.
     * Este campo es opcional.
     */
    @Column(name = "telefono_secundario")
    @Comment("Número telefónico alternativo de contacto (opcional).")
    private String telefonoSecundario;

    /**
     * URL de la imagen de perfil asociada a la persona, almacenada por ejemplo en Cloudinary.
     */
    @Column(name = "imagen_perfil_url")
    @Comment("URL de la imagen de perfil de la persona almacenada en Cloudinary. Si es empresa, logo de la empresa.")
    private String imagenPerfil;

    /**
     * Objeto embebido de la entidad {@link co.edu.uniquindio.model.users.base.User} que contiene la información de autenticación del usuario (email y contraseña).
     * <p>
     * Este objeto debe estar anotado con {@code Embeddable}.
     */
    @Embedded // Componente embebido que contiene email y contraseña
    private User user;

}