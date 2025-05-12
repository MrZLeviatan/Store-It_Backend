package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.EditarClienteDto;
import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.service.users.ClienteServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController // Indica que esta clase es un controlador REST
@RequiredArgsConstructor // Genera automáticamente el constructor con los atributos finales (como clienteServicio)
@RequestMapping("/api/cliente") // Define la ruta base para todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class ClienteController {

    // Servicio que contiene la lógica de negocio para clientes
    private final ClienteServicio clienteServicio;


    /**
     * Método encargado de eliminar un cliente del sistema.
     * @param eliminarClienteDto El objeto {@link EliminarClienteDto} que contiene el ID del cliente
     *                           y la contraseña para verificar la autenticidad de la operación.
     * @return Un objeto {@link ResponseEntity} con un {@link MensajeDTO} que indica si la operación
     *         fue exitosa o no. En caso de error, se lanzan excepciones personalizadas.
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el ID proporcionado.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada no es correcta.
     */
    @DeleteMapping("/eliminar")
    public ResponseEntity<MensajeDTO<String>> eliminarCliente(@RequestBody EliminarClienteDto eliminarClienteDto)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {
        // Llama al servicio para eliminar el cliente
        clienteServicio.eliminarCliente(eliminarClienteDto);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }



    @PutMapping(value = "/editar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> editarCuentaCliente(@PathVariable Long id,
                                                                  @RequestParam(required = true) Long idCliente,
                                                                  @RequestParam(required = false) String nombre,
                                                                  @RequestParam(required = false) String telefono,
                                                                  @RequestParam(required = false) String codigoPais,
                                                                  @RequestParam(required = false) String codigoPaisSecundario,
                                                                  @RequestParam(required = false) String telefonoSecundario,
                                                                  @RequestParam(required = false) MultipartFile imagenPerfil)
            throws ElementoIncorrectoException, ElementoNulosException,
            ElementoNoEncontradoException {

        // Verificamos si el ID del DTO coincide con el ID de la URL (esto es una validación opcional)
        if (!id.equals(idCliente)) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);}

        EditarClienteDto cuenta = new EditarClienteDto(
                idCliente, nombre, telefono, codigoPais, codigoPaisSecundario, telefonoSecundario, imagenPerfil, null);

        // Llama al servicio para editar el cliente
        clienteServicio.editarCliente(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }


    /**
     * Método encargado de obtener la información de un cliente a partir de su ID.
     * @param id El identificador único del cliente. Este valor se obtiene de la ruta de la solicitud
     *           como un parámetro de la URL.
     * @return Un objeto {@link ResponseEntity} con un {@link MensajeDTO} que contiene la información
     *         del cliente si la operación es exitosa. Si el cliente no se encuentra, se lanza una
     *         excepción personalizada {@link ElementoNoEncontradoException}.
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el ID proporcionado.
     */
    @GetMapping("/perfil/{id}")
    public ResponseEntity<MensajeDTO<ClienteDto>> obtenerClienteId(@PathVariable Long id)
            throws ElementoNoEncontradoException {

        // Llama al servicio para obtener los datos del cliente
        ClienteDto info = clienteServicio.obtenerPorId(id);

        // Retorna la información del cliente
        return ResponseEntity.ok(new MensajeDTO<>(false,info));
    }


    /**
     * Endpoint para obtener la información de un cliente mediante su correo electrónico.
     *
     * @param email El correo electrónico del cliente a consultar.
     * @return Un {@link ResponseEntity} que contiene un {@link MensajeDTO} con los datos del cliente.
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el correo proporcionado.
     */
    @GetMapping("/cuenta-cliente/email/{email}")
    public ResponseEntity<MensajeDTO<ClienteDto>> obtenerClienteEmail(@PathVariable String email)
            throws ElementoNoEncontradoException {

        // Llama al servicio para obtener la información del cliente por correo
        ClienteDto info = clienteServicio.obtenerPorEmail(email);

        // Retorna la información del cliente envuelta en un DTO de respuesta
        return ResponseEntity.ok(new MensajeDTO<>(false,info));
    }


    /**
     * Endpoint para editar los datos del usuario asociados al cliente.
     *
     * @param editarUserPersona DTO que contiene los nuevos datos del usuario a editar.
     * @return Un {@link ResponseEntity} que envuelve un {@link MensajeDTO} con el mensaje de éxito.
     * @throws ElementoIncorrectoException Si los datos proporcionados son inválidos.
     * @throws ElementoRepetidoException Si el nuevo correo ya está en uso por otro cliente.
     * @throws ElementoNoEncontradoException Si no se encuentra el cliente asociado.
     */
    @PutMapping("/editar-user/{id}")
    public ResponseEntity<MensajeDTO<String>> editarUserCliente(@PathVariable Long id,
                                                                @Valid @RequestBody EditarUserPersona editarUserPersona)
            throws ElementoIncorrectoException, ElementoRepetidoException,
            ElementoNoEncontradoException {

        // Verificamos si el ID del DTO coincide con el ID de la URL (esto es una validación opcional)
        if (!id.equals(editarUserPersona.id())) {
            throw new ElementoIncorrectoException(MensajeError.ID_NO_COINCIDE_URL);
        }

        // Llama al servicio para editar los datos del usuario del cliente
        clienteServicio.editarUserCliente(editarUserPersona);

        // Retorna un mensaje de éxito
        return ResponseEntity.ok(new MensajeDTO<>(false, "Usuario editado exitosamente"));
    }



}
