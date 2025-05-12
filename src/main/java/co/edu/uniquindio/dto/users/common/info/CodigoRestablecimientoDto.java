package co.edu.uniquindio.dto.users.common.info;

import java.time.LocalDateTime;

public record CodigoRestablecimientoDto(

        String codigoRestablecimiento,

        LocalDateTime fechaExpiracionCodigoRestablecimiento


) {
}
