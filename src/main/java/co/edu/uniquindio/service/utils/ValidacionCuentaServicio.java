package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;

/**
 * Servicio que contiene las validaciones relacionadas con datos únicos en las cuentas de usuario.
 * <p>
 * Este servicio se encarga de validar que los correos electrónicos (personales y empresariales)
 * y los teléfonos no estén duplicados en el sistema antes de registrar o actualizar una cuenta.
 * </p>
 *
 * <p>
 * Se lanza una excepción si alguno de los datos ya se encuentra registrado por otra persona.
 */
public interface ValidacionCuentaServicio {

    /**
     * Verifica si el correo electrónico ya está registrado en cualquier tipo de usuario.
     * <p>
     * Esta validación se realiza antes de registrar o editar una cuenta,
     * para garantizar que el correo electrónico sea único en el sistema.
     * </p>
     *
     * @param email Email a verificar.
     * @throws ElementoRepetidoException si el email ya está registrado.
     * @throws ElementoEliminadoException si el usuario asociado fue eliminado lógicamente.
     */
    void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException;

    /**
     * Verifica si el correo electrónico empresarial ya está registrado.
     * <p>
     * Se utiliza especialmente en validaciones de agentes de ventas, personal de bodega
     * o personal administrativo, donde se asigna un correo institucional.
     * </p>
     *
     * @param emailEmpresarial Correo institucional a verificar.
     * @throws ElementoRepetidoException si el correo empresarial ya está en uso.
     */
    void validarEmailEmpresarialNoRepetido(String emailEmpresarial)
            throws ElementoRepetidoException;


    /**
     * Verifica si el número de teléfono ya está registrado en cualquier tipo de usuario.
     * <p>
     * Este método asume que el número ya fue formateado correctamente (por ejemplo con libphonenumber).
     * </p>
     *
     * @param telefono Teléfono formateado internacionalmente a verificar.
     * @throws ElementoRepetidoException si el teléfono ya está registrado.
     */
    void validarTelefonoNoRepetido(String telefono)
            throws ElementoRepetidoException;

}
