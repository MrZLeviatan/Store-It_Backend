package co.edu.uniquindio.exception;

/**
 * Excepción personalizada que se lanza cuando un elemento proporcionado no es válido
 * según las reglas del negocio o las validaciones aplicadas.
 *
 * <p>Esta clase extiende de {@link RuntimeException}, por lo que es una excepción no comprobada.</p>
 *
 * <p>Puede utilizarse para representar errores de validación lógica o de integridad
 * en los datos antes de ser procesados o persistidos.</p>
 *
 * Ejemplos de uso:
 * <ul>
 *     <li>Cuando se intenta registrar un recurso con datos incorrectos.</li>
 *     <li>Cuando una condición de negocio no se cumple.</li>
 * </ul>
 *
 * @author MrZ.Leviatan
 */
public class ElementoNoValido extends RuntimeException {

    /**
     * Crea una nueva instancia de la excepción con un mensaje personalizado.
     *
     * @param message mensaje que describe el motivo de la excepción.
     */
    public ElementoNoValido(String message) {
        super(message);
    }

    /**
     * Crea una nueva instancia de la excepción con un mensaje y una causa asociada.
     *
     * @param message mensaje que describe el motivo de la excepción.
     * @param cause causa original que provocó esta excepción.
     */
    public ElementoNoValido(String message, Throwable cause) {super(message,cause);}

}
