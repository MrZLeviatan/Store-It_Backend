package co.edu.uniquindio.service.objects.Impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.espacio.CrearEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EditarEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoAunEnUsoException;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.EspacioMapper;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.Producto;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.objects.EspacioRepo;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.service.objects.EspacioServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import co.edu.uniquindio.service.utils.PdfService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Area;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EspacioServicioImpl implements EspacioServicio {

    private final EspacioRepo espacioRepo;
    private final EspacioMapper espacioMapper;
    private final BodegaRepo bodegaRepo;
    private final PdfService pdfService;
    private final EmailServicio emailServicio;
    private final ProductoRepo productoRepo;
    private static final Logger logger = LoggerFactory.getLogger(EspacioServicioImpl.class);


    /**
     * Registra un nuevo espacio en una bodega validando que cumpla con las restricciones
     * de altura y área disponible.
     *
     * @param crearEspacioDto DTO con los datos necesarios para crear el espacio.
     * @throws ElementoNoEncontradoException si no se encuentra la bodega especificada.
     * @throws ElementoIncorrectoException si la altura o el área del espacio superan
     *                                     los límites permitidos por la bodega.
     */
    @Override
    public void registrarEspacio(CrearEspacioDto crearEspacioDto) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        // Buscar la bodega por ID, si no se encuentra lanza una excepción
        Bodega bodega = bodegaRepo.findById(crearEspacioDto.idBodega())
                .orElseThrow(()-> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA));

        // Validar que la altura del espacio no supere la de la bodega
        if (bodega.getAltura() < crearEspacioDto.altura()){
            throw new ElementoIncorrectoException(MensajeError.ALTURA_EXCEDE);}

        // Validar que el área solicitada no supere el área disponible de la bodega
        if (bodega.areaDisponible() < crearEspacioDto.areaTotal()){
            throw new ElementoIncorrectoException(MensajeError.ESPACIO_EXCEDE);}

        // Convertir el DTO en entidad usando el mapper configurado previamente
        Espacio espacio = espacioMapper.toEntity(crearEspacioDto);

        // Asegurar que la bodega se setee correctamente (aunque ya se mapea, se refuerza aquí)
        espacio.setBodega(bodega);

        // Guardar el nuevo espacio en la base de datos
        espacioRepo.save(espacio);
    }


    @Override
    public void editarEspacio(EditarEspacioDto editarEspacioDto) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        // Buscar el espacio por ID, si no se encuentra lanza una excepción
        Espacio espacio = obtenerEspacio(editarEspacioDto.id());

        // Buscar la bodega por ID, si no se encuentra lanza una excepción
        Bodega bodega = bodegaRepo.findById(espacio.getBodega().getId())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA));

        // Validar que la altura del espacio no supere la de la bodega
        if (bodega.getAltura() < editarEspacioDto.altura()){
            throw new ElementoIncorrectoException(MensajeError.ALTURA_EXCEDE);}

        // Validar que el área solicitada no supere el área disponible de la bodega
        if ((bodega.areaDisponible() - espacio.getAreaTotal().getValue().doubleValue())
                < editarEspacioDto.areaTotal()){
            throw new ElementoIncorrectoException(MensajeError.ESPACIO_EXCEDE);}

        // Validar si el espacio no está asociado a un contrato
        if (espacio.getContrato() != null || !espacio.getProductos().isEmpty()){
            throw new ElementoAunEnUsoException(MensajeError.ESPACIO_EN_USO);
        }
    }


    /**
     * Obtiene un espacio por su ID.
     *
     * @param id Identificador del espacio a buscar.
     * @return La entidad {@link Espacio} correspondiente al ID.
     * @throws ElementoNoEncontradoException si no se encuentra un espacio con el ID proporcionado.
     */
    private Espacio obtenerEspacio(Long id) throws ElementoNoEncontradoException {
        return espacioRepo.findById(id)
                .orElseThrow(()-> new
                        ElementoNoEncontradoException(MensajeError.ESPACIO_NO_ENCONTRADO));
    }

    @Override
    public void eliminarEspacio(Long id) {

    }

    @Override
    public EspacioDto obtenerEspacioId(Long id) {
        return null;
    }

    @Override
    public List<EspacioDto> listarEspacios() {
        return List.of();
    }




    @Override
    public void ingresarProducto(Long idEspacio, ProductoDto producto) {

    }

    @Override
    public List<EspacioDto> listarEspaciosDispioniblesPorBodega(Long idBodega) throws ElementoNoEncontradoException {
        Bodega bodega = bodegaRepo.findById(idBodega)
        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA));

        List<Espacio> espaciosDisponibles = bodega.getEspacios();

        System.out.printf("LLegamos aqui");

        // Convertir entidades a DTOs
        return espaciosDisponibles
                .stream()
                .filter(espacio -> espacio.getEstadoEspacio().equals(EstadoEspacio.LIBRE))
                .map(espacioMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void retirarProducto(Long idEspacio, ProductoDto producto) {

    }

    @Override
    public EspacioDto listarEspaciosPorProducto(Long idProducto) throws ElementoNoEncontradoException {
        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_ENCONTRADO));

        // 🇪🇸 Obtener el espacio relacionado con el producto
        // 🇺🇸 Get the space related to the product
        Espacio espacio = producto.getEspacio(); // Asume que el producto tiene un método getEspacio()

        // 🇪🇸 Retornar el espacio convertido en lista de DTOs (aunque sea uno solo)
        // 🇺🇸 Return the space converted as a list of DTOs (even if it's just one)
        return espacioMapper.toDto(espacio);
    }
}
