package co.edu.uniquindio.controller.personalBodega;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.CrearProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.ProductoServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-bodega/producto")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class PersonalProductoController {


    private final ProductoServicio productoServicio;


    @PostMapping("/registrar-producto")
    public ResponseEntity<MensajeDTO<String>> crearProducto(@RequestBody CrearProductoDto crearProductoDTO)
            throws ElementoNoEncontradoException {
        productoServicio.crearProducto(crearProductoDTO);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Producto registrado exitosamente"));
    }


    @GetMapping("/cliente/{email}/espacios")
    public ResponseEntity<List<EspacioDto>> listarEspaciosCliente(@PathVariable String email)
            throws ElementoNoEncontradoException {

        List<EspacioDto> espacio = productoServicio.listarEspaciosCliente(email);
        if (espacio.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(espacio); // Si hay bodegas, retorna 200 con los datos.
    }


    @PostMapping("/retirar-producto")
    public ResponseEntity<MensajeDTO<String>> retirarProducto(@RequestBody RetiroProductoDto retiroProductoDto)
            throws ElementoNoEncontradoException {
        productoServicio.retirarProducto(retiroProductoDto);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Producto retirado exitosamente"));
    }

    @GetMapping("/cliente/{email}/productos")
    public ResponseEntity<List<ProductoDto>> listarProductosCliente(@PathVariable String email)
            throws ElementoNoEncontradoException {

        List<ProductoDto> productos = productoServicio.listarProductosCliente(email);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(productos); // Si hay bodegas, retorna 200 con los datos.
    }



}
