package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.Cliente.CrearClienteDTO;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.repository.ClienteRepo;
import co.edu.uniquindio.service.ClienteServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
