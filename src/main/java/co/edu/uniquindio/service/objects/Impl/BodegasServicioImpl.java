package co.edu.uniquindio.service.objects.Impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.bodega.EditarBodegaDto;
import co.edu.uniquindio.dto.objects.bodega.CrearBodegaDto;
import co.edu.uniquindio.exception.ElementoAunEnUsoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.BodegaMapper;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.enums.EstadoBodega;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.service.objects.BodegaServicio;
import co.edu.uniquindio.service.utils.CloudinaryServicio;
import jakarta.persistence.criteria.Expression;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de bodegas.
 * <p>
 * Esta clase proporciona la lógica de negocio para operaciones CRUD sobre entidades {@link Bodega},
 * incluyendo el registro, edición, eliminación lógica y listado con filtros dinámicos.
 * Utiliza un {@link BodegaRepo} para acceso a datos, {@link BodegaMapper} para conversión entre entidades y DTOs,
 * y {@link CloudinaryServicio} para el manejo de imágenes.
 * </p>
 *
 * <p>
 * Las operaciones soportan validaciones, manejo de excepciones y registro de eventos en logs.
 * </p>
 *
 * @author MrZ.Leviatan
 * @see Bodega
 * @see BodegaDto
 * @see CrearBodegaDto
 * @see EditarBodegaDto
 * @see BodegaRepo
 * @see BodegaMapper
 * @see CloudinaryServicio
 * @see EstadoBodega
 *
 */
@Service
@RequiredArgsConstructor
public class BodegasServicioImpl implements BodegaServicio {

    private final BodegaMapper bodegaMapper;
    private final BodegaRepo bodegaRepo;
    private final CloudinaryServicio cloudinaryServicio;
    private static final Logger logger = LoggerFactory.getLogger(BodegasServicioImpl.class);


    /**
     * Crea una nueva instancia de {@link Bodega} en la base de datos.
     *
     * @param crearBodegaDto DTO con los datos necesarios para crear una bodega.
     * @throws IllegalStateException sí ocurre un error al subir las imágenes.
     */
    @Override
    public void registrarBodega(CrearBodegaDto crearBodegaDto) {
        // Mapear el DTO a la entidad
        Bodega bodega = bodegaMapper.toEntity(crearBodegaDto);

        // Subir imágenes a Cloudinary y guardar las URLs
        List<String> urls = crearBodegaDto.fotos().stream()
                .map(cloudinaryServicio::uploadImage)
                .collect(Collectors.toList());
        bodega.setFotos(urls);

        // Guardar la entidad en la base de datos
        bodegaRepo.save(bodega);

        logger.info("Bodega {} registrada con éxito", bodega.getId());
    }

    /**
     * Actualiza los datos de una bodega existente a partir del DTO recibido.
     * <p>
     * Solo se actualizan los campos no nulos definidos en él {@link EditarBodegaDto}, respetando los valores actuales
     * en caso de que el campo no se desee modificar. En caso de incluir nuevas imágenes, se subirán a Cloudinary y
     * reemplazarán la lista anterior de URLs.
     * </p>
     *
     * @param editarBodegaDto Objeto {@link EditarBodegaDto} que contiene los datos a actualizar. Debe incluir un ID válido.
     * @throws ElementoNoEncontradoException si no existe una bodega con el ID proporcionado.
     */
    @Override
    public void editarBodega(EditarBodegaDto editarBodegaDto) throws ElementoNoEncontradoException {

        Bodega bodega = obtenerBodega(editarBodegaDto.id());

        bodegaMapper.toEntity(editarBodegaDto, bodega);

        // Si hay nuevas fotos, subirlas a Cloudinary
        if (editarBodegaDto.fotos() != null && !editarBodegaDto.fotos().isEmpty()) {
            List<String> nuevasFotos = editarBodegaDto.fotos().stream()
                    .map(cloudinaryServicio::uploadImage)
                    .collect(Collectors.toList());
            bodega.setFotos(nuevasFotos); // Reemplaza las anteriores
        }
        // Guardar cambios
        bodegaRepo.save(bodega);
        logger.info("Bodega {} editado con exito", bodega.getId());
    }

    /**
     * Obtiene una instancia de {@link Bodega} a partir de su identificador único.
     *
     * @param id el ID de la bodega que se desea consultar.
     * @return la entidad {@link Bodega} correspondiente al ID proporcionado.
     * @throws ElementoNoEncontradoException si no existe ninguna bodega con el ID dado.
     */
    private Bodega obtenerBodega(Long id) throws ElementoNoEncontradoException {
        return bodegaRepo.findById(id)
                .orElseThrow(()-> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA + id));
    }

    /**
     * Elimina lógicamente una bodega marcándola como inactiva,
     * siempre que no tenga espacios con contratos activos.
     * <p>
     * Si existen contratos activos asociados a alguno de los espacios de la bodega,
     * se lanza una excepción {@link ElementoAunEnUsoException}.
     * </p>
     *
     * @param id el identificador único de la bodega a eliminar.
     * @throws ElementoNoEncontradoException si no se encuentra una bodega con el ID proporcionado.
     * @throws ElementoAunEnUsoException si la bodega tiene contratos activos asociados.
     */
    @Override
    public void eliminarBodega(Long id) throws ElementoNoEncontradoException, ElementoAunEnUsoException {

        Bodega bodega = obtenerBodega(id);

        boolean tieneContratos = bodega.getEspacios().stream()
                .anyMatch(espacio -> espacio.getContrato().getEstadoContrato().equals(EstadoContrato.ACTIVO));

        if (tieneContratos) {
            throw new ElementoAunEnUsoException(MensajeError.BODEGA_CON_CONTRATOS);
        }

        bodega.setEstadoBodega(EstadoBodega.INACTIVA);
        bodegaRepo.save(bodega);

        logger.info("Bodega {} eliminado con éxito", bodega.getId());
    }


    /**
     * Obtiene los datos de una bodega específica en forma de {@link BodegaDto}, a partir de su identificador único.
     *
     * @param id el ID de la bodega que se desea consultar.
     * @return un {@link BodegaDto} con la información de la bodega encontrada.
     * @throws ElementoNoEncontradoException si no se encuentra ninguna bodega con el ID proporcionado.
     */
    @Override
    public BodegaDto obtenerBodegaId(Long id) throws ElementoNoEncontradoException {
        return bodegaMapper.toDto(obtenerBodega(id));
    }


    /**
     * Lista las bodegas aplicando filtros dinámicos opcionales como país, ciudad, estado de la bodega y área mínima.
     * Soporta paginación.
     *
     * @param pais           país de ubicación (opcional)
     * @param ciudad         ciudad de ubicación (opcional)
     * @param estado         estado de la bodega (ACTIVA, LLENA, INACTIVA) (opcional)
     * @param areaMinima     área mínima de la bodega en metros cuadrados (opcional)
     * @param pagina         número de página para paginación
     * @param size           tamaño de la página para paginación
     * @return lista paginada de bodegas que cumplen con los filtros, convertidas a DTO
     */
    public List<BodegaDto> listarBodegas(String pais, String ciudad, EstadoBodega estado,
                                         Double areaMinima, Double alturaMinima,
                                         int pagina, int size) {

        // Crear objeto Pageable para aplicar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Crear Specification base (sin condiciones)
        Specification<Bodega> spec = Specification.where(null);

        // Filtro por país (si se proporciona)
        if (pais != null && !pais.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("ubicacion").get("pais"), pais));
        }

        // Filtro por ciudad (si se proporciona)
        if (ciudad != null && !ciudad.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("ubicacion").get("ciudad"), ciudad));
        }

        // Filtro por estado de bodega (si se proporciona)
        if (estado != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoBodega"), estado));
        }

        // Filtro por área mínima (si se proporciona)
        if (areaMinima != null) {
            spec = spec.and((root, query, builder) -> {
                // Como usamos un converter, accedemos directamente al campo como si fuera un Double
                Expression<Double> area = root.get("areaTotal");
                return builder.greaterThanOrEqualTo(area, areaMinima);
            });
        }

        // Filtro por altura minima
        if (alturaMinima != null){
            spec = spec.and((root, query, builder) -> {
                Expression<Double> altura = root.get("altura").get("value");
                return builder.greaterThanOrEqualTo(altura, alturaMinima);
            });
        }

        // Consultar bodegas usando los filtros y la paginación
        Page<Bodega> bodegas = bodegaRepo.findAll(spec, pageable);

        // Convertir entidades a DTOs
        return bodegas.getContent()
                .stream()
                .map(bodegaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BodegaDto> listarBodegasMapa() {
        List<Bodega> bodegas = bodegaRepo.findAll();

        return bodegas.stream()
                .map(bodegaMapper::toDto)
                .collect(Collectors.toList());
    }



}
