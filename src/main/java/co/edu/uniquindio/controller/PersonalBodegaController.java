/*
*La clase controller se encarga de interacción
 */

package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.PersonalBodega.CrearPersonalBodegaDTO;
import co.edu.uniquindio.dto.MensajeDTO;

import co.edu.uniquindio.dto.PersonalBodega.EditarPersonalBodegaDTO;
import co.edu.uniquindio.dto.PersonalBodega.PersonalBodegaDTO;
import co.edu.uniquindio.service.PersonalBodegaService;
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
    public ResponseEntity<MensajeDTO<String>> crearPersonalBodega(@Valid @RequestBody CrearPersonalBodegaDTO personal) throws Exception {
        personalBodegaServicio.crearPersonalBodega(personal);
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Personal de bodega registrado "));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<PersonalBodegaDTO>> obtenerPersonalBodegaPoId(@PathVariable String id) throws Exception {
        PersonalBodegaDTO personal = personalBodegaServicio.obtenerPersonalBodegaPoId(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, personal));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarPersonalBodega(@PathVariable String id) throws Exception {
        personalBodegaServicio.eliminarPersonalBodega(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Personal de bodega eliminado"));
    }

    //@SecurityRequirement(name = "cookieAuth")
    @PutMapping
    public ResponseEntity<MensajeDTO<String>> actualizarPersonalBodega(@Valid @RequestBody EditarPersonalBodegaDTO cuentaPersonal) throws Exception {
        personalBodegaServicio.actualizarPersonalBodega(cuentaPersonal);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Información de personal de bodega actualizada"));
    }


}
