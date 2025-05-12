package co.edu.uniquindio.controller.recursosHumanos;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.CrearAgenteVentasDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.service.objects.SedeServicio;
import co.edu.uniquindio.service.users.AgenteVentaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/recursos-humanos/agentes-ventas") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class RecursosAgenteController {

    private final AgenteVentaServicio agenteVentaServicio;
    private final SedeServicio sedeServicio;



    @PostMapping("/registrar")
    public ResponseEntity<MensajeDTO<String>> registrarAgente(@Valid @ModelAttribute CrearAgenteVentasDto agenteVentasDto)
            throws ElementoRepetidoException, ElementoIncorrectoException, ElementoNulosException,
            ElementoNoEncontradoException {

        agenteVentaServicio.registrarAgenteVentas(agenteVentasDto);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Agente de ventas registrado correctamente"));
    }


    @GetMapping("/sedes")
    public ResponseEntity<List<SedeDto>> obtenerSedes(){
        List<SedeDto> sedes = sedeServicio.listarSedesMapa();
        if (sedes.isEmpty()){
            return ResponseEntity.noContent().build(); // Si no hay sedes, retorna código 204.
        }
        return ResponseEntity.ok(sedes);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarAgente(@PathVariable Long id)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

        agenteVentaServicio.eliminarAgenteVentas(id);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Agente de ventas eliminado correctamente"));
    }


    @GetMapping("/cuenta-agente-ventas/{id}")
    public ResponseEntity<MensajeDTO<AgenteVentasDto>> obtenerAgenteId(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        AgenteVentasDto agenteVentasDto = agenteVentaServicio.obtenerAgenteVentasId(id);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, agenteVentasDto));
    }



    @GetMapping("/cuenta-agente-ventas/email/{email}")
    public ResponseEntity<MensajeDTO<AgenteVentasDto>> obtenerAgenteEmail(@PathVariable String email)
            throws ElementoNoEncontradoException {

        AgenteVentasDto agenteVentasDto = agenteVentaServicio.obtenerAgenteVentasEmail(email);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, agenteVentasDto));
    }


    @GetMapping("/listar-agentes")
    public ResponseEntity<MensajeDTO<List<AgenteVentasDto>>> listarAgentes(
            @RequestParam(required = false) String idSede,
            @RequestParam(required = false) LocalDate fechaContratacion,
            @RequestParam(required = false) TipoContrato tipoContrato,
            @RequestParam(required = false)EstadoContratoLaboral estadoContratoLaboral,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {

            List<AgenteVentasDto> agentes = agenteVentaServicio.listarAgenteVentas
                    (idSede, fechaContratacion,tipoContrato, estadoContratoLaboral, pagina, size);

            return ResponseEntity.ok().body(new MensajeDTO<>(false, agentes));
        }

}
