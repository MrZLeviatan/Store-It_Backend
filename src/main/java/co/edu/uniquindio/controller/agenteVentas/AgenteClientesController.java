package co.edu.uniquindio.controller.agenteVentas;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.service.objects.BodegaServicio;
import co.edu.uniquindio.service.users.ClienteServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/agente-ventas/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class AgenteClientesController {


    private final ClienteServicio clienteServicio;

    /**
     * Endpoint para registrar un nuevo cliente en la plataforma Store-It.
     * <p>
     * Este método permite la creación de una cuenta de cliente incluyendo sus datos personales,
     * credenciales de acceso (correo y contraseña), imagen de perfil y datos de ubicación.
     * <p>
     * El formulario debe enviarse en formato <strong>multipart/form-data</strong>, ya que incluye un archivo (imagen).
     * @param cuenta Objeto {@link CrearClienteDto} que contiene toda la información necesaria para el registro.
     * @return {@link ResponseEntity} con estado HTTP 201 (Created) si el cliente fue registrado exitosamente.
     *
     * @throws ElementoRepetidoException        Si el correo electrónico o el número de teléfono ya están en uso.
     * @throws ElementoIncorrectoException      Si alguno de los datos proporcionados no es válido.
     * @throws ElementoNulosException           Sí faltan campos obligatorios requeridos para el registro.
     * @throws ElementoNoEncontradoException    Si alguna entidad relacionada (como la ubicación) no se encuentra registrada.
     */
    @PostMapping("/registro-cliente")
    public ResponseEntity<MensajeDTO<String>> crearCliente(@Valid @ModelAttribute CrearClienteDto cuenta)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException {

        // Llamamos al servicio que realiza la lógica de creación del cliente
        clienteServicio.registrarCliente(cuenta);

        // Retornamos HTTP 201 (CREATED)
        return ResponseEntity.status(201).body(new MensajeDTO<>(false, "Usuario creado"));
    }









}
