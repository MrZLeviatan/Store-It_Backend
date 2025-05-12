package co.edu.uniquindio.dto.common;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO que representa una ubicación geográfica con información específica sobre el país,
 * ciudad, latitud y longitud. Este registro es utilizado para modelar coordenadas
 * geográficas dentro del sistema.
 * <p>
 * Cada campo está validado para garantizar que los datos sean consistentes y estén completos.
 * Los valores de latitud y longitud deben cumplir con los rangos geográficos permitidos:
 * - Latitud: entre -90.0 y 90.0 grados.
 * - Longitud: entre -180.0 y 180.0 grados.
 * <p>
 * @param pais    Nombre del país. No puede ser nulo ni vacío.
 * @param ciudad  Nombre de la ciudad. No puede ser nulo ni vacío.
 * @param latitud Coordenada de latitud geográfica en formato decimal. No puede ser nula y debe estar dentro del rango permitido.
 * @param longitud Coordenada de longitud geográfica en formato decimal. No puede ser nula y debe estar dentro del rango permitido.
 */
public record UbicacionDto(

        @NotBlank String pais,
        @NotBlank String ciudad,
        @NotNull
        @DecimalMin(value = "-90.0", inclusive = true, message = "Latitud inválida.")
        @DecimalMax(value = "90.0", inclusive = true, message = "Latitud inválida.")
        Double latitud,

        @NotNull
        @DecimalMin(value = "-180.0", inclusive = true, message = "Longitud inválida.")
        @DecimalMax(value = "180.0", inclusive = true, message = "Longitud inválida.")
        Double longitud

) {
}
