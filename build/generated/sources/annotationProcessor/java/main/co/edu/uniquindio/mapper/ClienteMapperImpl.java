package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.dto.Espacio.EspacioDto;
import co.edu.uniquindio.dto.Producto.ProductoDto;
import co.edu.uniquindio.model.Cliente;
import co.edu.uniquindio.model.enums.Rol;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-27T18:56:18-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toEntity(CrearClienteDTO clienteDTO) {
        if ( clienteDTO == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setPassword( clienteDTO.password() );

        cliente.setRol( Rol.CLIENTE );

        return cliente;
    }

    @Override
    public ClienteDto toDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        String id = null;
        String password = null;
        Rol rol = null;

        id = cliente.getId();
        password = cliente.getPassword();
        rol = cliente.getRol();

        String nombre = null;
        String email = null;
        List<ProductoDto> listProducto = null;
        List<EspacioDto> listEspacios = null;

        ClienteDto clienteDto = new ClienteDto( id, nombre, email, password, rol, listProducto, listEspacios );

        return clienteDto;
    }

    @Override
    public void toEntity(EditarClienteDTO editarClienteDTO, Cliente cliente) {
        if ( editarClienteDTO == null ) {
            return;
        }

        cliente.setId( editarClienteDTO.id() );
    }
}
