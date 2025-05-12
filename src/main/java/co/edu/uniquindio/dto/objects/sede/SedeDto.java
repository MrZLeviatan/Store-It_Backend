package co.edu.uniquindio.dto.objects.sede;

import co.edu.uniquindio.dto.common.UbicacionDto;
import java.util.List;

public record SedeDto(

        Long id,
        String nombre,
        List<String> fotos,
        UbicacionDto ubicacion,
        String direccion,
        String telefono
) {
}
