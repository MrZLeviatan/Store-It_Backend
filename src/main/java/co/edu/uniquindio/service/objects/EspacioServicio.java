package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.espacio.CrearEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EditarEspacioDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.Impl.EspacioServicioImpl;

import java.util.List;

public interface EspacioServicio {



    void registrarEspacio(CrearEspacioDto crearEspacioDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException;


    void editarEspacio(EditarEspacioDto editarEspacioDto) throws ElementoNoEncontradoException, ElementoIncorrectoException;

    void eliminarEspacio(Long id);

    EspacioDto obtenerEspacioId(Long id);


    List<EspacioDto> listarEspacios();


    void ingresarProducto(Long idEspacio, ProductoDto producto);

    List<EspacioDto> listarEspaciosDispioniblesPorBodega(Long idBodega) throws ElementoNoEncontradoException;


    void retirarProducto(Long idEspacio, ProductoDto producto);


    EspacioDto listarEspaciosPorProducto(Long idProducto) throws ElementoNoEncontradoException;



}
