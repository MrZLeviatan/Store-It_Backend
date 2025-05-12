package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.TrasladoSedeAgenteDto;
import co.edu.uniquindio.dto.users.common.EditarDatosLaboralesDto;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.dto.users.personalBodega.CambiarTipoCargoDto;
import co.edu.uniquindio.dto.users.personalBodega.TrasladoPersonalBodegaDto;
import co.edu.uniquindio.dto.users.recursosHumanos.CrearRHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.EditarRRHHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.ReactivarCuentaDto;
import co.edu.uniquindio.dto.users.recursosHumanos.RecursosHumanosDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define los servicios relacionados con la gestión de recursos humanos dentro del sistema Store-It.
 * <p>
 * Esta interfaz proporciona operaciones como:
 * <ul>
 *     <li>Registro de nuevos recursos humanos con validaciones específicas.</li>
 *     <li>Edición de datos laborales y personales de los recursos humanos.</li>
 *     <li>Eliminación de recursos humanos dentro del sistema.</li>
 *     <li>Obtención de información sobre recursos humanos por ID o correo electrónico.</li>
 *     <li>Reactivación de cuentas laborales de recursos humanos.</li>
 *     <li>Cambio de tipo de cargo de personal de bodega y recursos humanos.</li>
 *     <li>Traslado de personal entre bodegas o sedes.</li>
 * </ul>
 * <p>
 * Los métodos de esta interfaz lanzan excepciones específicas en caso de error o si los elementos solicitados no existen, como:
 * <ul>
 *     <li>{@link ElementoNoEncontradoException} si no se encuentra el recurso solicitado.</li>
 *     <li>{@link ElementoRepetidoException} si un recurso humano ya está registrado.</li>
 *     <li>{@link ElementoIncorrectoException} si los datos proporcionados son incorrectos.</li>
 *     <li>{@link ElementoNulosException} si algún campo obligatorio está vacío o nulo.</li>
 * </ul>
 *
 * @see CrearRHDto
 * @see EditarRRHHDto
 * @see EliminarClienteDto
 * @see RecursosHumanosDto
 * @see ReactivarCuentaDto
 * @see CambiarTipoCargoDto
 * @see TrasladoPersonalBodegaDto
 * @see TrasladoSedeAgenteDto
 *
 * @author MrZ.Leviatan
 */
public interface RecursosHumanosServicio {

    /**
     * Registra un nuevo recurso humano en el sistema.
     *
     * @param recursosHumanos DTO con la información del recurso humano a registrar.
     * @throws ElementoRepetidoException si ya existe un recurso humano con los mismos datos.
     * @throws ElementoIncorrectoException si los datos proporcionados son incorrectos.
     * @throws ElementoNulosException si algunos campos obligatorios están vacíos.
     * @throws ElementoNoEncontradoException si el recurso humano no se encuentra.
     */
    void registrarRecursosHumanos(CrearRHDto recursosHumanos) throws ElementoRepetidoException, ElementoIncorrectoException, ElementoNulosException, ElementoNoEncontradoException;

    /**
     * Edita los datos de un recurso humano existente.
     *
     * @param recursosHumanos DTO con los nuevos datos del recurso humano.
     * @throws ElementoNoEncontradoException si el recurso humano no existe.
     * @throws ElementoIncorrectoException si los datos proporcionados son incorrectos.
     * @throws ElementoNulosException si algunos campos obligatorios están vacíos.
     */
    void editarRecursosHumanos(EditarRRHHDto recursosHumanos) throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNulosException;


    /**
     * Elimina de manera lógica un recurso humano del sistema.
     *
     * @param agente DTO que contiene la información del recurso humano a eliminar.
     * @throws ElementoNoEncontradoException si el recurso humano no existe.
     * @throws ElementoIncorrectoException si los datos proporcionados son incorrectos.
     */
    void eliminarRecursosHumanos(EliminarClienteDto agente) throws ElementoNoEncontradoException, ElementoIncorrectoException;



    SedeDto obtenerSedeRRHH(Long id) throws ElementoNoEncontradoException;

    /**
     * Obtiene un recurso humano por su ID.
     *
     * @param id El ID del recurso humano a obtener.
     * @return DTO con la información del recurso humano.
     * @throws ElementoNoEncontradoException si el recurso humano no existe.
     */
    RecursosHumanosDto obtenerRecursosId(Long id) throws ElementoNoEncontradoException;

    /**
     * Obtiene un recurso humano por su correo electrónico.
     *
     * @param email El correo electrónico del recurso humano a obtener.
     * @return DTO con la información del recurso humano.
     * @throws ElementoNoEncontradoException si el recurso humano no existe.
     */
    RecursosHumanosDto obtenerRecursosEmail(String email) throws ElementoNoEncontradoException;

    /**
     * Reactiva la cuenta laboral de un recurso humano.
     *
     * @param reactivarCuentaDto DTO con la información necesaria para reactivar la cuenta.
     * @throws ElementoNoEncontradoException si el recurso humano no existe.
     * @throws ElementoIncorrectoException si los datos proporcionados son incorrectos.
     */
    void reactivarCuentaLaboral(ReactivarCuentaDto reactivarCuentaDto) throws ElementoNoEncontradoException, ElementoIncorrectoException;

    /**
     * Edita los datos laborales de un agente de ventas.
     *
     * @param agenteDatosLaboral DTO con los nuevos datos laborales del agente.
     * @throws ElementoNoEncontradoException si el agente no existe.
     */
    void editarDatosLaboralesAgente(EditarDatosLaboralesDto agenteDatosLaboral) throws ElementoNoEncontradoException;

    /**
     * Edita los datos laborales del personal de bodega.
     *
     * @param personalBodega DTO con los nuevos datos laborales del personal de bodega.
     * @throws ElementoNoEncontradoException si el personal de bodega no existe.
     */
    void editarDatosLaboralesPersonal(EditarDatosLaboralesDto personalBodega) throws ElementoNoEncontradoException;

    /**
     * Edita los datos laborales de los recursos humanos.
     *
     * @param recursosHumanos DTO con los nuevos datos laborales de los recursos humanos.
     * @throws ElementoNoEncontradoException si los recursos humanos no existen.
     */
    void editarDatosLaboralesRecursosHumanos(EditarDatosLaboralesDto recursosHumanos) throws ElementoNoEncontradoException;

    /**
     * Cambia el tipo de cargo de un recurso humano o personal de bodega.
     *
     * @param cambiarTipoCargoDto DTO con el nuevo tipo de cargo.
     * @throws ElementoNoEncontradoException si el recurso humano o personal de bodega no existe.
     */
    void cambiarTipoCargo(CambiarTipoCargoDto cambiarTipoCargoDto) throws ElementoNoEncontradoException;

    /**
     * Realiza el traslado de un personal de bodega a una nueva bodega.
     *
     * @param trasladoPersonalBodegaDto DTO que contiene la información del traslado.
     * @throws ElementoNoEncontradoException si el personal de bodega o la bodega no existen.
     */
    void trasladoBodegaPersonal(TrasladoPersonalBodegaDto trasladoPersonalBodegaDto) throws ElementoNoEncontradoException;

    /**
     * Realiza el traslado de un agente de ventas a una nueva sede.
     *
     * @param agente DTO que contiene el ID del agente y el ID de la nueva sede.
     * @throws ElementoNoEncontradoException si el agente o la sede no existen.
     */
    void trasladoSedeAgente(TrasladoSedeAgenteDto agente) throws ElementoNoEncontradoException;


    List<RecursosHumanosDto> listarRecursosHumanos(String idSede, LocalDate fechaContratacion,
                                                   TipoContrato tipoContrato,
                                                   EstadoContratoLaboral estadoContratoLaboral,
                                                   int pagina, int size);



}
