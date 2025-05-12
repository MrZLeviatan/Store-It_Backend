package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.bodega.CrearBodegaDto;
import co.edu.uniquindio.dto.objects.bodega.EditarBodegaDto;
import co.edu.uniquindio.mapper.users.PersonalBodegaMapper;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;

import javax.measure.Quantity;
import javax.measure.quantity.Area;

/**
 * Mapper para transformar entre la entidad {@link Bodega} y sus respectivos DTOs
 * ({@link CrearBodegaDto}, {@link EditarBodegaDto}, {@link BodegaDto}).
 * Utiliza MapStruct para generar automáticamente la lógica de mapeo, facilitando
 * la conversión de datos entre capas de la aplicación.
 * <p>
 * Este mapper incluye la conversión de tipos especiales como {@code Quantity<Area>}
 * (representando áreas en m²) mediante métodos auxiliares y una clase convertidora.
 * También inicializa listas vacías para relaciones como espacios y personal de bodega,
 * y evita sobrescribir campos nulos durante actualizaciones parciales.
 */
@Mapper(componentModel = "spring", uses =
        {QuantityAreaConverter.class, EspacioMapper.class, PersonalBodegaMapper.class})
public interface BodegaMapper {

    /**
     * Convierte un DTO de creación de bodega a una entidad {@link Bodega}.
     * Algunos campos como fotos, espacios y personal de bodega se inicializan vacíos.
     * <p>
     * Converts a {@link CrearBodegaDto} into a {@link Bodega} entity.
     * Fields like photos, spaces, and staff are initialized as empty.
     *
     * @param crearBodegaDto DTO con los datos de entrada.
     * @return instancia de la entidad Bodega.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "areaTotal", source = "areaTotal", qualifiedByName = "toQuantity")
    @Mapping(target = "espacios",expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "personalBodega", expression = "java(new java.util.ArrayList<>())")
    Bodega toEntity (CrearBodegaDto crearBodegaDto);

    /**
     * Convierte una entidad {@link Bodega} a su representación {@link BodegaDto}.
     * <p>
     * Converts a {@link Bodega} entity into a {@link BodegaDto}.
     *
     * @param bodega la entidad a convertir.
     * @return DTO correspondiente.
     */
    @InheritInverseConfiguration
    @Mapping(source = "areaTotal", target = "areaTotal", qualifiedByName = "toDouble")
    BodegaDto toDto(Bodega bodega);

    /**
     * Actualiza una entidad {@link Bodega} existente con los valores presentes en un DTO de edición.
     * Solo los campos no nulos serán actualizados.
     * <p>
     * Updates an existing {@link Bodega} entity with non-null values from an {@link EditarBodegaDto}.
     *
     * @param editarBodegaDto DTO con los campos modificables.
     * @param bodega entidad objetiva a modificar.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fotos", ignore = true)
    @Mapping(target = "areaTotal", source = "areaTotal", qualifiedByName = "toQuantity")
    void toEntity(EditarBodegaDto editarBodegaDto, @MappingTarget Bodega bodega);



}
