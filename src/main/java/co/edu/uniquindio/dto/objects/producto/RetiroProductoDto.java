package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;

public record RetiroProductoDto(

        @NotBlank String emailCliente,
        @NotBlank Long idPersonal,
        @NotBlank Long idProducto

) {
}
