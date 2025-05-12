package co.edu.uniquindio.service.common.impl;

import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.model.users.common.CodigoRestablecimiento;
import co.edu.uniquindio.security.JWTUtils;
import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.model.users.PersonalBodega;
import co.edu.uniquindio.model.users.RecursosHumanos;
import co.edu.uniquindio.model.users.base.Persona;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.repository.users.RecursosHumanosRepo;
import co.edu.uniquindio.service.common.AuthServicio;
import co.edu.uniquindio.service.users.ClienteServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Servicio de autenticación centralizado para los distintos tipos de usuarios
 * del sistema: Cliente, Agente de Ventas, Personal de Bodega y Recursos Humanos.
 * <p>
 * Esta clase implementa el flujo completo de autenticación y recuperación
 * de credenciales, proporcionando soporte a las siguientes funcionalidades:
 * <ul>
 * <li> Inicio de sesión con verificación de correo electrónico y contraseña.
 * <li> Generación y envío de códigos para restablecimiento de contraseña.
 * <li> Validación de códigos y actualización segura de contraseñas.
 * </ul>
 * <p>
 * SEGURIDAD Y UTILIDADES USADAS:
 * <ul>
 * <li> {@link JWTUtils}: Se utiliza para generar y validar tokens JWT,
 *   permitiendo autenticación segura sin mantener sesiones activas.
 * <li> {@link PasswordEncoder}: Proporciona encriptación de contraseñas con hash seguro,
 *   usado tanto al registrar como al verificar credenciales.
 * <li> {@link EmailServicio}: Encargado de enviar correos electrónicos con
 *   códigos de restablecimiento de contraseña u otros mensajes relacionados
 *   con la autenticación del usuario.
 * <p>
 * INTEGRACIÓN:
 * Este servicio se conecta directamente con los repositorios de {@link ClienteRepo},
 * {@link AgenteVentasRepo}, {@link PersonalBodegaRepo} y {@link RecursosHumanos} garantizando una autenticación unificada para todos los roles.
 */
@Service
@RequiredArgsConstructor
public class AuthServicioImpl implements AuthServicio {

    private final ClienteRepo clienteRepo;
    private final AgenteVentasRepo agenteRepo;
    private final PersonalBodegaRepo personalRepo;
    private final RecursosHumanosRepo recursosHumanosRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailServicio emailServicio;
    private final ClienteServicio clienteServicio;
    private static final Logger logger = LoggerFactory.getLogger(AuthServicio.class);


    /**
     * Inicia sesión en el sistema autenticando al usuario por su email y contraseña.
     * <p>
     * Este método busca al usuario entre los distintos tipos de entidades (Cliente, Agente de Ventas, Personal de Bodega, Recursos Humanos).
     * Si las credenciales son válidas, se genera un token JWT con los datos del usuario autenticado.
     * @param dto DTO que contiene el email y la contraseña ingresados por el usuario
     * @return Token JWT generado si la autenticación es exitosa
     * @throws ElementoNoEncontradoException si el usuario no existe o no se encuentra un rol correspondiente
     * @throws ElementoIncorrectoException si la contraseña es incorrecta
     * @throws ResponseStatusException si las credenciales no coinciden con ningún usuario válido
     */
    @Override
    public TokenDto login(@NotNull LoginDto dto) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        try {
            String token;

            // Buscar al usuario en la tabla Cliente usando el email
            Optional<Cliente> clienteOpt = buscarPorEmail(
                    clienteRepo::findByUser_Email,
                    null,
                    dto.email());

            if (clienteOpt.isPresent()) {

                // Autenticar el cliente: validar estado de cuenta y contraseña
                Cliente cliente = autenticarUsuario(clienteOpt, dto.password())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

                // Generar y retornar el token JWT con los datos del cliente
                token = jwtUtils.generateToken(cliente.getId().toString(), generarTokenLogin(cliente));
                return new TokenDto(token);
            }

            // Buscar al usuario en Agente de Ventas usando dos métodos: por user.email o por email empresarial
            Optional<AgenteVentas> agenteOpt = buscarPorEmail(
                    agenteRepo::findByUser_Email,
                    agenteRepo::findByDatosLaborales_EmailEmpresarial,
                    dto.email());

            if (agenteOpt.isPresent()) {

                // Autenticar al agente
                AgenteVentas agenteVentas = autenticarUsuario(agenteOpt, dto.password())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

                // Generar y retornar token JWT
                token = jwtUtils.generateToken(String.valueOf(agenteVentas.getId()), generarTokenLogin(agenteVentas));
                return new TokenDto(token);
            }

            // Buscar en Personal de Bodega usando email personal o email empresarial
            Optional<PersonalBodega> personalOpt = buscarPorEmail(
                    personalRepo::findByUser_Email,
                    personalRepo::findByDatosLaborales_EmailEmpresarial,
                    dto.email());

            // Autenticar al personal de bodega
            if (personalOpt.isPresent()) {
                PersonalBodega personalBodega = autenticarUsuario(personalOpt, dto.password())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

                // Generar y retornar token JWT
                token = jwtUtils.generateToken(String.valueOf(personalBodega.getId()), generarTokenLogin(personalBodega));
                return new TokenDto(token);
            }

            // Buscar en Recursos Humanos usando email personal o empresarial
            Optional<RecursosHumanos> recursosOpt = buscarPorEmail(
                    recursosHumanosRepo::findByUser_Email,
                    recursosHumanosRepo::findByDatosLaborales_EmailEmpresarial,
                    dto.email());

            // Autenticar al RRHH
            if (recursosOpt.isPresent()) {
                RecursosHumanos recursosHumanos = autenticarUsuario(recursosOpt, dto.password())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

                // Generar y retornar token JWT
                token = jwtUtils.generateToken(String.valueOf(recursosHumanos.getId()), generarTokenLogin(recursosHumanos));
                return new TokenDto(token);
            }

            // Si no se encontró ninguna cuenta o las credenciales no son válidas
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, MensajeError.CREDENCIALES_INVALIDAS);

        } catch (SolicitarReactivacionException  e) {
            // Si capturamos esta excepción, mandamos un 403 FORBIDDEN y el mensaje
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }}


    @Override
    public TokenDto loginConGoogle(String credential) throws ElementoNoEncontradoException {

        // Validar y decodificar el token JWT de Google usando la biblioteca Google API
        GoogleIdToken.Payload payload = verificarTokenGoogle(credential);

        // Extraer el email del token
        String email = payload.getEmail();

        String token;

        // Buscar entre las distintas entidades
        Optional<Cliente> clienteOpt = buscarPorEmail(clienteRepo::findByUser_Email, null, email);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            token = jwtUtils.generateToken(cliente.getId().toString(), generarTokenLogin(cliente));
            return new TokenDto(token);
        }

        Optional<AgenteVentas> agenteOpt = buscarPorEmail(agenteRepo::findByUser_Email, agenteRepo::findByDatosLaborales_EmailEmpresarial, email);
        if (agenteOpt.isPresent()) {
            AgenteVentas agente = agenteOpt.get();
            token = jwtUtils.generateToken(agente.getId().toString(), generarTokenLogin(agente));
            return new TokenDto(token);
        }

        Optional<PersonalBodega> personalOpt = buscarPorEmail(personalRepo::findByUser_Email, personalRepo::findByDatosLaborales_EmailEmpresarial, email);
        if (personalOpt.isPresent()) {
            PersonalBodega personal = personalOpt.get();
            token = jwtUtils.generateToken(personal.getId().toString(), generarTokenLogin(personal));
            return new TokenDto(token);
        }

        Optional<RecursosHumanos> recursosOpt = buscarPorEmail(recursosHumanosRepo::findByUser_Email, recursosHumanosRepo::findByDatosLaborales_EmailEmpresarial, email);
        if (recursosOpt.isPresent()) {
            RecursosHumanos rh = recursosOpt.get();
            token = jwtUtils.generateToken(rh.getId().toString(), generarTokenLogin(rh));
            return new TokenDto(token);
        }

        throw new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO);
    }


    /**
     * Verifica el token recibido desde el frontend usando la API de Google.
     * @param tokenId Token JWT enviado por Google
     * @return Payload con los datos del usuario (como email)
     * @throws ElementoNoEncontradoException si el token es inválido
     */
    private GoogleIdToken.Payload verificarTokenGoogle(String tokenId) throws ElementoNoEncontradoException {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList("719752748978-o4ah1907npcfhqqb7eoh7ol49a8objga.apps.googleusercontent.com")) // ⚠️ Reemplazar con tu CLIENT_ID
                    .build();

            GoogleIdToken idToken = verifier.verify(tokenId);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO);
            }
        } catch (Exception e) {
            throw new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO);
        }
    }




    /**
     * Auténtica al usuario comparando la contraseña proporcionada con la almacenada.
     * @param usuarioOpt Usuario opcional que puede estar presente
     * @param password Contraseña ingresada por el usuario
     * @return El usuario autenticado si la contraseña coincide
     * @throws ElementoEliminadoException si la cuenta está eliminada
     * @throws ElementoIncorrectoException si la contraseña es incorrecta
     */
    private <T extends Persona> Optional<T> autenticarUsuario(Optional<T> usuarioOpt, String password) throws ElementoEliminadoException, ElementoIncorrectoException {

        // Verificar si la cuenta se encuentra
        if (usuarioOpt.isPresent()) {
            T usuario = usuarioOpt.get();

            // Verificar si la cuenta ha sido eliminada
            if (usuario.getUser().getEstadoCuenta() == EstadoCuenta.ELIMINADO) {

                // Solo si es un Cliente, lanzar excepción especial para reactivar
                if (usuario instanceof Cliente){
                    throw new SolicitarReactivacionException(MensajeError.CUENTA_ELIMINADA_SOLICITAR_REACTIVACION);
                }

                // Para otros usuarios (Agente, Personal, RRHH) simplemente cuenta eliminada
                throw new ElementoEliminadoException(MensajeError.CUENTA_ASOCIADA_ELIMINADA);
            }
            // Verificar si la contraseña ingresada es correcta
            if (passwordEncoder.matches(password, usuario.getUser().getPassword())) {
                return Optional.of(usuario);
            }else{
                throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);
            }
        }
        // Si no hay usuario, retornar vacío
        return Optional.empty();
    }


    /**
     * Busca una entidad por email usando dos funciones de búsqueda (principal y alternativa).
     * @param buscadorPrincipal Función principal para buscar por email
     * @param buscadorAlternativo Función alternativa si la búsqueda principal no encuentra resultados
     * @param email Email con el cual buscar la entidad
     * @return Optional con la entidad encontrada o vacío si ninguna función retorna resultados
     */
    private <T> Optional<T> buscarPorEmail(Function<String, Optional<T>> buscadorPrincipal,
                                           Function<String, Optional<T>> buscadorAlternativo,
                                           String email) {

        // Intentar buscar con la función principal
        Optional<T> entidad = buscadorPrincipal.apply(email);

        // Si encuentra resultado o no hay función alternativa, retornar
        return entidad.isPresent() || buscadorAlternativo == null
                ? entidad
                : buscadorAlternativo.apply(email); // Si no encontró, usar alternativa
    }


    /**
     * Genera un mapa con los datos necesarios para el token JWT de un usuario autenticado.
     * El mapa incluye el email, nombre y el rol del usuario según su clase específica.
     *
     * @param persona Instancia de una subclase de {@link Persona}, como {@link Cliente}, {@link AgenteVentas}, {@link PersonalBodega} o {@link RecursosHumanos}.
     * @return Un {@code Map<String, String>} que contiene el email, nombre y rol del usuario autenticado.
     * @throws ElementoNoEncontradoException Si la clase de la persona no tiene un rol asociado definido.
     */
    private Map<String, String> generarTokenLogin(Persona persona) throws ElementoNoEncontradoException {

        // Obtener el email del usuario desde la clase embebida User
        String email = persona.getUser().getEmail();

        // Obtener el nombre del usuario
        String nombre = persona.getNombre();

        // Mapa que asocia las clases concretas con su respectivo rol en formato ROLE_*
        Map<Class<?>, String> rolesPorClase = Map.of(
                Cliente.class, "ROLE_CLIENTE",
                AgenteVentas.class, "ROLE_AGENTE_VENTAS",
                PersonalBodega.class, "ROLE_PERSONAL_BODEGA",
                RecursosHumanos.class, "ROLE_RECURSOS_HUMANOS");

        // Obtener el rol correspondiente a la clase específica del objeto persona
        String rol = rolesPorClase.get(persona.getClass());

        // Validar que el rol exista; si no, lanzar excepción indicando que no se encontró el rol
        if (rol == null || rol.isEmpty()) {
            throw new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ROL);}

        // Retornar un mapa con los datos del token: email, nombre y rol
        return Map.of(
                "email", email,
                "nombre", nombre,
                "rol", rol);
    }


    /**
     * Inicia el proceso de restablecimiento de contraseña para un usuario registrado en Store-It.
     * <p>
     * Este método busca el email en las entidades Cliente, AgenteVentas y PersonalBodega.
     * Si encuentra al usuario, genera un código de restablecimiento único, lo guarda en su cuenta
     * y envía un correo con instrucciones de recuperación.
     * </p>
     * @param dto Objeto que contiene el email del usuario que desea restablecer su contraseña.
     * @throws ElementoNoEncontradoException Si no se encuentra ninguna cuenta asociada al email proporcionado.
     */
    @Override
    public void solicitarRestablecimientoPassword(SolicitudEmailDto dto) throws ElementoNoEncontradoException {

        // Buscar al usuario en los distintos repositorios
        Persona persona = buscarPersonaPorEmail(dto.email())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.EMAIL_NO_ENCONTRADO));

        // Generar código único de restablecimiento
        String codigoGenerado = generadorCodigo();

        // Crear objeto CodigoRestablecimiento y asignarlo al usuario
        CodigoRestablecimiento codigo = new CodigoRestablecimiento(codigoGenerado, LocalDateTime.now().plusMinutes(30));
        persona.getUser().setCodigoRestablecimiento(codigo);

        // Guardar cambios en la base de datos
        guardarUsuario(persona);

        // Construir cuerpo del mensaje / Build email body
        String cuerpo = """
            Tu código para restablecer la contraseña es: %s
        
            También puedes hacerlo desde: http://localhost:4200/login?codigo=%s&email=%s
            """.formatted(codigoGenerado, codigoGenerado, dto.email());


        // Crear DTO de correo / Create email DTO
        EmailDto email = new EmailDto(
                dto.email(),
                "Restablecimiento de contraseña",
                cuerpo);

        // Enviar el correo / Send email
        emailServicio.enviarCorreo(email);

        // Log de confirmación de envío
        logger.info("Se ha enviado un código de restablecimiento a {}", dto.email());
    }


    /**
     * Busca un usuario (cliente, agente o personal) por su email.
     * @param email Email a buscar
     * @return Optional de Persona
     */
    private Optional<Persona> buscarPersonaPorEmail(String email) {
        return clienteRepo.findByUser_Email(email)
                .map(cliente -> (Persona) cliente)
                .or(() -> agenteRepo.findByUser_Email(email).map(agente -> (Persona) agente))
                .or(() -> agenteRepo.findByDatosLaborales_EmailEmpresarial(email).map(agente -> (Persona) agente))
                .or(() -> personalRepo.findByDatosLaborales_EmailEmpresarial(email).map(personal -> (Persona) personal))
                .or(() -> personalRepo.findByUser_Email(email).map(personal -> (Persona) personal))
                .or(() -> recursosHumanosRepo.findByUser_Email(email).map(recursosHumanos -> (Persona) recursosHumanos))
                .or(() -> recursosHumanosRepo.findByDatosLaborales_EmailEmpresarial(email).map(recursosHumanos -> (Persona) recursosHumanos));
    }


    /**
     * Guarda el usuario actualizado según su tipo.
     * @param usuario Usuario a guardar
     */
    private void guardarUsuario(Persona usuario) {
        if (usuario instanceof Cliente cliente) {
            clienteRepo.save(cliente);
        } else if (usuario instanceof AgenteVentas agente) {
            agenteRepo.save(agente);
        } else if (usuario instanceof PersonalBodega personal) {
            personalRepo.save(personal);
        } else if (usuario instanceof RecursosHumanos rh) {
            recursosHumanosRepo.save(rh);}
    }


    /**
     * Genera un código aleatorio para restablecimiento de contraseña.
     * @return Código generado
     */
    private String generadorCodigo() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // Ejemplo simple: 6 caracteres
    }


    /**
     * Verifica si un código de restablecimiento enviado al correo es válido.
     *
     * @param dto DTO que contiene el email y el código a verificar
     * @throws ElementoIncorrectoException si el código es inválido o ha expirado
     */
    @Override
    public void verificarCodigoPassword (VerificarCodigoDto dto) throws ElementoIncorrectoException, ElementoNoEncontradoException {

        // Buscar al usuario por email
        Persona usuario = buscarPersonaPorEmail(dto.email())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.EMAIL_NO_ENCONTRADO));

        // Obtener el código de restablecimiento
        CodigoRestablecimiento codigoRestablecimiento = usuario.getUser().getCodigoRestablecimiento();

        // Validar existencia del código
        if (codigoRestablecimiento == null) {
            throw new ElementoNoEncontradoException(MensajeError.CODIGO_INVALIDO);}

        // Validar coincidencia de código y expiración
        if (!codigoRestablecimiento.getCodigoRestablecimiento().equals(dto.codigo()) ||
                codigoRestablecimiento.getFechaExpiracionCodigoRestablecimiento().isBefore(LocalDateTime.now())) {
            throw new ElementoNoEncontradoException(MensajeError.CODIGO_INVALIDO);}

        // Log de validación exitosa
        logger.info("Código de restablecimiento verificado correctamente para {}", dto.email());
    }


    /**
     * Actualiza la contraseña de un usuario después de la verificación exitosa del código.
     * <p>
     * Este método encripta la nueva contraseña y limpia el código de restablecimiento.
     * </p>
     * @param dto DTO que contiene el email del usuario y la nueva contraseña deseada.
     * @throws ElementoNoEncontradoException Si el email no corresponde a ningún usuario.
     */
    @Override
    public void actualizarPassword(ActualizarPasswordDto dto) throws ElementoNoEncontradoException {

        // Buscar al usuario por email
        Persona usuario = buscarPersonaPorEmail(dto.email())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.EMAIL_NO_ENCONTRADO));

        // Actualizar la contraseña (encriptándola)
        usuario.getUser().setPassword(passwordEncoder.encode(dto.nuevaPassword()));

        // Limpiar el código de restablecimiento
        usuario.getUser().setCodigoRestablecimiento(null);

        // Guardar los cambios
        guardarUsuario(usuario);

        // Log de éxito
        logger.info("Contraseña actualizada exitosamente para {}", dto.email());
    }


    @Override
    public void reactivarCuenta(String email)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {
        clienteServicio.restablecerCuenta(email);
    }



}