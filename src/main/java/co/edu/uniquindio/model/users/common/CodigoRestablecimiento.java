package co.edu.uniquindio.model.users.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Representa los códigos de seguridad para la activación de cuenta y restablecimiento de contraseña.
 * Esta clase está marcada como {@link Embeddable}, lo que permite que sea embebida dentro de otras entidades.
 * <p>
 * Incluye atributos como:
 * <li> Código de activación con su fecha de expiración.
 * <li> Código de restablecimiento de contraseña con su fecha de expiración.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CodigoRestablecimiento {

    /**
     * Código usado para restablecimiento de contraseña.
     */
    @Column(name = "codigo_restablecimiento")
    private String codigoRestablecimiento;


    /**
     * Fecha y hora de expiración del código de restablecimiento de contraseña.
     */
    @Column(name = "codigo_restablecimiento_expiracion")
    private LocalDateTime fechaExpiracionCodigoRestablecimiento;

}
