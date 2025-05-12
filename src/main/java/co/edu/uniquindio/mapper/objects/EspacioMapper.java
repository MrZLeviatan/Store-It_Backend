package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.espacio.CrearEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EditarEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;

import javax.measure.Quantity;
import javax.measure.quantity.Area;

/**
 * Mapper responsable de convertir entre las entidades {@link Espacio} y sus respectivos DTOs.
 * Utiliza otros mappers y conversores para realizar las conversiones de tipos complejos como {@code Quantity<Area>}.
 *
 * Este mapper permite:
 * - Crear una entidad {@link Espacio} desde un {@link CrearEspacioDto}.
 * - Convertir una entidad {@link Espacio} a un {@link EspacioDto}.
 * - Actualizar parcialmente una entidad {@link Espacio} a partir de un {@link EditarEspacioDto}.
 *
 * Se apoya en MapStruct para generar el código de mapeo de manera automática.
 *
 * @author MrZ.Leviatan
 */
@Mapper(componentModel = "spring", uses =
        {QuantityAreaConverter.class})
public interface EspacioMapper {


    /**
     * Convierte un objeto {@link CrearEspacioDto} en una entidad {@link Espacio}.
     * Se inicializan propiedades como el estado (por defecto en LIBRE), productos y movimientos vacíos.
     * El ID y contrato son ignorados en la creación.
     *
     * @param espacioDto DTO con los datos necesarios para crear un nuevo espacio.
     * @return Entidad {@link Espacio} lista para ser persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    @Mapping(target = "areaTotal", source = "areaTotal", qualifiedByName = "toQuantity")
    @Mapping(target = "areaDisponible", source = "areaTotal", qualifiedByName = "toQuantity")
    @Mapping(target = "estadoEspacio", constant = "LIBRE")
    @Mapping(target = "productos",expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "movimientos",expression = "java(new java.util.ArrayList<>())")
    Espacio toEntity(CrearEspacioDto espacioDto);

    /**
     * Convierte una entidad {@link Espacio} en su representación DTO {@link EspacioDto}.
     * Se aplica la configuración inversa al mapeo de creación y se convierte el área a {@code Double}.
     *
     * @param espacio Entidad {@link Espacio} que se desea convertir.
     * @return DTO {@link EspacioDto} representando la entidad.
     */
    @InheritInverseConfiguration
    @Mapping(source = "areaTotal", target = "areaTotal", qualifiedByName = "toDouble")
    @Mapping(source = "areaDisponible", target = "areaDisponible", qualifiedByName = "toDouble")
    EspacioDto toDto(Espacio espacio);

    /**
     * Aplica los valores de un {@link EditarEspacioDto} a una entidad existente {@link Espacio}.
     * Solo los campos no nulos del DTO serán aplicados, el resto se ignoran.
     *
     * @param editarEspacioDto DTO con los nuevos valores a aplicar.
     * @param espacio Entidad destinó que se actualizará.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "areaTotal", source = "areaTotal", qualifiedByName = "toQuantity")
    void toEntity (EditarEspacioDto editarEspacioDto, @MappingTarget Espacio espacio);



}