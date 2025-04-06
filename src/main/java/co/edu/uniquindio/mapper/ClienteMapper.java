package co.edu.uniquindio.mapper;


import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ClienteMapper {

    /**
     * Convierte un DTO de creación de cliente a una entidad Cliente.
     *
     * @param clienteDTO DTO con la información del cliente a crear.
     * @return Objeto Cliente con los datos asignados.
     */
    @Mapping(target = "rol", constant = "CLIENTE") // 🔹 Se asigna el rol "CLIENTE" por defecto
    Cliente toEntity(CrearClienteDTO clienteDTO);

    /**
     * Convierte una entidad Cliente a un DTO de Cliente.
     *
     * @param cliente Objeto Cliente a convertir.
     * @return DTO con la información del cliente.
     */
    ClienteDto toDTO(Cliente cliente);

    /**
     * Actualiza una entidad Cliente con los datos de un DTO de edición.
     *
     * @param editarClienteDTO DTO con la información a actualizar.
     * @param cliente Objeto Cliente que será modificado.
     */
    void toEntity(EditarClienteDTO editarClienteDTO, @MappingTarget Cliente cliente);
}
