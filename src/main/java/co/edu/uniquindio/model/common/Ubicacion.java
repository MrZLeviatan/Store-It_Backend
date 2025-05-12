package co.edu.uniquindio.model.common;


import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.model.users.Cliente;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * Clase embebida que representa la ubicación geográfica de una persona o entidad dentro del sistema.
 * <p>
 * Esta clase contiene información detallada sobre la posición del usuario, incluyendo país, ciudad,
 * latitud y longitud. Se espera que estos datos sean proporcionados por una API externa de geolocalización.
 * <p>
 * Al ser una clase marcada con {@link Embeddable}, sus atributos se insertan directamente
 * en la tabla de la entidad que la contiene (por ejemplo, {@link Sede } , {@link Cliente }).
 * <p>
 * Ejemplo de uso: mostrar en el frontend la posición exacta del cliente en un mapa a partir de su latitud y longitud.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Ubicacion {


    /**
     * Nombre del país donde reside el cliente o usuario.
     */
    @Column(name = "pais")
    @Comment("País de residencia.")
    private String pais;

    /**
     * Nombre de la ciudad donde reside el cliente o usuario.
     */
    @Column(name = "ciudad")
    @Comment("Ciudad de residencia.")
    private String ciudad;

    /**
     * Coordenada de latitud asociada a la ubicación geográfica.
     * Será utilizada en el frontend para ubicar al cliente en un mapa.
     */
    @Column(name = "latitud")
    @Comment("Latitud geográfica de la ubicación.")
    private Double latitud;

    /**
     * Coordenada de longitud asociada a la ubicación geográfica.
     * Será utilizada en el frontend para ubicar al cliente en un mapa.
     */
    @Column(name = "longitud")
    @Comment("Longitud geográfica de la ubicación.")
    private Double longitud;
}
