package co.edu.uniquindio.controller.personalBodega;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.personalBodega.EditarPersonalBodegaDto;
import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.users.PersonalBodegaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
    @RequestMapping("/api/personal-bodega") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class personalBodegaController {


    private final PersonalBodegaServicio personalBodegaServicio;


    @GetMapping("/perfil/{id}")
    public ResponseEntity<MensajeDTO<PersonalBodegaDto>> obtenerAgenteId(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        PersonalBodegaDto personalBodegaDto = personalBodegaServicio.obtenerPersonalId(id);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, personalBodegaDto));
    }


    @PutMapping("/editar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@PathVariable Long id,
                                                           @RequestParam(required = true) Long idPersonalBodega,
                                                           @RequestParam(required = false) String nombre,
                                                           @RequestParam(required = false) String telefono,
                                                           @RequestParam(required = false) String codigoPais,
                                                           @RequestParam(required = false) String codigoPaisSecundario,
                                                           @RequestParam(required = false) String telefonoSecundario,
                                                           @RequestParam(required = false) MultipartFile imagenPerfil)
            throws ElementoIncorrectoException, ElementoNulosException, ElementoNoEncontradoException {

        // Verificamos si el ID del DTO coincide con el ID de la URL (esto es una validación opcional)
        if (!id.equals(idPersonalBodega)) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);}

        EditarPersonalBodegaDto editarPersonalBodegaDto = new EditarPersonalBodegaDto( idPersonalBodega, nombre, telefono, codigoPais,  telefonoSecundario,codigoPaisSecundario, imagenPerfil);

        personalBodegaServicio.editarPersonal(editarPersonalBodegaDto);

        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Cuenta editada correctamente"));
    }


    @GetMapping("/perfil/{id}/bodega")
    public ResponseEntity<MensajeDTO<BodegaDto>> obtenerBodegaPersonal(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        BodegaDto bodegaDto = personalBodegaServicio.obtenerBodegaPersonal(id);
        return ResponseEntity.ok(new MensajeDTO<>(false,bodegaDto));
    }

}
