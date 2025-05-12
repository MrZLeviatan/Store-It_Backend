package co.edu.uniquindio.dto.objects.bodega;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DTO utilizado para editar los datos de una bodega existente.
 * Permite modificar información general como ubicación, dirección,
 * teléfono, área, altura y fotos, de forma parcial o total.
 *
 * @param id         Identificador único de la bodega a modificar. No puede ser nulo.
 * @param ubicacion  Información geográfica actualizada de la bodega (ciudad, latitud, longitud, etc.).
 * @param direccion  Dirección física de la bodega. Máximo 100 caracteres. Opcional.
 * @param telefono   Número telefónico principal con código de país incluido. Máximo 15 caracteres.
 * @param fotos      Lista de archivos de imagen (.jpg o .png) que serán subidos a Cloudinary. Opcional.
 * @param areaTotal  Nueva área total en metros cuadrados. Debe ser un valor positivo. Opcional.
 * @param altura     Nueva altura de la bodega en metros. Debe ser un valor positivo. Opcional.
 *
 * @author MrZ.Leviatan
 */
@TelefonoValido
public record EditarBodegaDto(

        @NotNull Long id,

        UbicacionDto ubicacion,

        @Length(max = 100) String direccion,

        @Length(max = 15) String telefono,

        List<MultipartFile> fotos,

        @Positive Double areaTotal,

        @Positive Double altura

) {
}
