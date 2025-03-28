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

@Service // 🔹 Indica que esta clase es un servicio de Spring y permite la inyección de dependencias automáticamente.
@RequiredArgsConstructor
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepo clienteRepo;
    private final ClienteMapper clienteMapper;


    @Override
    public void crear(CrearClienteDTO cuenta) throws Exception {

        // 🔹 Verifica si el email ya está registrado antes de crear el cliente.
        if (existeEmail(cuenta.email())){
            throw new ElementoRepetidoException("El email ya existe");
        }

        // 🔹 Verifica si la cedula ya está registrado antes de crear el cliente.
        if (existeCeula(cuenta.cedula())){
            throw new ElementoRepetidoException("El cedula ya existe");
        }

        // 🔹 Convierte el DTO en una entidad y lo guarda en la base de datos.
        Cliente cliente = clienteMapper.toEntity(cuenta);
        clienteRepo.save(cliente);
    }

    // 🔹 Método privado para verificar si un email ya existe en la base de datos.
    private boolean existeEmail(String email) {
        return clienteRepo.findByEmail(email).isPresent();
    }

    private boolean existeCeula(String cedula){
        return clienteRepo.findByCedula(cedula).isPresent();
    }

    @Override
    public void eliminar(String id) throws ElementoNoEncontradoException {
        // 🔹 Verifica si el cliente con el ID especificado existe.
        if (!clienteRepo.existsById(id)) {
            throw new ElementoNoEncontradoException("El cliente con ID " + id + " no existe.");
        }
        // 🔹 Elimina el cliente de la base de datos.
        clienteRepo.deleteById(id);
    }


    // 🔹 Método privado para buscar un cliente por ID
    private Cliente buscarClientePorId(String cedula) throws ElementoNoEncontradoException {
        return clienteRepo.findByCedula(cedula)
                .orElseThrow(() -> new ElementoNoEncontradoException("El cliente con ID " + cedula + " no existe."));
    }

    @Override
    public void editar(EditarClienteDTO cuenta) throws Exception {
        // 🔹 Buscar cliente en la base de datos
        Cliente cliente = buscarClientePorId(cuenta.cedula());

        // 🔹 Actualizar datos del cliente usando el mapper
        clienteMapper.toEntity(cuenta, cliente);

        // 🔹 Guardar cambios
        clienteRepo.save(cliente);
    }


    @Override
    public ClienteDto obtener(String id) throws Exception {
        // 🔹 Buscar el cliente por ID
        Cliente cliente = buscarClientePorId(id);

        // 🔹 Convertir la entidad a DTO usando el mapper
        return clienteMapper.toDTO(cliente);
    }

    @Override
    public List<ClienteDto> listarTodos(int pagina) {
        // 🔹 Validar que la página no sea menor a 0
        if (pagina < 0) throw new RuntimeException("La página no puede ser menor a 0");

        // 🔹 Consultar con paginación de 5 elementos por página
        List<Cliente> clientes = clienteRepo.findAll(PageRequest.of(pagina, 5)).getContent();

        // 🔹 Convertir la lista de clientes a DTOs
        return clientes.stream()
                .map(clienteMapper::toDTO)
                .toList();

    }
}
