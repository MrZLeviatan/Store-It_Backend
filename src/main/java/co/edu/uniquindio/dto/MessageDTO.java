package co.edu.uniquindio.dto;

public record MessageDTO<T>(boolean error, T mensaje) {
}