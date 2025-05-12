package co.edu.uniquindio.controller.recursosHumanos;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.recursosHumanos.EditarRRHHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.RecursosHumanosDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.users.ClienteServicio;
import co.edu.uniquindio.service.users.PersonalBodegaServicio;
import co.edu.uniquindio.service.users.RecursosHumanosServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales
@RequestMapping("/api/recursos-humanos") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class RecursosHumanosController {


    private final ClienteServicio clienteServicio;
    private final PersonalBodegaServicio personalBodegaServicio;
    private final RecursosHumanosServicio recursosHumanosService;



    @GetMapping("/perfil/{id}")
    public ResponseEntity<MensajeDTO<RecursosHumanosDto>> obtenerRRHHId(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        // Llama al servicio para obtener los datos del cliente
        RecursosHumanosDto info = recursosHumanosService.obtenerRecursosId(id);

        // Retorna la información del cliente
        return ResponseEntity.ok(new MensajeDTO<>(false,info));
    }



    @PutMapping(value = "/editar-cuenta/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MensajeDTO<String>> editarCuentaCliente(@PathVariable Long id,
                                                                  @RequestParam(required = true) Long idRRHH,
                                                                  @RequestParam(required = false) String nombre,
                                                                  @RequestParam(required = false) String telefono,
                                                                  @RequestParam(required = false) String codigoPais,
                                                                  @RequestParam(required = false) String codigoPaisSecundario,
                                                                  @RequestParam(required = false) String telefonoSecundario,
                                                                  @RequestParam(required = false) MultipartFile imagenPerfil)
            throws ElementoIncorrectoException, ElementoNulosException,
            ElementoNoEncontradoException {

        // Verificación del ID de la URL y el ID de los datos recibidos
        if (!id.equals(idRRHH)) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);
        }
        // Crear el DTO manualmente con los valores obtenidos
        EditarRRHHDto cuenta = new EditarRRHHDto(idRRHH, nombre, telefono, telefonoSecundario, imagenPerfil, codigoPais,codigoPaisSecundario);
        // Llamada al servicio para editar los recursos humanos
        recursosHumanosService.editarRecursosHumanos(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }



    @GetMapping("/perfil/{id}/sede")
    public ResponseEntity<MensajeDTO<SedeDto>> obtenerSedeRRHH(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        SedeDto sedeDto = recursosHumanosService.obtenerSedeRRHH(id);
        return ResponseEntity.ok(new MensajeDTO<>(false,sedeDto));
    }






}
