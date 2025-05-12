package co.edu.uniquindio.controller.agenteVentas;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.EditarAgenteVentasDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.service.users.AgenteVentaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/agente-ventas") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class AgenteVentasController {


    private final AgenteVentaServicio agenteVentaServicio;


    @PutMapping("/editar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@PathVariable Long id,
                                                           @RequestParam(required = true) Long idAgenteVentas,
                                                           @RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) String telefono,
                                                           @RequestParam(required = false) String codigoPais,
                                                           @RequestParam(required = false) String codigoPaisSecundario,
                                                           @RequestParam(required = false) String telefonoSecundario,
                                                           @RequestParam(required = false) MultipartFile imagenPerfil)
            throws ElementoIncorrectoException, ElementoNulosException, ElementoNoEncontradoException {

        // Verificamos si el ID del DTO coincide con el ID de la URL (esto es una validación opcional)
        if (!id.equals(idAgenteVentas)) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);}

        EditarAgenteVentasDto agenteVentasDto = new EditarAgenteVentasDto( idAgenteVentas, nombre, telefono, codigoPais,  telefonoSecundario,codigoPaisSecundario, imagenPerfil);

        agenteVentaServicio.editarAgenteVentas(agenteVentasDto);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Cuenta editada correctamente"));
    }


    @PutMapping("/editar-user/{id}")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(@PathVariable Long id,
                                                            @Valid @RequestBody EditarUserPersona editarUserPersona)
            throws ElementoIncorrectoException, ElementoRepetidoException, ElementoNoEncontradoException {

        // Verificamos si el ID del DTO coincide con el ID de la URL (esto es una validación opcional)
        if (!id.equals(editarUserPersona.id())) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);
        }

       agenteVentaServicio.editarUserAgenteVentas(editarUserPersona);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Usuario editado correctamente"));
    }



    @GetMapping("/perfil/{id}")
    public ResponseEntity<MensajeDTO<AgenteVentasDto>> obtenerAgenteId(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        AgenteVentasDto agenteVentasDto = agenteVentaServicio.obtenerAgenteVentasId(id);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, agenteVentasDto));
    }



    @GetMapping("/perfil/{id}/sede")
    public ResponseEntity<MensajeDTO<SedeDto>> obtenerSedeRRHH(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        SedeDto sedeDto = agenteVentaServicio.obtenerSedeAgente(id);
        return ResponseEntity.ok(new MensajeDTO<>(false,sedeDto));
    }



    @GetMapping("/cuenta-agente-ventas/email/{email}")
    public ResponseEntity<MensajeDTO<AgenteVentasDto>> obtenerAgenteEmail(@PathVariable String email)
            throws ElementoNoEncontradoException {

        AgenteVentasDto agenteVentasDto = agenteVentaServicio.obtenerAgenteVentasEmail(email);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, agenteVentasDto));
    }

}
