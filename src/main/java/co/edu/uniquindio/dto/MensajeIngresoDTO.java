package co.edu.uniquindio.dto;

public record MensajeIngresoDTO<T>(boolean error, T mensaje, T id) {
}
