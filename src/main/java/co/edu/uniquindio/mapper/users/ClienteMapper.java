package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.EditarClienteDto;
import co.edu.uniquindio.mapper.objects.FacturaMapper;
import co.edu.uniquindio.mapper.objects.ProductoMapper;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.utils.QuantityAreaConverter;
import org.mapstruct.*;


/**
 * Mapper encargado de convertir entre entidades {@link Cliente} y sus respectivos DTO,
 * utilizando {@code  MapStruct} para facilitar la transformación de datos entre capas.
 *
 * <p>Este componente está integrado con Spring mediante {@code componentModel = "spring"} y
 * utiliza los mappers auxiliares {@link FacturaMapper} y {@link ProductoMapper} para mapear
 * las relaciones anidadas de facturas y productos.</p>
 *
 * <p>Funciones principales:</p>
 * <ul>
 *   <li>Convertir un {@link CrearClienteDto} a una entidad {@link Cliente}.</li>
 *   <li>Convertir una entidad {@link Cliente} a un {@link ClienteDto}.</li>
 *   <li>Actualizar una entidad {@link Cliente} a partir de un {@link EditarClienteDto}.</li>
 * </ul>
 */
@Mapper(componentModel = "spring", uses =
        {FacturaMapper.class , ProductoMapper.class,
                QuantityAreaConverter.class})
public interface ClienteMapper {


    /**
     * Convierte un DTO de {@link CrearClienteDto} a una entidad {@link Cliente}
     *
     * @param clienteDTO DTO con la información del cliente a crear.
     * @return Objeto Cliente con los datos asignados.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "user.codigoRestablecimiento", ignore = true)
    @Mapping(target = "contratos", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "facturas", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "productos", expression = "java(new java.util.ArrayList<>())")
    Cliente toEntity(CrearClienteDto clienteDTO);


    /**
     * Convierte una entidad {@link Cliente} a un DTO de {@link ClienteDto}
     *
     * @param cliente Objeto Cliente a convertir.
     * @return DTO con la información del cliente.
     */
     ClienteDto toDTO(Cliente cliente);


    /**
     * Actualiza una entidad {@link Cliente} con los datos de un DTO de {@link EditarClienteDto}
     *
     * @param editarClienteDTO DTO con la información a actualizar.
     * @param cliente Objeto Cliente que será modificado.
     **/
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "imagenPerfil", ignore = true)
    @Mapping(target = "id", ignore = true)
    void toEntity(EditarClienteDto editarClienteDTO, @MappingTarget Cliente cliente);

}