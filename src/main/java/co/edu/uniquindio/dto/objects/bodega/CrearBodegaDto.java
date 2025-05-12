package co.edu.uniquindio.dto.objects.bodega;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.validator.TelefonoValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DTO utilizado para el registro de una nueva bodega.
 * Incluye los datos necesarios para crear una instancia completa de {@link co.edu.uniquindio.model.objects.Bodega}, como su ubicación,
 * dirección, imágenes, teléfono, área total y altura.
 * <p>
 * Las imágenes enviadas en formato PNG o JPG serán procesadas y almacenadas en Cloudinary.
 * Solo las URL resultantes serán persistidas en la base de datos.
 * </p>
 * @param ubicacion Objeto embebido de la clase {@link UbicacionDto}.
 * @param direccion Dirección de la bodega.
 * @param fotos Lista con fotos de la bodega.
 * @param telefono Teléfono principal con el código de país incluido.
 * @param areaTotal Area total de la bodega.
 * @param altura Altura total de la bodega.
 *
 * @author MrZ.Leviatan
 */
@TelefonoValido
public record CrearBodegaDto(

        @NotNull UbicacionDto ubicacion,

        @NotBlank @Length(max = 255) String direccion,

        @NotNull
        List<@NotBlank MultipartFile> fotos,

        @NotBlank @Length(max = 15) String telefono,

        @NotNull @Positive Double areaTotal,

        @NotNull @Positive Double altura
) {
}
