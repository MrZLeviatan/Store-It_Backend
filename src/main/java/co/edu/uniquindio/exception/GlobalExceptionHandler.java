package co.edu.uniquindio.exception;


import co.edu.uniquindio.dto.MensajeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ⚠️ Elemento repetido (400 - Bad Request)
    @ExceptionHandler(ElementoRepetidoException.class)
    public ResponseEntity<MensajeDTO<String>> manejarElementoRepetido(ElementoRepetidoException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDTO<>(false, e.getMessage()));
    }

    // ⚠️ Elemento no encontrado (404 - Not Found)
    @ExceptionHandler(ElementoNoEncontradoException.class)
    public ResponseEntity<MensajeDTO<String>> manejarElementoNoEncontrado(ElementoNoEncontradoException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MensajeDTO<>(true, e.getMessage()));
    }

    // ⚠️ Errores de validación de campos (@Valid) (400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeDTO<List<String>>> manejarValidaciones(MethodArgumentNotValidException e) {
        List<String> errores = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDTO<>(true, errores));
    }

    // ⚠️ Cualquier otro error inesperado (500 - Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO<String>> manejarErroresGenerales(Exception e) {
        e.printStackTrace(); // Para depuración en consola
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDTO<>(true, "Error inesperado: " + e.getMessage()));
    }
}
