package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.CrearProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;

import java.util.List;

public interface ProductoServicio {


    void crearProducto(CrearProductoDto crearProductoDTO) throws ElementoNoEncontradoException;

    List<EspacioDto> listarEspaciosCliente(String email) throws ElementoNoEncontradoException;

    void retirarProducto(RetiroProductoDto retirarProductoDTO) throws ElementoNoEncontradoException;


    List<ProductoDto> listarProductosCliente(String email) throws ElementoNoEncontradoException;


    List<ProductoDto> listarProductosClienteId(Long id) throws ElementoNoEncontradoException;
}
