package co.edu.uniquindio.service.impl;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.mapper.ClienteMapper;
import co.edu.uniquindio.model.Cliente;
import co.edu.uniquindio.repository.ClienteRepo;
import co.edu.uniquindio.service.ClienteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indica que esta clase es un componente de servicio en Spring y puede ser inyectado donde se necesite
@RequiredArgsConstructor // Genera automáticamente un constructor con todos los atributos finales (clienteRepo, clienteMapper)
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepo clienteRepo;
    private final ClienteMapper clienteMapper;

    @Override
    public void crear(CrearClienteDTO cuenta) throws Exception {

        // Verifica si el email ya está registrado antes de crear el cliente
        if (existeEmail(cuenta.email())) {
            throw new ElementoRepetidoException("El email ya existe");
        }

        // Verifica si la cédula ya está registrada antes de crear el cliente
        if (existeCeula(cuenta.cedula())) {
            throw new ElementoRepetidoException("El cedula ya existe");
        }

        // Convierte el DTO en una entidad Cliente y la guarda en la base de datos
        Cliente cliente = clienteMapper.toEntity(cuenta);
        clienteRepo.save(cliente);
    }

    // Método privado para verificar si un email ya existe en la base de datos
    private boolean existeEmail(String email) {
        return clienteRepo.findByEmail(email).isPresent();
    }

    // Método privado para verificar si una cédula ya existe en la base de datos
    private boolean existeCeula(String cedula) {
        return clienteRepo.findByCedula(cedula).isPresent();
    }

    @Override
    public void eliminar(String id) throws ElementoNoEncontradoException {
        // Verifica si el cliente con el ID especificado existe en la base de datos
        if (!clienteRepo.existsById(id)) {
            throw new ElementoNoEncontradoException("El cliente con ID " + id + " no existe.");
        }

        // Elimina el cliente de la base de datos
        clienteRepo.deleteById(id);
    }

    // Método privado para buscar un cliente por cédula
    private Cliente buscarClientePorId(String cedula) throws ElementoNoEncontradoException {
        return clienteRepo.findByCedula(cedula)
                .orElseThrow(() -> new ElementoNoEncontradoException("El cliente con ID " + cedula + " no existe."));
    }

    @Override
    public void editar(EditarClienteDTO cuenta) throws Exception {
        // Busca al cliente en la base de datos por su cédula
        Cliente cliente = buscarClientePorId(cuenta.cedula());

        // Actualiza los datos del cliente con la información del DTO
        clienteMapper.toEntity(cuenta, cliente);

        // Guarda los cambios actualizados en la base de datos
        clienteRepo.save(cliente);
    }

    @Override
    public ClienteDto obtener(String id) throws Exception {
        // Busca al cliente por cédula
        Cliente cliente = buscarClientePorId(id);

        // Convierte la entidad Cliente en un DTO para retornar
        return clienteMapper.toDTO(cliente);
    }

    @Override
    public List<ClienteDto> listarTodos(int pagina) {
        // Valida que el número de página no sea negativo
        if (pagina < 0) throw new RuntimeException("La página no puede ser menor a 0");

        // Consulta la lista paginada de clientes (5 elementos por página)
        List<Cliente> clientes = clienteRepo.findAll(PageRequest.of(pagina, 5)).getContent();

        // Convierte la lista de entidades Cliente a una lista de DTOs
        return clientes.stream()
                .map(clienteMapper::toDTO)
                .toList();
    }
}
