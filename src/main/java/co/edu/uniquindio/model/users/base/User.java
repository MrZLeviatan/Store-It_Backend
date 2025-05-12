package co.edu.uniquindio.model.users.base;

import co.edu.uniquindio.model.users.common.CodigoRestablecimiento;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

/**
 * Clase embebida que contiene las credenciales y el estado de cuenta de un usuario en el sistema.
 * <p>
 * Esta clase se utiliza como un componente {@link Embeddable}, lo que significa que su información
 * se almacena dentro de la misma tabla de la entidad que la contiene (como {@code Cliente}, {@code AgenteVentas}, etc.).
 * Contiene el email de autenticación, la contraseña cifrada, el estado de la cuenta y un código de validación para activar o verificar el usuario.
 * <p>
 * El atributo {@code email} es único y obligatorio, y está anotado con {@code @Email} para validar su formato.
 * La contraseña debe estar cifrada al momento de guardarse.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class User {

    /**
     * Dirección de correo electrónico única del usuario.
     * Es usada para autenticación en el sistema y debe tener un formato válido.
     */
    @Column(name = "email", nullable = false, unique = true)
    @Comment("Dirección de correo electrónico única para autenticación.")
    @Email
    private String email;

    /**
     * Contraseña cifrada del usuario.
     * Debe ser protegida adecuadamente y no debe almacenarse en texto plano.
     */
    @Column(name = "password", nullable = false)
    @Comment("Contraseña cifrada del usuario para autenticación.")
    private String password;

    /**
     * Estado actual de la cuenta del usuario.
     * Puede ser ACTIVA, INACTIVA o ELIMINADA, de acuerdo al enum {@link EstadoCuenta}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false)
    @Comment("Estado de la cuenta: ACTIVA, INACTIVA, ELIMINADA.")
    private EstadoCuenta estadoCuenta;

    /**
     * Código de seguridad para restablecer la contraseña.
     * Este código es temporal y debe ser válido por un tiempo limitado.
     */
    @Embedded
    private CodigoRestablecimiento codigoRestablecimiento;

}

