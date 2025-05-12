package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.personalBodega.CrearPersonalBodegaDto;
import co.edu.uniquindio.dto.users.personalBodega.EditarPersonalBodegaDto;
import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.mapper.objects.BodegaMapper;
import co.edu.uniquindio.mapper.objects.MovimientoMapper;
import co.edu.uniquindio.model.users.PersonalBodega;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;

import javax.measure.Quantity;
import javax.measure.quantity.Area;

/**
 * Mapper que gestiona la conversión entre la entidad {@link PersonalBodega} y sus respectivos DTOs
 * relacionados con la creación, edición y visualización del personal de bodega.
 *
 * <p>Este mapper hace uso de MapStruct para realizar las transformaciones automáticas de datos,
 * integrándose con Spring mediante {@code componentModel = "spring"}.</p>
 *
 * <p>Utiliza {@link BodegaMapper} y {@link MovimientoMapper} para mapear relaciones anidadas
 * como la bodega asignada y los movimientos realizados por el personal.</p>
 *
 * <p>Funcionalidades clave:</p>
 * <ul>
 *   <li>Crear una entidad {@link PersonalBodega} a partir del DTO {@link CrearPersonalBodegaDto},
 *       inicializando automáticamente algunos campos.</li>
 *   <li>Actualizar una entidad existente desde un {@link EditarPersonalBodegaDto}, ignorando campos nulos.</li>
 *   <li>Transformar una entidad a su DTO {@link PersonalBodegaDto} para visualización.</li>
 * </ul>
 */
@Mapper(componentModel = "spring",
        uses = {QuantityAreaConverter.class,
                MovimientoMapper.class})
public interface PersonalBodegaMapper {


    /**
     * Convierte un objeto {@link CrearPersonalBodegaDto} en una entidad {@link PersonalBodega}.
     *
     * <p>Ignora campos como {@code id} e {@code imagenPerfil} y establece valores predeterminados como
     * {@code estadoCuenta = ACTIVO}, fecha de contratación con la fecha actual, y estado de contrato laboral como ACTIVO.</p>
     *
     * @param personalBodegaDto DTO con la información necesaria para crear un nuevo personal de bodega.
     * @return Entidad {@link PersonalBodega} lista para ser persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "datosLaborales", source = "datosLaborales")
    @Mapping(target = "datosLaborales.fechaContratacion", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "datosLaborales.estadoContratoLaboral", constant = "ACTIVO")
    @Mapping(target = "movimientosProducto", expression = "java(new java.util.ArrayList<>())")
    PersonalBodega toEntity(CrearPersonalBodegaDto personalBodegaDto);


    /**
     * Convierte una entidad {@link PersonalBodega} en su representación {@link PersonalBodegaDto}.
     *
     * @param personalBodega Entidad a convertir.
     * @return DTO con los datos del personal de bodega.
     */
    PersonalBodegaDto toDto(PersonalBodega personalBodega);


    /**
     * Actualiza una entidad {@link PersonalBodega} existente utilizando los datos no nulos del DTO {@link EditarPersonalBodegaDto}.
     *
     * <p>Ignora los campos {@code id} e {@code imagenPerfil} para evitar sobrescrituras no deseadas.</p>
     *
     * @param editarPersonalBodegaDto DTO con la información a actualizar.
     * @param personalBodega Entidad que será actualizada con los valores del DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenPerfil", ignore = true)
    void toEntity (EditarPersonalBodegaDto editarPersonalBodegaDto, @MappingTarget PersonalBodega personalBodega);




}
