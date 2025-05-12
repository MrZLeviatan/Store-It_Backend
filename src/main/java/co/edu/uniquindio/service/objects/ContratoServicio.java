package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.contrato.CrearContratoDto;
import co.edu.uniquindio.dto.objects.contrato.EditarContratoDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio que define las operaciones relacionadas con la gestión de contratos
 * dentro del sistema Store-It.
 *
 * <p>
 * Esta interfaz declara los métodos necesarios para crear, firmar, editar, cancelar y consultar contratos,
 * así como obtener los contratos asociados a un cliente o agente de ventas.
 */
public interface ContratoServicio {


    /**
     * Crea un nuevo contrato con la información proporcionada.
     *
     * @param contratoDto DTO con los datos necesarios para crear el contrato.
     * @throws ElementoNoEncontradoException si el cliente, agente o espacio no existen.
     */
    void crearContrato(CrearContratoDto contratoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;

    /**
     * Firma un contrato por parte del cliente, adjuntando una imagen de la firma.
     *
     * @param idContrato ID del contrato a firmar.
     * @param firmaImagen Imagen con la firma del cliente.
     * @throws ElementoNoEncontradoException si el contrato no existe.
     */
    void firmarContratoPorCliente(Long idContrato, MultipartFile firmaImagen) throws ElementoNoEncontradoException;

    /**
     * Firma un contrato por parte del agente de ventas, adjuntando una imagen de la firma.
     *
     * @param idContrato ID del contrato a firmar.
     * @param firmaImagen Imagen con la firma del agente.
     * @throws ElementoNoEncontradoException si el contrato no existe.
     */
    void firmarContratoPorAgente(Long idContrato, MultipartFile firmaImagen) throws ElementoNoEncontradoException;

    /**
     * Edita los datos de un contrato existente.
     *
     * @param contratoDto DTO con los datos editados del contrato.
     * @throws ElementoNoEncontradoException si el contrato no existe.
     */
    void editarContrato(EditarContratoDto contratoDto) throws ElementoNoEncontradoException;

    /**
     * Obtiene un contrato a partir de su identificador.
     *
     * @param id ID del contrato.
     * @return DTO con los datos del contrato.
     * @throws ElementoNoEncontradoException si el contrato no existe.
     */
    ContratoDto obtenerContratoId(Long id) throws ElementoNoEncontradoException;

    /**
     * Cancela un contrato existente.
     *
     * @param id ID del contrato a cancelar.
     * @throws ElementoNoEncontradoException si el contrato no existe.
     * @throws ElementoIncorrectoException si el contrato no se puede cancelar por su estado.
     */
    void cancelarContrato (Long id) throws ElementoNoEncontradoException, ElementoIncorrectoException;


    /**
     * Obtiene una lista paginada de contratos asociados a un cliente,
     * filtrados por fecha de inicio y estado del contrato.
     *
     * @param clienteId ID del cliente.
     * @param fechaInicio Fecha de inicio del contrato (opcional).
     * @param estadoContrato Estado del contrato (opcional).
     * @param pagina Número de página para la paginación.
     * @param size Tamaño de la página.
     * @return Lista de contratos del cliente.
     */
    List<ContratoDto> obtenerContratoCliente(Long clienteId,
                                             LocalDate fechaInicio,
                                             EstadoContrato estadoContrato,
                                             int pagina, int size);


    /**
     * Obtiene una lista paginada de contratos gestionados por un agente de ventas,
     * filtrados por fecha de inicio y estado del contrato.
     *
     * @param idAgenteVentas ID del agente de ventas.
     * @param fechaInicio Fecha de inicio del contrato (opcional).
     * @param estadoContrato Estado del contrato (opcional).
     * @param pagina Número de página para la paginación.
     * @param size Tamaño de la página.
     * @return Lista de contratos del agente de ventas.
     */
    List<ContratoDto> obtenerContratoAgenteVentas(Long idAgenteVentas,
                                                  LocalDate fechaInicio,
                                                  EstadoContrato estadoContrato,
                                                  int pagina, int size);


    // Método para obtener todos los contratos asociados a una lista de agentes de ventas


}
