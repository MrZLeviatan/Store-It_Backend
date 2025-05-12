package co.edu.uniquindio.controller.recursosHumanos;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.CrearAgenteVentasDto;
import co.edu.uniquindio.dto.users.personalBodega.CrearPersonalBodegaDto;
import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoCargo;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.service.objects.BodegaServicio;
import co.edu.uniquindio.service.users.PersonalBodegaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/recursos-humanos/personal-bodega") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class RecursosPersonalController {


    private final PersonalBodegaServicio personalBodegaService;
    private final BodegaServicio bodegaServicio;


    @PostMapping("/registrar")
    public ResponseEntity<MensajeDTO<String>> registrarAgente(@Valid @ModelAttribute CrearPersonalBodegaDto personalBodegaDto)
            throws ElementoRepetidoException, ElementoIncorrectoException, ElementoNulosException,
            ElementoNoEncontradoException {

        personalBodegaService.registrarPersonal(personalBodegaDto);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Personal de Bodega registrado correctamente"));
    }



    @GetMapping("/bodegas")
    public ResponseEntity<List<BodegaDto>> obtenerBodegas() {
        List<BodegaDto> bodegas = bodegaServicio.listarBodegasMapa();
        if (bodegas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(bodegas); // Si hay bodegas, retorna 200 con los datos.
    }


    @GetMapping("/listar-personal-bodega")
    public ResponseEntity<MensajeDTO<List<PersonalBodegaDto>>> listarAgentes(
            @RequestParam(required = false) String idBodega,
            @RequestParam(required = false) LocalDate fechaContratacion,
            @RequestParam(required = false) TipoContrato tipoContrato,
            @RequestParam(required = false) EstadoContratoLaboral estadoContratoLaboral,
            @RequestParam(required = false) TipoCargo tipoCargo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {

        List<PersonalBodegaDto> personalBodega = personalBodegaService.listarPersonalBodega
                (idBodega, fechaContratacion, estadoContratoLaboral,tipoContrato,tipoCargo, pagina, size);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, personalBodega));
    }



}
