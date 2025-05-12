package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.recursosHumanos.CrearRHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.EditarRRHHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.RecursosHumanosDto;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.model.users.RecursosHumanos;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;

/**
 * Mapper de {@code MapStruct} encargado de convertir entre entidades {@link RecursosHumanos} y sus respectivos DTOs.
 * <p>
 * Este componente facilita la transformación de datos entre las capas de presentación y persistencia para el
 * personal del área de Recursos Humanos. Utiliza {@link SedeMapper} para mapear la información relacionada con la sede.
 *
 * <p> Las reglas de mapeo incluyen inicializar campos con valores por defecto como la fecha de contratación o el estado
 * del contrato, además de ignorar campos como la imagen de perfil que requieren procesamiento adicional.
 *
 * @author MrZ.Leviatan
 */
@Mapper(componentModel = "spring", uses = {SedeMapper .class, QuantityAreaConverter .class})
public interface RecursosHumanosMapper {

    /**
     * Convierte un DTO de creación de Recursos Humanos en una entidad {@link RecursosHumanos}.
     * <p>
     * - Ignora el campo {@code id} ya que será generado automáticamente.<br>
     * - Ignora el campo {@code imagenPerfil} ya que se maneja por separado.<br>
     * - Establece el estado de cuenta del usuario como {@code ACTIVO}.<br>
     * - Establece la fecha de contratación como la fecha actual del sistema.<br>
     * - Establece el estado del contrato laboral como {@code ACTIVO}.
     *
     * @param recursosHumanosDto DTO con los datos necesarios para crear un recurso humano.
     * @return Instancia de {@link RecursosHumanos} lista para ser persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "datosLaborales", source = "datosLaborales")
    @Mapping(target = "datosLaborales.fechaContratacion", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "datosLaborales.estadoContratoLaboral", constant = "ACTIVO")
    @Mapping(target = "sede", source = "sede")
    RecursosHumanos toEntity(CrearRHDto recursosHumanosDto);


    /**
     * Convierte una entidad {@link RecursosHumanos} en su DTO correspondiente {@link RecursosHumanosDto}.
     *
     * @param recursosHumanos Entidad de Recursos Humanos.
     * @return DTO con los datos correspondientes.
     */
    RecursosHumanosDto toDto(RecursosHumanos recursosHumanos);


    /**
     * Actualiza una entidad existente de {@link RecursosHumanos} con los datos proporcionados en el DTO.
     * <p>
     * - Se omiten los campos nulos (no se sobreescriben).<br>
     * - Se ignoran el {@code id} y la {@code imagenPerfil}, ya que no deben modificarse aquí.
     *
     * @param recursosHumanosDto DTO con los datos a actualizar.
     * @param recursosHumanos Entidad de Recursos Humanos que se va a modificar.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toEntity(EditarRRHHDto recursosHumanosDto, @MappingTarget RecursosHumanos recursosHumanos);

}
