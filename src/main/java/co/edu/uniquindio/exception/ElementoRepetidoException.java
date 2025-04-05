package co.edu.uniquindio.exception;

// Clase personalizada de excepción para representar errores cuando un elemento ya existe en el sistema
public class ElementoRepetidoException extends Exception {

    // Constructor que recibe un mensaje personalizado y lo pasa a la clase padre Exception
    public ElementoRepetidoException(String mensaje) {
        super(mensaje); // Llama al constructor de la clase Exception con el mensaje proporcionado
    }
}
