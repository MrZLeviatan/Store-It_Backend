package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.dto.users.personalBodega.*;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.users.enums.TipoCargo;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que define las operaciones principales para la gestión del personal de bodega.
 * <p>
 * Incluye métodos para registrar, editar, eliminar lógicamente, modificar los datos laborales,
 * obtener personal de bodega por ID o email, y listar personal con filtros y paginación.
 * </p>
 * <p>
 * Cada operación lanza excepciones específicas para validar correctamente los datos
 * y manejar errores según la lógica del negocio.
 * </p>
 *
 * @see CrearPersonalBodegaDto
 * @see EditarPersonalBodegaDto
 * @see EliminarClienteDto
 * @see EditarUserPersona
 */
public interface PersonalBodegaServicio {

    /**
     * Registra un nuevo personal de bodegas en el sistema.
     *
     * @param personal necesarios para crear un nuevo personal de ventas.
     * @throws ElementoRepetidoException     si ya existe un personal con el mismo correo o teléfono.
     * @throws ElementoIncorrectoException   si los datos proporcionados no son válidos.
     * @throws ElementoNulosException        si algún campo obligatorio está vacío o nulo.
     * @throws ElementoNoEncontradoException si no se encuentra la sede u otra entidad relacionada.
     */
    void registrarPersonal(CrearPersonalBodegaDto personal)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException;


    /**
     * Edita los datos personales de un personal de bodegas existente.
     *
     * @param personalEditado DTO con los datos modificados del personal de bodegas.
     * @throws ElementoNoEncontradoException si el personal no existe.
     * @throws ElementoIncorrectoException   si los nuevos datos no son válidos.
     * @throws ElementoNulosException        si algún campo obligatorio está vacío o nulo.
     */
    void editarPersonal(EditarPersonalBodegaDto personalEditado)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoNulosException;


    BodegaDto obtenerBodegaPersonal(Long id) throws ElementoNoEncontradoException;

    /**
     * Elimina un personal de bodegas del sistema.
     *
     * @param personal DTO que contiene la información del personal a eliminar.
     * @throws ElementoNoEncontradoException si el personal no existe.
     * @throws ElementoIncorrectoException   si los datos proporcionados no son válidos.
     */
    void eliminarPersonal(EliminarClienteDto personal) throws
            ElementoNoEncontradoException, ElementoIncorrectoException;


    /**
     * Obtiene un personal de bodega mediante su ID.
     *
     * @param id Identificador único del personal.
     * @return DTO con los datos del personal encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra un personal con ese ID.
     */
    PersonalBodegaDto obtenerPersonalId(Long id)
            throws ElementoNoEncontradoException;


    /**
     * Obtiene un personal de bodegas mediante su correo electrónico.
     *
     * @param email Correo electrónico del personal.
     * @return DTO con los datos del personal encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra un personal con ese correo.
     */
    PersonalBodegaDto obtenerPersonalEmail(String email)
            throws ElementoNoEncontradoException;


    /**
     * Edita los datos del objeto embebido User del personal de bodegas.
     *
     * @param personalBodega DTO con los nuevos datos de usuario.
     * @throws ElementoNoEncontradoException si el personal no existe.
     * @throws ElementoIncorrectoException   si los datos nuevos no cumplen validaciones.
     * @throws ElementoRepetidoException     si el nuevo correo o teléfono ya existen en otro usuario.
     */
    void editarUserPersonalBodega(EditarUserPersona personalBodega)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoRepetidoException;


    /**
     * Lista el personal de bodegas filtrados por bodega, fecha de contratación, tipo de contrato y estado del contrato laboral.
     *
     * @param idBodega              ID de la bodega (puede ser null para listar todos).
     * @param fechaContratacion     Fecha exacta de contratación (puede ser null).
     * @param tipoContrato          Tipo de contrato (puede ser null).
     * @param estadoContrato        Estado actual del contrato laboral (puede ser null).
     * @param pagina                Número de página para paginación.
     * @param size                  Cantidad de resultados por página.
     * @return Lista de agentes de ventas que cumplen con los filtros aplicados.
     */
    List<PersonalBodegaDto> listarPersonalBodega (String idBodega, LocalDate fechaContratacion,
                                                EstadoContratoLaboral estadoContrato,
                                                TipoContrato tipoContrato,
                                                TipoCargo tipoCargo,
                                                int pagina, int size );

}
