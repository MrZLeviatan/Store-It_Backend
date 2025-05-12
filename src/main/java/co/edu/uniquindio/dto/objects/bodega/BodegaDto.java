package co.edu.uniquindio.dto.objects.bodega;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;


import java.util.List;

/**
 * Representación de transferencia de datos (DTO) que encapsula toda la información relevante
 * asociada a una bodega dentro del sistema Store-It.
 * <p>
 * Esta clase se utiliza principalmente en las operaciones de consulta y visualización
 * de bodegas, permitiendo transportar los datos desde el backend hacia el frontend u
 * otros consumidores de la API. Contiene datos estructurados relacionados con la
 * ubicación geográfica, dimensiones físicas, imágenes, contacto, así como la relación
 * con los espacios internos y el personal asignado a la bodega.
 * </p>
 *
 * @param id               Identificador único de la bodega generado por la base de datos.
 * @param ubicacion        Objeto DTO que representa la información geográfica estructurada
 *                         de la bodega {@link  UbicacionDto}
 * @param direccion        Dirección física textual donde se encuentra la bodega.
 * @param fotos            Lista de URLs públicas correspondientes a las imágenes asociadas
 *                         a la bodega. Estas URLs son generadas tras la carga de archivos
 *                         a un proveedor de almacenamiento en la nube (por ejemplo, Cloudinary).
 * @param telefono         Número telefónico principal de contacto de la bodega.
 * @param areaTotal        Dimensión total del área útil de la bodega expresada en metros cuadrados (m²).
 * @param altura           Altura máxima de la bodega en metros.
 * @param espacios         Lista de objetos DTO que representan los espacios definidos dentro
 *                         de la bodega.
 * @param personalBodega   Conjunto de personas asignadas a la operación o supervisión de la bodega.
 */
public record BodegaDto(

        Long id,
        UbicacionDto ubicacion,
        String direccion,
        List<String> fotos,
        String telefono,
        Double areaTotal,
        Double altura

){
}
