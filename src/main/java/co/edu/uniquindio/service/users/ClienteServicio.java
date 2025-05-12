package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.EditarClienteDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.TipoCliente;

import java.util.List;
import co.edu.uniquindio.model.users.Cliente;

/**
 * Servicio que define las operaciones principales para la gestión de {@link Cliente}
 * <p>
 * Incluye métodos para registrar, editar, eliminar lógicamente, activar cuentas,
 * obtener clientes por ID o email, y listar clientes con filtros y paginación.
 * <p>
 * Cada operación lanza excepciones específicas para validar correctamente los datos
 * y manejar errores según la lógica del negocio.
 * </p>
 * @see CrearClienteDto
 * @see EditarClienteDto
 * @see EliminarClienteDto
 * @see ClienteDto
 */
public interface ClienteServicio {


    /**
     * Registra una nueva cuenta de cliente.
     * @param cuenta Objeto de transferencia de datos (DTO) con la información del cliente a registrar.
     * @throws ElementoRepetidoException Si el email del cliente ya está registrado.
     * @throws ElementoIncorrectoException Si los datos proporcionados no son válidos.
     * @throws ElementoNulosException Si hay campos requeridos que no fueron proporcionados.
     * @throws ElementoNoEncontradoException Si no se puede completar por datos dependientes inexistentes.
     */
    void registrarCliente(CrearClienteDto cuenta) throws ElementoRepetidoException,
            ElementoIncorrectoException, ElementoNulosException,
            ElementoNoEncontradoException;


    /**
     * Actualiza una cuenta de cliente existente.
     * @param cuenta Objeto de transferencia de datos (DTO) con la información actualizada del cliente.
     * @throws ElementoNoEncontradoException Si el cliente no existe.
     * @throws ElementoIncorrectoException Si los datos proporcionados no son válidos.
     * @throws ElementoNulosException Si hay campos requeridos que no fueron proporcionados.
     */
    void editarCliente(EditarClienteDto cuenta)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoNulosException ;


    /**
     * Actualiza la información del usuario (correo electrónico y contraseña) de un cliente.
     * @param cuenta Objeto DTO que contiene los datos necesarios para la actualización
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el ID proporcionado.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada es incorrecta o si la nueva contraseña es igual a la actual.
     * @throws ElementoRepetidoException Si el correo electrónico proporcionado ya está registrado en otro cliente.
     */
    void editarUserCliente(EditarUserPersona cuenta)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoRepetidoException;


    /**
     * Elimina lógicamente un cliente del sistema.
     * @param dto DTO que contiene el identificador del cliente a eliminar.
     * @throws ElementoNoEncontradoException Si el cliente no existe.
     * @throws ElementoIncorrectoException Si el identificador no es válido o el cliente no puede ser eliminado.
     */
    void eliminarCliente(EliminarClienteDto dto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;


    /**
     * Obtiene la información de un cliente según su ID.
     * @param id Identificador único del cliente.
     * @return ClienteDto con la información correspondiente.
     * @throws ElementoNoEncontradoException Si el cliente no se encuentra.
     */
    ClienteDto obtenerPorId(Long id) throws ElementoNoEncontradoException;


    /**
     * Obtiene la información de un cliente según su correo electrónico.
     * @param email Correo electrónico del cliente.
     * @return ClienteDto con los datos del cliente asociado al email.
     * @throws ElementoNoEncontradoException Si el cliente no se encuentra o hay un error en la consulta.
     */
    ClienteDto obtenerPorEmail(String email) 
            throws ElementoNoEncontradoException;


    /**
     * Permite restablecer la cuenta de un cliente utilizando su correo electrónico
     * registrado en el sistema.
     * @param email Correo electrónico asociado a la cuenta del cliente que debe
     *              ser restablecida.
     * @throws ElementoNoEncontradoException Si no existe ninguna cuenta registrada
     *                                        con el correo electrónico proporcionado.
     * @throws ElementoIncorrectoException   Si el correo proporcionado no es válido
     *                                        o no cumple con los requisitos del sistema.
     */
    void restablecerCuenta(String email)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;


    /**
     * Listar clientes aplicando filtros opcionales por país, ciudad y tipo de cliente, con paginación.
     * @param pais         (Opcional) País del cliente.
     * @param ciudad       (Opcional) Ciudad del cliente.
     * @param tipoCliente  (Opcional) Tipo de usuario asociado.
     * @param pagina       Número de página (inicia en 0).
     * @param size         Cantidad de elementos por página.
     * @return Lista de objetos ClienteDto según los filtros aplicados.
     */
    List<ClienteDto> listarClientes(String pais, String ciudad, TipoCliente tipoCliente,
                                    int pagina, int size);

}

