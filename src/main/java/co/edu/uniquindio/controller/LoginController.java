package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.Login.LoginDto;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.MensajeIngresoDTO;
import co.edu.uniquindio.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/Autentifcar")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // ✅ Permitir peticiones desde Angular
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/iniciarSesion")
    public ResponseEntity<MensajeIngresoDTO<String>> iniciarSesion(@Valid @RequestBody LoginDto loginDTO ) throws Exception {

        String resultado = String.valueOf(loginService.login(loginDTO).rol());

        // Si es cliente
        if (resultado == "CLIENTE") {
            return ResponseEntity.status(200).body(new MensajeIngresoDTO<>(false, "CLIENTE",loginService.login(loginDTO).cedula()));
        }

        // Si es otro rol (puedes modificar el mensaje según necesidad)
        return ResponseEntity.status(200).body(new MensajeIngresoDTO<>(false, "OTRO",loginService.login(loginDTO).cedula()));
    }
}
