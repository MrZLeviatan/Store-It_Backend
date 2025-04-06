/*
*La clase controller se encarga de interacción
 */

package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.PersonalBodegaDTO;
import co.edu.uniquindio.dto.MessageDTO;

import co.edu.uniquindio.service.service.PersonalBodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personal-bodega")
public class PersonalBodegaController {

    private final PersonalBodegaService personalBodegaServicio;

    @PostMapping //Metodos HTTP
    public ResponseEntity<MessageDTO<String>> crearPersonalBodega(@Valid @RequestBody PersonalBodegaDTO personal) throws Exception {
        personalBodegaServicio.crearPersonalBodega(personal);
        return ResponseEntity.status(201).body(new MessageDTO<>(false, "Personal de bodega registrado "));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO<PersonalBodegaDTO>> obtenerPersonalBodegaPoId(@PathVariable String id) throws Exception {
        PersonalBodegaDTO personal = personalBodegaServicio.obtenerPersonalBodegaPoId(id);
        return ResponseEntity.ok(new MessageDTO<>(false, personal));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> eliminarPersonalBodega(@PathVariable String id) throws Exception {
        personalBodegaServicio.eliminarPersonalBodega(id);
        return ResponseEntity.ok(new MessageDTO<>(false, "Personal de bodega eliminado"));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @PutMapping
    public ResponseEntity<MessageDTO<String>> actualizarPersonalBodega(@Valid @RequestBody PersonalBodegaDTO cuentaPersonal) throws Exception {
        personalBodegaServicio.actualizarPersonalBodega(cuentaPersonal);
        return ResponseEntity.ok(new MessageDTO<>(false, "Información de personal de bodega actualizada"));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @GetMapping
    public ResponseEntity<MessageDTO<List<PersonalBodegaDTO>>> listarTodos(@RequestParam int pagina) {
        List<PersonalBodegaDTO> lista = personalBodegaServicio.listarPersonalBod(pagina);
        return ResponseEntity.ok(new MessageDTO<>(false, lista));
    }
}
