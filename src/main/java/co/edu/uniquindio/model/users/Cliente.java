package co.edu.uniquindio.model.users;

import co.edu.uniquindio.model.common.Ubicacion;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.objects.Factura;
import co.edu.uniquindio.model.objects.Producto;
import co.edu.uniquindio.model.users.base.Persona;
import co.edu.uniquindio.model.users.enums.TipoCliente;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Representa a un cliente dentro del sistema de gestión Store-It.
 * <p>
 * Esta entidad hereda de {@link Persona} los atributos comunes como {@code nombre, teléfonos y user}.
 * Cada cliente se encuentra vinculado a una {@link Ubicacion}, tiene un {@link TipoCliente}
 * y mantiene relaciones con múltiples contratos, facturas y productos.
 * <p>
 * La clase está anotada como {@link Entity} y se mapea a la tabla {@code clientes}.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@Comment("Entidad que representa a un cliente que firma contratos con la empresa.")
public class Cliente extends Persona {

    /**
     * Objeto embebido de la entidad {@link Ubicacion}
     * Ubicación geográfica del cliente (país, ciudad, dirección, etc.).
     * Esta clase es embebida, por lo que sus atributos se almacenan en la misma tabla.
     */
    @Embedded // Embebemos la clase Ubicación
    private Ubicacion ubicacion;

    /**
     * Tipo de cliente: puede ser una persona natural o una persona jurídica.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    @Comment("Tipo Cliente que contrata el servicio. Natural o Jurídica.")
    private TipoCliente tipoCliente;

    /**
     * Lista de {@link Contrato} asociados a este cliente.
     * Un cliente puede tener varios contratos registrados.
     */
    @OneToMany(mappedBy = "cliente", cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // Relación uno a muchos con contratos
    @Comment("Lista de contratos asociados a este cliente.")
    private List<Contrato> contratos;

    /**
     * Lista de {@link Factura} asociadas al cliente.
     * Cada factura está ligada a un contrato específico firmado por el cliente.
     */
    @OneToMany(mappedBy = "cliente" ,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de facturas asociadas a este cliente")
    private List<Factura> facturas;

    /**
     * Lista de {@link Producto} registrados por este cliente.
     * Estos productos pueden estar almacenados en diferentes espacios dentro del sistema.
     */
    @OneToMany(mappedBy = "cliente", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de productos asociados a este cliente")
    private List<Producto> productos;

}
