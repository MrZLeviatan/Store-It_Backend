package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.Login.LoginDto;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Autentifcar")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/iniciarSesion")
    public ResponseEntity<MensajeDTO<String>> iniciarSesion(@Valid @RequestBody LoginDto loginDTO ) throws Exception {
        int resultado = loginService.login(loginDTO);

        // Si es cliente
        if (resultado == 1) {
            return ResponseEntity.status(200).body(new MensajeDTO<>(false, "CLIENTE"));
        }

        // Si es otro rol (puedes modificar el mensaje según necesidad)
        return ResponseEntity.status(200).body(new MensajeDTO<>(false, "OTRO"));
    }
}
