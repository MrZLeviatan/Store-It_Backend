package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.service.ClienteServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteServicio clienteServicio;


    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CrearClienteDTO cuenta) throws Exception{
        clienteServicio.crear(cuenta);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Su registro ha sido exitoso"));
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<ClienteDto>> obtener(@PathVariable String id) throws Exception {
        ClienteDto info = clienteServicio.obtener(id);
        return ResponseEntity.ok(new MensajeDTO<>(false,info));
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception {
        clienteServicio.eliminar(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }


    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @PutMapping
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarClienteDTO cuenta) throws Exception{
        clienteServicio.editar(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @GetMapping
    public ResponseEntity<MensajeDTO<List<ClienteDto>>> listarTodos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam int pagina
    ){
        List<ClienteDto> lista = clienteServicio.listarTodos(pagina);
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

}
