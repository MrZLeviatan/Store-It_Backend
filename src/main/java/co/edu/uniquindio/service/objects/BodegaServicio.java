package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.bodega.EditarBodegaDto;
import co.edu.uniquindio.dto.objects.bodega.CrearBodegaDto;
import co.edu.uniquindio.exception.ElementoAunEnUsoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.enums.EstadoBodega;

import java.util.List;

/**
 * Interfaz que define las operaciones del servicio de gestión de bodegas.
 * <p>
 * Esta interfaz establece el contrato para registrar, editar, eliminar lógicamente,
 * consultar y listar bodegas con filtros dinámicos y paginación.
 * </p>
 *
 * <p>
 * Las implementaciones deben manejar validaciones, lógica de negocio y mapeo entre entidades y DTOs.
 * </p>
 *
 * @author MrZ.Leviatan
 * @see CrearBodegaDto
 * @see EditarBodegaDto
 * @see BodegaDto
 * @see EstadoBodega
 */
public interface BodegaServicio {

    /**
     * Registra una nueva {@link co.edu.uniquindio.model.objects.Bodega} en el sistema.
     * <p>
     * Este método toma un DTO con los datos necesarios para la creación de la bodega,
     * mapea esos datos a la entidad {@link co.edu.uniquindio.model.objects.Bodega},
     * sube las fotos a un servicio externo (Cloudinary) y finalmente guarda la bodega en la base de datos.
     * </p>
     *
     * @param crearBodegaDto DTO con los datos necesarios para crear la bodega.
     *                       Este DTO debe incluir información como nombre, ubicación, área, fotos, etc.
     *                       Las fotos serán subidas y las URLs se guardarán en la entidad Bodega.
     */
    void registrarBodega(CrearBodegaDto crearBodegaDto);

    /**
     * Edita una {@link co.edu.uniquindio.model.objects.Bodega} que ya se encuentre en el sistema.
     * <p>
     * Este método permite modificar los datos de una bodega ya registrada. Solo se actualizan los campos no nulos del DTO.
     * Si el DTO incluye nuevas fotos, estas serán subidas y las URLs reemplazarán a las anteriores.
     * </p>
     *
     * @param editarBodegaDto DTO con los datos necesarios para la edición de la bodega.
     *                        Este DTO debe incluir el ID de la bodega que se desea editar.
     * @throws ElementoNoEncontradoException Si no encuentra una bodega con el ID proporcionado.
     *                                       En este caso, el servicio no podrá realizar la edición.
     */
    void editarBodega(EditarBodegaDto editarBodegaDto) throws ElementoNoEncontradoException;

    /**
     * Cambia el {@link EstadoBodega} a {@code INACTIVO} de una bodega ya registrada en el sistema.
     * <p>
     * Este método marca una bodega como inactiva, lo que implica que ya no estará disponible para su uso.
     * La eliminación solo es posible si la bodega no tiene espacios con contratos activos.
     * Si existen contratos activos, se lanzará una excepción {@link ElementoAunEnUsoException}.
     * </p>
     *
     * @param id ID de la bodega registrada que se desea eliminar.
     *           Si la bodega tiene contratos activos, la operación no se llevará a cabo.
     * @throws ElementoNoEncontradoException Si no encuentra una bodega con el ID proporcionado.
     *                                       Si el ID no corresponde a una bodega válida en la base de datos.
     * @throws ElementoAunEnUsoException Si la bodega tiene contratos activos asociados.
     *                                   En este caso, no se podrá eliminar la bodega hasta que se resuelvan esos contratos.
     */
    void eliminarBodega(Long id) throws ElementoNoEncontradoException, ElementoAunEnUsoException;

    /**
     * Obtiene una {@link BodegaDto} mediante un ID.
     * <p>
     * Este método busca una bodega en la base de datos por su ID y devuelve los datos de la bodega en formato DTO,
     * que incluye solo los campos necesarios para ser enviados al cliente. Si la bodega no existe, se lanza una excepción.
     * </p>
     *
     * @param id ID de la bodega registrada.
     *           Este ID debe ser válido y corresponde a una bodega existente en el sistema.
     * @return Retorna una {@link BodegaDto} con la información de la bodega encontrada.
     *         Este DTO contiene los datos relevantes de la bodega.
     * @throws ElementoNoEncontradoException Si no encuentra una bodega con el ID proporcionado.
     *                                       En este caso, se lanza una excepción indicando que la bodega no existe.
     */
    BodegaDto obtenerBodegaId(Long id) throws ElementoNoEncontradoException;


    /**
     * Lista las bodegas aplicando filtros dinámicos opcionales como país, ciudad, estado de la bodega y área mínima.
     * Soporta paginación para devolver un número determinado de resultados por página.
     * <p>
     * Este método permite realizar una búsqueda de bodegas con filtros opcionales. Si no se proporciona un filtro,
     * ese criterio será ignorado. Además, soporta la paginación, lo que permite obtener un número limitado de resultados por solicitud.
     * </p>
     *
     * @param pais           país de ubicación (opcional) para filtrar las bodegas por país.
     * @param ciudad         ciudad de ubicación (opcional) para filtrar las bodegas por ciudad.
     * @param estado         estado de la bodega (ACTIVA, INACTIVA, etc.) (opcional) para filtrar por estado de la bodega.
     * @param areaMinima     área mínima de la bodega en metros cuadrados (opcional) para filtrar por tamaño.
     * @param pagina         número de página para la paginación de los resultados.
     * @param size           tamaño de la página para la paginación de los resultados.
     * @return lista paginada de bodegas que cumplen con los filtros, convertidas a DTO.
     *         La lista estará en el formato adecuado para ser consumida por el cliente.
     */
    List<BodegaDto> listarBodegas(String pais, String ciudad, EstadoBodega estado,
                                  Double areaMinima, Double alturaMinima,
                                  int pagina, int size);


    List<BodegaDto> listarBodegasMapa();




}
