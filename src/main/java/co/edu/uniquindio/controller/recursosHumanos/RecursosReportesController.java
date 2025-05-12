package co.edu.uniquindio.controller.recursosHumanos;

import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.service.objects.SedeServicio;
import co.edu.uniquindio.service.users.AgenteVentaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/recursos-humanos/reportes") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class RecursosReportesController {


    private final SedeServicio sedeServicio;
    private final AgenteVentaServicio agenteVentaServicio;


    @GetMapping("/sedes")
    public ResponseEntity<List<SedeDto>> obtenerSedes(){
        List<SedeDto> sedes = sedeServicio.listarSedesMapa();
        if (sedes.isEmpty()){
            return ResponseEntity.noContent().build(); // Si no hay sedes, retorna código 204.
        }
        return ResponseEntity.ok(sedes);
    }


    @GetMapping("/agentes/{idSede}")
    public ResponseEntity<List<AgenteVentasDto>> obtenerAgentesVentasPorSede(@PathVariable Long idSede) {
        List<AgenteVentasDto> agentes = agenteVentaServicio.obtenerAgentesVentas(idSede);

        if (agentes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay agentes, retorna código 204.
        }

        return ResponseEntity.ok(agentes); // Si hay agentes, retorna código 200 con la lista de agentes
    }



    @PostMapping("/contratos/por-agentes")
    public ResponseEntity<List<ContratoDto>> obtenerContratosPorAgentesVentas(@RequestBody AgenteVentasDto agentesVentasDto) {
        // Llamamos al servicio para obtener los contratos
        Object contratoServicio;
        List<ContratoDto> contratosDto = agenteVentaServicio.obtenerContratosPorAgenteVentas(agentesVentasDto);

        if (contratosDto.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay contratos, retornamos un 204
        }

        return ResponseEntity.ok(contratosDto); // Si hay contratos, retornamos la lista
    }

}
