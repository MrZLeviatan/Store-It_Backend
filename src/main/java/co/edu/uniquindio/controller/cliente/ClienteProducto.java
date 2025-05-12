package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.EspacioServicio;
import co.edu.uniquindio.service.objects.ProductoServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales (como clienteServicio)
@RequestMapping("/api/cliente/producto") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class ClienteProducto {



    private final ProductoServicio productoServicio;
    private final EspacioServicio espacioServicio;


    @GetMapping("/cliente/{id}/productos")
    public ResponseEntity<List<ProductoDto>> listarProductosCliente(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        List<ProductoDto> productos = productoServicio.listarProductosClienteId(id);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(productos); // Si hay bodegas, retorna 200 con los datos.
    }


    @GetMapping("/producto/{id}/espacio")
    public ResponseEntity<EspacioDto> obtenerEspacioPorProducto(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        EspacioDto espacioDto = espacioServicio.listarEspaciosPorProducto(id);
        return ResponseEntity.ok(espacioDto);
    }
}
