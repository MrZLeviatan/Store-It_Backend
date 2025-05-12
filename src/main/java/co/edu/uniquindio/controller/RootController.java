package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.common.email.MensajeContactoDTO;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.service.objects.BodegaServicio;
import co.edu.uniquindio.service.objects.SedeServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store-it") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class RootController {

    private final BodegaServicio bodegaServicio;
    private final SedeServicio sedeServicio;
    private final EmailServicio emailService;


    @GetMapping("/bodegas")
    public ResponseEntity<List<BodegaDto>> obtenerBodegas() {
        List<BodegaDto> bodegas = bodegaServicio.listarBodegasMapa();
        if (bodegas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Si no hay bodegas, retorna código 204.
        }
        return ResponseEntity.ok(bodegas); // Si hay bodegas, retorna 200 con los datos.
    }
    

    @GetMapping("/sedes")
    public ResponseEntity<List<SedeDto>> obtenerSedes(){
        List<SedeDto> sedes = sedeServicio.listarSedesMapa();
        if (sedes.isEmpty()){
            return ResponseEntity.noContent().build(); // Si no hay sedes, retorna código 204.
        }
        return ResponseEntity.ok(sedes);
    }


    @PostMapping("/contacto/enviar")
    public ResponseEntity<MensajeDTO<String>> enviarContacto(@RequestBody MensajeContactoDTO dto) {
        try {
            emailService.enviarMensajeContacto(dto);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Mensaje enviado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MensajeDTO<>(true, "Error al enviar el mensaje: " + e.getMessage()));
        }}


}
