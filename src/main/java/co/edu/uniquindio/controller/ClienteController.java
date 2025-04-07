package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.Cliente.ClienteDto;
import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.Cliente.EditarClienteDTO;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.service.ClienteServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales (como clienteServicio)
@RequestMapping("/api/clientes") // Define la ruta base para todos los endpoints de este controlador
@CrossOrigin(origins = "http://localhost:4200") // ✅ Permitir peticiones desde Angular
//Clase del Cliente Controlador
public class ClienteController {

    private final ClienteServicio clienteServicio; // 🛠 Servicio que contiene la lógica de negocio para clientes


    // Endpoint para crear un nuevo cliente
    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crear(@Valid @RequestBody CrearClienteDTO cuenta) throws Exception {
        clienteServicio.crear(cuenta); // Si lanza excepción, será capturada globalmente
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MensajeDTO<>(false, "Su registro ha sido exitoso"));
    }


    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @GetMapping("/{id}") // 🔎 Obtiene la información de un cliente por su ID
    public ResponseEntity<MensajeDTO<ClienteDto>> obtener(@PathVariable String id) throws Exception {
        ClienteDto info = clienteServicio.obtener(id); // Llama al servicio para obtener los datos del cliente
        return ResponseEntity.ok(new MensajeDTO<>(false,info)); // Retorna la información del cliente
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @DeleteMapping("/{id}") // 🗑 Elimina un cliente por su ID
    public ResponseEntity<MensajeDTO<String>> eliminar(@PathVariable String id) throws Exception {
        clienteServicio.eliminar(id); // Llama al servicio para eliminar el cliente
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @PutMapping // ✏️ Actualiza la información de un cliente
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarClienteDTO cuenta) throws Exception{
        clienteServicio.editar(cuenta); // Llama al servicio para editar el cliente
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }

    @SecurityRequirement(name = "cookieAuth")  // 🔐 Método protegido (requiere autenticación con cookies)
    @GetMapping // 📋 Lista todos los clientes, con opción de filtrar por nombre y ciudad
    public ResponseEntity<MensajeDTO<List<ClienteDto>>> listarTodos(

            @RequestParam(required = false) String nombre, // 🔍 Filtro opcional por nombre
            @RequestParam(required = false) String ciudad, // 🔍 Filtro opcional por ciudad
            @RequestParam int pagina // 📄 Número de página para paginación
    ){
        List<ClienteDto> lista = clienteServicio.listarTodos(pagina); // Llama al servicio para obtener la lista paginada
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

}
