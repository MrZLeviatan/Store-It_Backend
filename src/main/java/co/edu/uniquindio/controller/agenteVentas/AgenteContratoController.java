package co.edu.uniquindio.controller.agenteVentas;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.contrato.CrearContratoDto;
import co.edu.uniquindio.dto.objects.espacio.EspacioDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.service.objects.BodegaServicio;
import co.edu.uniquindio.service.objects.ContratoServicio;
import co.edu.uniquindio.service.objects.EspacioServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/agente-ventas/contrato") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class AgenteContratoController {


    private final ContratoServicio contratoServicio;
    private final BodegaServicio bodegaServicio;
    private final EspacioServicio espacioServicio;


    @PostMapping("/crear-contrato")
    public ResponseEntity<MensajeDTO<String>> crearCliente(@Valid @ModelAttribute CrearContratoDto cuenta)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException {

        contratoServicio.crearContrato(cuenta);

        // Retornamos HTTP 201 (CREATED)
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Contrato creado"));
    }


    @GetMapping("/bodegas")
    public ResponseEntity<List<BodegaDto>> obtenerBodegas() {
        List<BodegaDto> bodegas = bodegaServicio.listarBodegasMapa();
        if (bodegas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(bodegas); // Si hay bodegas, retorna 200 con los datos.
    }


    @GetMapping("/bodega/{idBodega}/espacios")
    public ResponseEntity<List<EspacioDto>> obtenerEspaciosDisponibles(@PathVariable Long idBodega) throws ElementoNoEncontradoException {

        List<EspacioDto> espacio = espacioServicio.listarEspaciosDispioniblesPorBodega(idBodega);
        if (espacio.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(espacio); // Si hay bodegas, retorna 200 con los datos.
    }


    @GetMapping("/agente/{idAgente}/contratos")
    public ResponseEntity<List<ContratoDto>> obtenerContratosPorAgente(
            @PathVariable Long idAgente,
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) EstadoContrato estadoContrato,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {

        // Validación de ID
        if (idAgente == null || idAgente <= 0) {
            return ResponseEntity.badRequest().build();
        }
        // Llamada al servicio con filtros
        List<ContratoDto> contratos = contratoServicio.obtenerContratoAgenteVentas(idAgente, fechaInicio, estadoContrato, pagina, size);
        // Si no hay contratos encontrados
        if (contratos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contratos);
    }



}


