package co.edu.uniquindio.exception;

/**
 * Excepción personalizada que se lanza cuando ocurre un error durante la carga
 * de un archivo, recurso o datos en el sistema.
 *
 * <p>Esta excepción extiende de {@link RuntimeException}, por lo tanto, no necesita
 * ser declarada explícitamente en los métodos que la lanzan.</p>
 *
 * <p>Usualmente, se utiliza para manejar errores en procesos como:</p>
 * <ul>
 *     <li>Subida de archivos a un servidor o almacenamiento en la nube.</li>
 *     <li>Lectura de recursos externos.</li>
 *     <li>Procesos de importación de datos.</li>
 * </ul>
 *
 * @author MrZ.Leviatan
 */
public class CargaFallidaException extends RuntimeException{

    public CargaFallidaException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }

}
