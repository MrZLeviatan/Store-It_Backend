package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.utils.PhoneServicio;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * {@code Service} que implementa la clase {@link PhoneServicio} para proporcionar utilidades para el manejo de números telefónicos.
 * Incluye validación, formateo e interpretación de teléfonos usando la librería {@code libphonenumber}.
 * <p>
 * Esta clase utiliza la librería `libphonenumber` de Google para validar y formatear números de teléfono.
 * @see <a href="https://github.com/google/libphonenumber">libphonenumber en GitHub</a>
 */
@Service
public class PhoneServicioImpl implements PhoneServicio {


    // Instancia del validador de números telefónicos de la librería de Google
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * Obtiene un número telefónico validado y formateado a partir de su valor crudo y el nombre del país.
     *
     * @param telefono Número de teléfono ingresado por el usuario (sin formato internacional obligatorio).
     * @param pais Nombre del país (ej. "Colombia", "México", "United States").
     * @return Número de teléfono formateado en formato internacional (ej. +57 300 1234567).
     * @throws ElementoNulosException si el país ingresado no es válido.
     * @throws ElementoNoValido si el número de teléfono no pasa la validación.
     * @throws ElementoNoEncontradoException si no se puede determinar el código del país a partir del nombre.
     */
    @Override
    public String obtenerTelefonoFormateado(String telefono, String codigoPais)
            throws ElementoNulosException, ElementoNoValido, ElementoNoEncontradoException {

        // Intentamos obtener el código ISO del país a partir del nombre proporcionado

        // Verificamos que el código de país sea válido
        if (codigoPais.isEmpty()) {
            throw new ElementoNulosException(MensajeError.PAIS_NO_ENCONTRADO);}

        // Validamos que el número coincida con el formato y reglas del país
        if (!validarTelefono(telefono, codigoPais)) {
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO);}

        // Si todo es válido, devolvemos el número formateado
        return formatearTelefono(telefono, codigoPais);
    }


    /**
     * Válida un número telefónico según las reglas del país especificado.
     *
     * @param telefono Número sin formato a validar.
     * @param codigoPais Código ISO del país (ej. "CO", "US").
     * @return true si el número es válido según libphonenumber.
     * @throws ElementoNoValido si ocurre un error al interpretar el número.
     */
    @Override
    public boolean validarTelefono(String telefono, String codigoPais) {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.isValidNumber(numero); // Devuelve true si el número es válido
        } catch (NumberParseException e) {
            // Lanza una excepción personalizada si el número es inválido
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }}


    /**
     * Formatea un número de teléfono al formato internacional (ej. +57 312 4567890).
     *
     * @param telefono Número sin formato.
     * @param codigoPais Código ISO del país (ej. "CO", "US").
     * @return Número formateado listo para almacenar o mostrar.
     * @throws ElementoNoValido si el número no es válido o no se puede interpretar.
     */
    @Override
    public String formatearTelefono(String telefono, String codigoPais) {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.format(numero, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            // Retorna si el número no es válido
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }}


    /**
     * Obtiene el código de país ISO a partir del nombre de un país.
     * Utiliza la clase Locale para recorrer todos los países disponibles.
     *
     * @param pais Nombre completo del país (no debe abreviarse).
     * @return Código ISO del país correspondiente (ej. "CO" para Colombia).
     * @throws ElementoNoEncontradoException si el país no está disponible en los Locales del sistema.
     */
    @Override
    public String obtenerCodigoPaisDesdeNombre(String pais) throws ElementoNoEncontradoException {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry().equalsIgnoreCase(pais)) {
                return locale.getCountry(); // Retorna el código de país
            }}
        // Si no se encuentra el país, se lanza una excepción
        throw new ElementoNoEncontradoException(MensajeError.PAIS_NO_ENCONTRADO);
    }


}