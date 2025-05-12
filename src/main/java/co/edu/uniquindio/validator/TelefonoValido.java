package co.edu.uniquindio.validator;

import co.edu.uniquindio.constants.MensajeError;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Anotación personalizada para validar que los números de teléfono proporcionados
 * (principal y secundario, si aplica) sean válidos conforme al país indicado en la ubicación del cliente.
 * <p>
 * Esta validación se aplica a nivel de clase (por ejemplo, DTO) y utiliza una clase validadora personalizada
 * que emplea la biblioteca libphonenumber para verificar el formato correcto según el código de país.
 *
 * <p><b>Ejemplo de uso:</b>
 * <pre>
 * &#64;TelefonoValido
 * public record EditarClienteDto(...) { ... }
 * </pre>
 *
 * La validación puede ser útil para asegurar coherencia entre el país y los teléfonos ingresados,
 * evitando datos inválidos o inconsistentes.
 *
 * @see TelefonoValidoValidator
 */
@Documented // Indica que esta anotación será incluida en la documentación Javadoc.
@Constraint(validatedBy = TelefonoValidoValidator.class) // Especifica la clase validadora que contiene la lógica de validación.
@Target({ElementType.TYPE}) // Esta anotación se aplica a clases (como DTOs), no a campos individuales.
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución para ser utilizada por el validador.
public @interface TelefonoValido {

    /**
     * Mensaje personalizado que se muestra cuando la validación falla.
     *
     * @return mensaje de error
     */
    String message() default MensajeError.TELEFONO_INVALIDO;


    /**
     * Grupos de validación opcionales (generalmente no utilizados).
     *
     * @return arreglo de clases para agrupar validaciones
     */
    Class<?>[] groups() default {};


    /**
     * Carga útil opcional que puede ser utilizada por los clientes de Bean Validation.
     *
     * @return arreglo de clases tipo Payload
     */
    Class<? extends Payload>[] payload() default {};
}

