package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.service.common.AuthServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class AuthController {


    private final AuthServicio authServicio;

    /**
     * Endpoint para autenticar usuarios del sistema.
     * <p>
     * Este método recibe las credenciales del usuario (email y contraseña) mediante un objeto {@link LoginDto},
     * las valida y, si son correctas, genera y retorna un token JWT encapsulado en un {@link TokenDto}.
     * </p>
     *
     * @param login Objeto que contiene las credenciales de acceso del usuario (email y contraseña).
     *              Validado automáticamente mediante la anotación {@code @Valid}.
     * @return Una respuesta HTTP 200 que contiene un {@link MensajeDTO} con el token JWT generado si la autenticación es exitosa.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada es incorrecta.
     * @throws ElementoNoEncontradoException Si no se encuentra al usuario con el email proporcionado.
     */
    @PostMapping("/login")
    private ResponseEntity<MensajeDTO<TokenDto>> login (@Valid @RequestBody LoginDto login)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

        // Llama al servicio de autentificación para procesar el login y genera el token JWT
        TokenDto tokenDto = authServicio.login(login);

        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDTO<>(false,tokenDto));
    }


    @PostMapping("/login-google")
    private ResponseEntity<MensajeDTO<TokenDto>> loginConGoogle(@RequestBody Map<String, String> body)
            throws ElementoNoEncontradoException {

        // Obtener el token de Google desde el cuerpo (clave: "credential")
        String credential = body.get("token");

        // Pasar el token al servicio para validar y generar nuestro token JWT
        TokenDto tokenDto = authServicio.loginConGoogle(credential);

        // Retornar el token dentro de un contenedor estándar
        return ResponseEntity.status(HttpStatus.OK).body(new MensajeDTO<>(false, tokenDto));
    }


    /**
     * Solicita el restablecimiento de la contraseña para un usuario registrado.
     * @param dto DTO con el email del usuario
     * @throws ElementoNoEncontradoException Si no se encuentra al usuario
     */
    @PostMapping("/restablecer-password")
    public ResponseEntity<MensajeDTO<String>> solicitarRestablecimientoPassword(@RequestBody @Valid SolicitudEmailDto dto)
            throws ElementoNoEncontradoException {
        authServicio.solicitarRestablecimientoPassword(dto);
        return ResponseEntity.ok().body(new MensajeDTO<>(false,"Solicitud de restablecimiento de contraseña enviada"));
    }


    /**
     * Verifica el código de restablecimiento de contraseña enviado al email.
     * @param dto DTO con email y código de verificación
     * @throws ElementoIncorrectoException Si el código es incorrecto o ha expirado
     * @throws ElementoNoEncontradoException Si no se encuentra al usuario
     */
    @PostMapping("/verificar-codigo")
    public ResponseEntity<MensajeDTO<String>> verificarCodigoPassword(@RequestBody @Valid VerificarCodigoDto dto)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

        authServicio.verificarCodigoPassword(dto);
        return ResponseEntity.ok().body(new MensajeDTO<>(false,"Código verificado exitosamente"));
    }


    /**
     * Actualiza la contraseña de un usuario
     * @param dto DTO con email y nueva contraseña
     * @throws ElementoNoEncontradoException Si no se encuentra al usuario
     */
    @PutMapping("/actualizar-password")
    public ResponseEntity<MensajeDTO<String>> actualizarPassword(@RequestBody @Valid ActualizarPasswordDto dto)
            throws ElementoNoEncontradoException {

        authServicio.actualizarPassword(dto);
        return ResponseEntity.ok().body(new MensajeDTO<>(false,"Contraseña actualizada correctamente"));
    }


    /**
     * Endpoint para solicitar la reactivación de la cuenta usando el email.
     */
    @PostMapping("/reactivar-cuenta")
    public ResponseEntity<MensajeDTO<String>> reactivarCuenta(@RequestBody LoginDto login)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

        // Se utiliza el email del login para restablecer la cuenta
        authServicio.reactivarCuenta(login.email());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MensajeDTO<>(false, "Cuenta reactivada exitosamente. Ahora puedes volver a iniciar sesión."));
    }


}
