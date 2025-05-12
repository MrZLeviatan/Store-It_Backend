package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import org.jetbrains.annotations.NotNull;

/**
 * Interfaz que define los métodos relacionados con la autenticación y gestión
 * de cuentas de clientes. Proporciona funcionalidades para registrar cuentas,
 * activar cuentas, iniciar sesión, restablecer contraseñas y gestionar la cuenta
 * de un cliente.
 * <p>
 * @see CrearClienteDto
 * @see LoginDto
 * @see SolicitudEmailDto
 * @see VerificarCodigoDto
 * @see ActualizarPasswordDto
 */
public interface AuthServicio {


    /**
     * Inicia sesión con las credenciales proporcionadas y devuelve un token JWT si las credenciales son válidas.
     *
     * @param dto Objeto que contiene el email y la contraseña del usuario.
     * @return Mapa que contiene el token JWT y el tipo de usuario autenticado.
     */
    TokenDto login(LoginDto dto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;


    TokenDto loginConGoogle(String credential) throws ElementoNoEncontradoException;

    /**
     * Solicita el restablecimiento de contraseña para el usuario con el email proporcionado.
     * Si el email no está registrado, lanza una excepción.
     * @param dto Objeto que contiene el email del usuario.
     * @throws ElementoNoEncontradoException si no se encuentra el email en ninguna cuenta registrada.
     */
    void solicitarRestablecimientoPassword(SolicitudEmailDto dto)
            throws ElementoNoEncontradoException;


    /**
     * Verifica si el código de restablecimiento ingresado es válido para el email indicado.
     * @param dto Objeto que contiene el email y el código de restablecimiento.
     * @throws ElementoIncorrectoException si el código es incorrecto o ha expirado.
     */
    void verificarCodigoPassword(VerificarCodigoDto dto)
            throws ElementoIncorrectoException, ElementoNoEncontradoException;


    /**
     * Actualiza la contraseña del usuario si el código de verificación es válido.
     * @param dto Objeto que contiene el email, el código de verificación y la nueva contraseña.
     * @throws ElementoNoEncontradoException si no se encuentra una cuenta asociada al email.
     */
    void actualizarPassword(ActualizarPasswordDto dto)
            throws ElementoNoEncontradoException;


    /**
     * Reactiva la cuenta de un Cliente asociada al correo electrónico proporcionado.
     * Si el correo no está registrado o presenta inconsistencias, se lanzarán las excepciones correspondientes.
     * @param email Dirección de correo electrónico de la cuenta que se desea reactivar.
     * @throws ElementoIncorrectoException si la información proporcionada es incorrecta o no válida.
     * @throws ElementoNoEncontradoException si no se encuentra una cuenta asociada al correo electrónico proporcionado.
     */
    void reactivarCuenta(String email)
            throws ElementoIncorrectoException, ElementoNoEncontradoException;

}
