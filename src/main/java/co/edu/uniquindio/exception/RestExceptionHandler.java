package co.edu.uniquindio.exception;

import co.edu.uniquindio.dto.MensajeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    // Maneja ElementoNoEncontradoException (404)
    @ExceptionHandler(ElementoNoEncontradoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoNoEncontrado(ElementoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoRepetidoException (409)
    @ExceptionHandler(ElementoRepetidoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoRepetido(ElementoRepetidoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoNulosException (400)
    @ExceptionHandler(ElementoNulosException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoNulo(ElementoNulosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoIncorrectoException (400)
    @ExceptionHandler(ElementoIncorrectoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoIncorrecto(ElementoIncorrectoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoNoValido (422)
    @ExceptionHandler(ElementoNoValido.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoNoValido(ElementoNoValido ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoNoActivadoException (403)
    @ExceptionHandler(ElementoNoActivadoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoNoActivado(ElementoNoActivadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoEliminadoException (410)
    @ExceptionHandler(ElementoEliminadoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoEliminado(ElementoEliminadoException ex) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja ElementoAunEnUsoException (423)
    @ExceptionHandler(ElementoAunEnUsoException.class)
    public ResponseEntity<MensajeDTO<String>> handleElementoEnUso(ElementoAunEnUsoException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja CargaFallidaException (500)
    @ExceptionHandler(CargaFallidaException.class)
    public ResponseEntity<MensajeDTO<String>> handleCargaFallida(CargaFallidaException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja SinAgentesDisponiblesException (503)
    @ExceptionHandler(SinAgentesDisponiblesException.class)
    public ResponseEntity<MensajeDTO<String>> handleSinAgentes(SinAgentesDisponiblesException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja SolicitarReactivacionException (403)
    @ExceptionHandler(SolicitarReactivacionException.class)
    public ResponseEntity<MensajeDTO<String>> handleReactivacion(SolicitarReactivacionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensajeDTO<>(true, ex.getMessage()));
    }

    // Maneja excepciones no controladas (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO<String>> handleExcepcionGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDTO<>(true, "Error interno del servidor: " + ex.getMessage()));
    }
}

