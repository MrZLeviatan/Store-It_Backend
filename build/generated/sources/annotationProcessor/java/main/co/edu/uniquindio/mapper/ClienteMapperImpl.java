package co.edu.uniquindio.mapper;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.model.Cliente;
import co.edu.uniquindio.model.enums.Rol;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
<<<<<<< HEAD
    date = "2025-03-30T15:34:29-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.4 (Oracle Corporation)"
=======
    date = "2025-04-04T20:17:16-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 21.0.3 (Amazon.com Inc.)"
>>>>>>> Registrar_Clientes
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toEntity(CrearClienteDTO clienteDTO) {
        if ( clienteDTO == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setCedula( clienteDTO.cedula() );
        cliente.setNombre( clienteDTO.nombre() );
        cliente.setEmail( clienteDTO.email() );
        cliente.setPassword( clienteDTO.password() );

        cliente.setRol( Rol.CLIENTE );

        return cliente;
    }

    @Override
    public ClienteDto toDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        String cedula = null;
        String nombre = null;
        String email = null;
        String password = null;
        Rol rol = null;

        cedula = cliente.getCedula();
        nombre = cliente.getNombre();
        email = cliente.getEmail();
        password = cliente.getPassword();
        rol = cliente.getRol();

        ClienteDto clienteDto = new ClienteDto( cedula, nombre, email, password, rol );

        return clienteDto;
    }

    @Override
    public void toEntity(EditarClienteDTO editarClienteDTO, Cliente cliente) {
        if ( editarClienteDTO == null ) {
            return;
        }

        cliente.setCedula( editarClienteDTO.cedula() );
        cliente.setNombre( editarClienteDTO.nombre() );
    }
}
