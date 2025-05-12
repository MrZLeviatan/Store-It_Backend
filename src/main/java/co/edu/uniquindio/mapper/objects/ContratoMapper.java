package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.contrato.CrearContratoDto;
import co.edu.uniquindio.dto.objects.contrato.EditarContratoDto;
import co.edu.uniquindio.mapper.users.AgenteVentasMapper;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;


/**
 * Mapper de MapStruct encargado de transformar entre la entidad {@link Contrato} y sus diferentes DTOs,
 * facilitando así la transferencia de datos entre las capas del sistema.
 * <p>
 * Este mapper permite:
 * <ul>
 *     <li>Crear instancias de {@link Contrato} a partir de {@link CrearContratoDto}.</li>
 *     <li>Convertir entidades {@link Contrato} a {@link ContratoDto} para envío al frontend.</li>
 *     <li>Actualizar entidades existentes con datos provenientes de {@link EditarContratoDto}.</li>
 * </ul>
 * <p>
 * Utiliza mappers auxiliares para mapear relaciones complejas con:
 * {@link ClienteMapper}, {@link AgenteVentasMapper}, {@link EspacioMapper} y {@link DetalleFacturaMapper}.
 * <p>
 * Este mapper es gestionado por Spring y se utiliza principalmente en el servicio de contratos.
 *
 * @author MrZ.Leviatan
 */
@Mapper(componentModel = "spring", uses = {ClienteMapper.class,
        AgenteVentasMapper.class, EspacioMapper.class,
        DetalleFacturaMapper.class,
        QuantityAreaConverter.class})
public interface ContratoMapper {

    /**
     * Convierte un DTO de creación de contrato en una entidad {@link Contrato}.
     * <p>
     * Se establece la fecha de inicio automáticamente con la fecha actual, y la lista de detalles de factura
     * se inicializa vacía.
     *
     * @param contrato DTO con los datos necesarios para crear un contrato.
     * @return entidad {@link Contrato} lista para ser persistida.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firmaCliente", ignore = true)
    @Mapping(target = "firmaAgenteVentas", ignore = true)
    @Mapping(target = "fechaFirmaCliente", ignore = true)
    @Mapping(target = "fechaInicio" , expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "estadoContrato", constant = "PENDIENTE_VERIFICACION")
    @Mapping(target = "detallesFactura",expression = "java(new java.util.ArrayList<>())")
    Contrato toEntity(CrearContratoDto contrato);

    /**
     * Convierte una entidad {@link Contrato} en su representación {@link ContratoDto}.
     *
     * @param contrato entidad a convertir.
     * @return DTO correspondiente.
     */
    ContratoDto toDTO(Contrato contrato);


    /**
     * Actualiza una entidad {@link Contrato} con los datos del DTO de edición.
     * <p>
     * Se ignoran los valores nulos, y también se ignora el campo ID para evitar su sobreescritura.
     *
     * @param contratoDto DTO con los nuevos datos del contrato.
     * @param contrato entidad original que será actualizada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void toEntity(EditarContratoDto contratoDto, @MappingTarget Contrato contrato);

}