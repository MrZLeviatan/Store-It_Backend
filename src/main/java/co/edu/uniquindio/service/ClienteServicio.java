package co.edu.uniquindio.service;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;

import java.util.List;

public interface ClienteServicio {

    /**
     * Crea una nueva cuenta de cliente.
     *
     * @param cuenta Objeto de transferencia de datos (DTO) con la información del cliente.
     * @throws Exception Sí ocurre un error durante la creación.
     */
    void crear(CrearClienteDTO cuenta) throws Exception;

    /**
     * Elimina un cliente por su ID.
     *
     * @param id Identificador único del cliente.
     * @throws Exception Si el cliente no existe o no puede ser eliminado.
     */
    void eliminar(String id) throws Exception;

    /**
     * Actualiza una cuenta de cliente existente.
     *
     * @param cuenta Objeto de transferencia de datos (DTO) con la información actualizada del cliente.
     * @throws Exception Si la actualización falla o el cliente no existe.
     */
    void editar(EditarClienteDTO cuenta) throws Exception;

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id Identificador único del cliente.
     * @return ClienteDto con la información del cliente.
     * @throws Exception Si el cliente no existe.
     */
    ClienteDto obtener(String id) throws Exception;

    /**
     * Obtiene una lista paginada de todos los clientes filtrados por nombre.
     *
     * @param nombre Filtro de nombre para los clientes.
     * @param pagina Número de página para la paginación.
     * @return Lista de ClienteDto con los detalles de los clientes.
     */
    List<ClienteDto> listarTodos(String nombre, int pagina);

}
