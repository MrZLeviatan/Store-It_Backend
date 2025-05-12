package co.edu.uniquindio.service.users;


import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.CrearAgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.EditarAgenteVentasDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que define las operaciones principales para la gestión del agente de ventas.
 * <p>
 * Incluye métodos para registrar, editar, eliminar lógicamente, modificar los datos laborales,
 * obtener agentes de ventas por ID o email, y listar agente de ventas con filtros y paginación
 * <p>
 * Cada operación lanza excepciones específicas para validar correctamente los datos
 * y manejar errores según la lógica del negocio.
 * <p>
 *
 * @see CrearAgenteVentasDto
 * @see EditarAgenteVentasDto
 * @see EliminarClienteDto
 * @see EditarUserPersona
 */
public interface AgenteVentaServicio {


    /**
     * Registra un nuevo agente de ventas en el sistema.
     * @param agente Datos necesarios para crear un nuevo agente de ventas.
     * @throws ElementoRepetidoException     si ya existe un agente con el mismo correo o teléfono.
     * @throws ElementoIncorrectoException   si los datos proporcionados no son válidos.
     * @throws ElementoNulosException        si algún campo obligatorio está vacío o nulo.
     * @throws ElementoNoEncontradoException si no se encuentra la sede u otra entidad relacionada.
     */
    void registrarAgenteVentas(CrearAgenteVentasDto agente)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException;


    /**
     * Edita los datos personales de un agente de ventas existente.
     * @param agente DTO con los datos modificados del agente.
     * @throws ElementoNoEncontradoException si el agente no existe.
     * @throws ElementoIncorrectoException   si los nuevos datos no son válidos.
     * @throws ElementoNulosException        si algún campo obligatorio está vacío o nulo.
     */
    void editarAgenteVentas(EditarAgenteVentasDto agente)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoNulosException;



    SedeDto obtenerSedeAgente(Long id) throws ElementoNoEncontradoException;


    List<AgenteVentasDto> obtenerAgentesVentas(Long idSede);



    public List<ContratoDto> obtenerContratosPorAgenteVentas(AgenteVentasDto agenteVentasDto);



    /**
     * Elimina un agente de ventas del sistema.
     * @throws ElementoNoEncontradoException si el agente no existe.
     * @throws ElementoIncorrectoException   si los datos proporcionados no son válidos.
     */
    void eliminarAgenteVentas(Long id)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;


    /**
     * Obtiene un agente de ventas mediante su ID.
     * @param id Identificador único del agente.
     * @return DTO con los datos del agente encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra un agente con ese ID.
     */
    AgenteVentasDto obtenerAgenteVentasId(Long id)
            throws ElementoNoEncontradoException;


    /**
     * Obtiene un agente de ventas mediante su correo electrónico.
     * @param email Correo electrónico del agente.
     * @return DTO con los datos del agente encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra un agente con ese correo.
     */
    AgenteVentasDto obtenerAgenteVentasEmail(String email) throws ElementoNoEncontradoException;


    /**
     * Edita los datos del objeto embebido User del agente de ventas.
     * @param agenteUser DTO con los nuevos datos de usuario.
     * @throws ElementoNoEncontradoException si el agente no existe.
     * @throws ElementoIncorrectoException   si los datos nuevos no cumplen validaciones.
     * @throws ElementoRepetidoException     si el nuevo correo o teléfono ya existen en otro usuario.
     */
    void editarUserAgenteVentas(EditarUserPersona agenteUser)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoRepetidoException;


    /**
     * Lista los agentes de ventas filtrados por sede, fecha de contratación, tipo de contrato y estado del contrato laboral.
     * @param idSede                ID de la sede (puede ser null para listar todos).
     * @param fechaContratacion     Fecha exacta de contratación (puede ser null).
     * @param tipoContrato          Tipo de contrato (puede ser null).
     * @param estadoContratoLaboral Estado actual del contrato laboral (puede ser null).
     * @param pagina                Número de página para paginación.
     * @param size                  Cantidad de resultados por página.
     * @return Lista de agentes de ventas que cumplen con los filtros aplicados.
     */
    List<AgenteVentasDto> listarAgenteVentas(String idSede, LocalDate fechaContratacion,
                                             TipoContrato tipoContrato,
                                             EstadoContratoLaboral estadoContratoLaboral,
                                             int pagina, int size);
}



