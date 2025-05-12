package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.objects.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearProductoDto(
        @NotBlank String nombre,
        @NotBlank String descripcion,
        @NotNull Double areaOcupada,
        @NotNull Double altura,
        @NotNull TipoProducto tipoProducto,
        @NotNull Long idEspacio,
        @NotNull String emailCliente,
        @NotNull Long idPersonalBodega


) {
}
