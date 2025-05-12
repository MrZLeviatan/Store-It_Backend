package co.edu.uniquindio.validator;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.service.utils.impl.PhoneServicioImpl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Validador que implementa la lógica de validación para la anotación personalizada {@link TelefonoValido}.
 * <p>
 * Este validador utiliza reflexión para obtener los valores de los métodos esperados en el DTO anotado
 * (como `teléfono()`, `teléfonoSecundario()` y `ubicación()`), y válida los teléfonos según el país proporcionado
 * en la ubicación, utilizando el servicio {@link PhoneServicioImpl} que integra la librería libphonenumber.
 *
 * <p>Se asume que el DTO tiene una estructura compatible con los métodos mencionados.</p>
 *
 * <p><b>Reglas de validación:</b></p>
 * <ul>
 *     <li>Si el teléfono o el país son nulos, se omite la validación (considerado válido).</li>
 *     <li>El número debe coincidir con el formato internacional válido según el país especificado.</li>
 *     <li>El teléfono secundario es opcional, pero si se proporciona, también debe ser válido.</li>
 *     <li>En caso de errores de reflexión, la validación se considera aprobada para evitar bloqueos.</li>
 * </ul>
 */
@Component // 🇪🇸 Componente de Spring para permitir la inyección de dependencias
public class TelefonoValidoValidator implements ConstraintValidator<TelefonoValido, Object> {

    @Autowired
    private PhoneServicioImpl phoneServiceImpl; // 🇪🇸 Servicio que encapsula la lógica de libphonenumber

    /**
     * 🇪🇸 Método que valida los teléfonos de un DTO usando el código del país directamente.
     *
     * @param dto Objeto con los datos a validar. Se espera que tenga `telefono()`, `telefonoSecundario()` y `codigoTelefono()`.
     * @param context Contexto de validación
     * @return true si ambos teléfonos son válidos o no requeridos; false si alguno no cumple el formato.
     */
    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        try {
            // 🇪🇸 Obtenemos el teléfono principal
            Method getTelefono = dto.getClass().getMethod("telefono");
            String telefono = (String) getTelefono.invoke(dto);

            // 🇪🇸 Obtenemos el teléfono secundario
            Method getTelefonoSecundario = dto.getClass().getMethod("telefonoSecundario");
            String telefonoSecundario = (String) getTelefonoSecundario.invoke(dto);

            // 🇪🇸 Obtenemos el código telefónico del país directamente (por ejemplo, "CO", "+57", etc.)
            Method getCodigoTelefono = dto.getClass().getMethod("codigoTelefono");
            String codigoTelefono = (String) getCodigoTelefono.invoke(dto);

            Method getCodigoTelefonoSecundario = dto.getClass().getMethod("codigoTelefonoSecundario");
            String codigoTelefonoSecundario = (String) getCodigoTelefonoSecundario.invoke(dto);

            // 🇪🇸 Si no hay teléfono o código país, se omite la validación
            if (telefono == null || codigoTelefono == null) return true;

            // 🇪🇸 Validamos ambos teléfonos
            boolean telefonoValido = phoneServiceImpl.validarTelefono(telefono, codigoTelefono);
            boolean telefonoSecValido = telefonoSecundario == null
                    || phoneServiceImpl.validarTelefono(telefonoSecundario, codigoTelefonoSecundario);

            return telefonoValido && telefonoSecValido;

        } catch (Exception e) {
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO, e);
        }
    }
}


