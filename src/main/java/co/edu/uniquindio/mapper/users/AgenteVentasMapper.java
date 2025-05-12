package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.CrearAgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.EditarAgenteVentasDto;
import co.edu.uniquindio.mapper.objects.ContratoMapper;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;


/**
 * Mapper encargado de realizar las conversiones entre la entidad {@link AgenteVentas}
 * y sus respectivos DTO, utilizando MapStruct como motor de mapeo automático.
 * <p>Esta interfaz permite transformar objetos de transferencia de datos relacionados con
 * los agentes de ventas, tales como {@link CrearAgenteVentasDto}, {@link EditarAgenteVentasDto}
 * y {@link AgenteVentasDto}, hacia y desde la entidad principal.</p>
 * <p>Está integrada con Spring mediante {@code componentModel = "spring"} y hace uso de
 * {@link SedeMapper} y {@link ContratoMapper} para mapear las relaciones anidadas.</p>
 * <p>Funcionalidades clave:</p>
 * <ul>
 *   <li>Convertir un DTO de creación a una entidad {@link AgenteVentas} con estado activo por defecto.</li>
 *   <li>Actualizar un agente existente con datos desde un DTO de edición, ignorando campos nulos.</li>
 *   <li>Convertir una lista de entidades a una lista de DTO.</li>
 * </ul>
 * <p>Nota: Algunos campos como {@code imagenPerfil} o {@code id} son ignorados para evitar
 * sobre escrituras no deseadas. El campo {@code fechaContratación} se inicializa automáticamente.</p>
 */
@Mapper(componentModel = "spring" , uses = {
        SedeMapper.class, ContratoMapper.class,
        QuantityAreaConverter .class})
public interface AgenteVentasMapper {

    /**
     * Convierte un DTO de creación de agente de ventas en una entidad {@link AgenteVentas}.
     * <p>
     * Los campos {@code id} e {@code imagenPerfil} se ignoran para evitar sobre escrituras no deseadas.
     * El estado de la cuenta se establece como {@code ACTIVO} por defecto.
     * También se inicializa la fecha de contratación con la fecha actual y el estado del contrato laboral como {@code ACTIVO}.
     * </p>
     *
     * @param agente el DTO con los datos para crear el agente.
     * @return la entidad {@link AgenteVentas} lista para ser persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "datosLaborales", source = "datosLaborales")
    @Mapping(target = "datosLaborales.fechaContratacion", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "datosLaborales.estadoContratoLaboral", constant = "ACTIVO")
    @Mapping(target = "contratos", expression = "java(new java.util.ArrayList<>())")
    AgenteVentas toEntity(CrearAgenteVentasDto agente);


    /**
     * Convierte una entidad {@link AgenteVentas} en su representación DTO {@link AgenteVentasDto}
     *
     * @param agente la entidad a convertir.
     * @return el DTO {@link AgenteVentasDto} correspondiente.
     */
    AgenteVentasDto toDTO(AgenteVentas agente);


    AgenteVentas toEntity(AgenteVentasDto agenteVentasDto);


    /**
     * Actualiza una entidad {@link AgenteVentas} existente con los valores de un DTO de edición.
     * <p>
     * Los campos nulos en el DTO serán ignorados y no afectarán los valores existentes.
     * El campo {@code imagenPerfil} se ignora siempre para evitar sobre escrituras.
     * </p>
     *
     * @param agenteVentasDto el DTO con los valores actualizados.
     * @param agenteVentas la entidad existente que será modificada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toEntity(EditarAgenteVentasDto agenteVentasDto, @MappingTarget AgenteVentas agenteVentas);


}
