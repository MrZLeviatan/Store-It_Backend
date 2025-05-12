package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.utils.impl.PhoneServicioImpl;

/**
 * Clase {@code interface} que se implementara en {@link PhoneServicioImpl} el cual define los métodos necesarios para el manejo y validación de números telefónicos.
 * Los métodos permiten obtener números formateados internacionalmente, validar su existencia y obtener el código de país correspondiente.
 *
 * @author MrZ.Leviatan
 */
public interface PhoneServicio {

    /**
     * Obtiene un número de teléfono validado y formateado en el formato internacional, según el país especificado.
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li> Obtiene el código de país basado en el nombre del país proporcionado.</li>
     * <li> Válida que el número de teléfono es válido según las reglas de formato del país. </li>
     * <li> Formatea el número en formato internacional. </li>
     *
     * @param telefono El número de teléfono a validar y formatear.
     * @param codigoPais El nombre del país cuyo código será utilizado para validar el número.
     * @return El número de teléfono formateado en formato internacional.
     * @throws ElementoNulosException Si el nombre del país proporcionado es inválido o no encontrado.
     * @throws ElementoIncorrectoException Si el número de teléfono proporcionado no es válido para el país.
     * @throws ElementoNoEncontradoException Si no se encuentra el país especificado en la lista de países disponibles.
     */
    String obtenerTelefonoFormateado(String telefono, String codigoPais)
            throws ElementoNulosException, ElementoIncorrectoException,
            ElementoNoEncontradoException;

    /**
     * Válida si un número de teléfono es correcto según el formato del país especificado.
     * Utiliza la librería {@code libphonenumber} para analizar y verificar si el número es válido.
     *
     * @param telefono El número de teléfono a validar.
     * @param codigoPais El código de país que se utilizará para la validación.
     * @return true si el número de teléfono es válido según el formato del país; false de lo contrario.
     * @throws ElementoNoValido Si el número de teléfono no es válido según las reglas del país.
     */
    boolean validarTelefono(String telefono, String codigoPais)
            throws ElementoNoValido;

    /**
     * Formatea un número de teléfono en el formato internacional estándar (ejemplo: +1 234-567-890).
     * Utiliza la librería {@code libphonenumber} para convertir el número de teléfono al formato internacional.
     *
     * @param telefono El número de teléfono a formatear.
     * @param codigoPais El código de país para el formateo.
     * @return El número de teléfono formateado en formato internacional.
     * @throws ElementoNoValido Si el número de teléfono no es válido según las reglas del país.
     */
    String formatearTelefono(String telefono, String codigoPais)
            throws ElementoNoValido;

    /**
     * Obtiene el código del país a partir del nombre del país proporcionado.
     * Utiliza la clase {@code Locale} de Java para mapear el nombre del país al código de país correspondiente.
     *
     * @param pais Nombre del país (ejemplo: "Colombia", "United States").
     * @return El código de país correspondiente (ejemplo: "CO", "US").
     * @throws ElementoNoEncontradoException Si no se encuentra el país especificado.
     */
    String obtenerCodigoPaisDesdeNombre(String pais)
            throws ElementoNoEncontradoException;
}
