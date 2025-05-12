package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.EditarClienteDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.objects.enums.EstadoProducto;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.model.users.enums.TipoCliente;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.utils.EmailServicio;
import co.edu.uniquindio.service.utils.CloudinaryServicio;
import co.edu.uniquindio.service.users.ClienteServicio;
import co.edu.uniquindio.service.utils.PhoneServicio;
import co.edu.uniquindio.service.utils.ValidacionCuentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Implementación concreta del servicio {@link ClienteServicio} encargado de gestionar
 * las operaciones relacionadas con los clientes dentro del sistema Store-It.
 * <p>
 * Esta clase integra funcionalidades como:
 * <ul>
 *     <li>Registro de nuevos clientes con validación de correo, teléfono e imagen de perfil.</li>
 *     <li>Codificación de contraseñas y envío de correos electrónicos con códigos de activación.</li>
 *     <li>Eliminación lógica de clientes considerando sus contratos y productos activos.</li>
 *     <li>Actualización de información personal y profesional del cliente.</li>
 * </ul>
 * <p>
 * Además, incorpora servicios externos como:
 * <ul>
 *     <li>{@code Cloudinary} para la gestión de imágenes.</li>
 *     <li>{@code libphonenumber} para la validación y formateo de teléfonos.</li>
 * </ul>
 * <p>
 * La configuración necesaria se gestiona a través del archivo {@code application.yml}.
 * @see ClienteRepo
 * @see ClienteMapper
 * @see CloudinaryServicio
 * @see EmailServicio
 * @see PhoneServicio
 * @see ValidacionCuentaServicio
 *
 */
@Service // Indica que esta clase es un componente de servicio en Spring y puede ser inyectado donde se necesite
@RequiredArgsConstructor // Genera automáticamente un constructor con todos los atributos finales (clienteRepo, clienteMapper)
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepo clienteRepo;
    private final ClienteMapper clienteMapper;
    private final CloudinaryServicio cloudinaryServicio;
    private final PasswordEncoder passwordEncoder;
    private final EmailServicio emailServicio;
    private final PhoneServicio phoneServicio;
    private final ValidacionCuentaServicio validacionCuentaServicio;
    private static final Logger logger = LoggerFactory.getLogger(ClienteServicioImpl.class);
    private static final String IMAGEN_URL = "https://res.cloudinary.com/dehltwwbu/image/upload/v1745680101/PerfilErroError_pgaehq.jpg";


    /**
     * Registra un nuevo cliente en el sistema Store-It.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Válida que el correo electrónico y los teléfonos proporcionados no estén registrados previamente.</li>
     *     <li>Sube la imagen de perfil proporcionada o asigna una imagen de perfil por defecto si no se especifica una.</li>
     *     <li>Encripta la contraseña antes de almacenarla en la base de datos.</li>
     *     <li>Convierte el objeto {@link CrearClienteDto} a una entidad de tipo {@link Cliente}.</li>
     *     <li>Formatea correctamente el número de teléfono principal y el secundario (si existe) de acuerdo al país de origen.</li>
     *     <li>Asigna la imagen de perfil y la contraseña encriptada al cliente.</li>
     *     <li>Guarda la información del cliente en la base de datos.</li>
     *     <li>Envía un correo electrónico de bienvenida al cliente registrado.</li>
     *     <li>Registra en los logs el evento de registro de un nuevo cliente.</li>
     * </ul>
     *
     * @param cuenta Objeto de tipo {@link CrearClienteDto} que contiene los datos necesarios para registrar al cliente.
     * @throws ElementoRepetidoException Si el correo electrónico o alguno de los teléfonos ya están registrados en el sistema.
     * @throws ElementoIncorrectoException Si la información proporcionada no cumple con las validaciones establecidas.
     * @throws ElementoNulosException Si se detectan campos obligatorios que han sido enviados como nulos.
     * @throws ElementoNoEncontradoException Si no se puede asociar correctamente la ubicación (país) del cliente.
     */
    @Override
    public void registrarCliente(CrearClienteDto cuenta)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException {

        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentaServicio.validarEmailNoRepetido(cuenta.user().email());
        validacionCuentaServicio.validarTelefonoNoRepetido(cuenta.telefono());
        // El teléfono secundario puede estar nulo, pues en la lógica lo maneja
        validacionCuentaServicio.validarTelefonoNoRepetido(cuenta.telefonoSecundario());

        // 2. Subimos imagen de perfil o asignamos una por defecto
        String urlImagen;
        if (cuenta.imagenPerfil() != null && !cuenta.imagenPerfil().isEmpty()) {
            urlImagen = cloudinaryServicio.uploadImage(cuenta.imagenPerfil());
        } else {
            urlImagen = IMAGEN_URL;}

        // 3. Encriptamos contraseña
        String passwordEncriptada = passwordEncoder.encode(cuenta.user().password());

        // 4. Convertimos DTO a entidad
        Cliente cliente = clienteMapper.toEntity(cuenta);

        // 5. Formateamos teléfonos
        cliente.setTelefono(phoneServicio.obtenerTelefonoFormateado(cliente.getTelefono(), cuenta.codigoPais()));

        // Formateamos el teléfono secundario si no esta nulo.
        if (cliente.getTelefonoSecundario() != null && !cliente.getTelefonoSecundario().isEmpty()) {
            cliente.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(cliente.getTelefonoSecundario(), cuenta.codigoPaisSecundario()));}

        // 5. Asignamos imagen y contraseña encriptada
        cliente.setImagenPerfil(urlImagen);
        cliente.getUser().setPassword(passwordEncriptada);

        // 6. Guardamos en la base de datos
        clienteRepo.save(cliente);

        // Definimos el asunto del correo
        String asunto = "Bienvenido a Store-It - Gestión de Bodegas";

        // Definimos el cuerpo del mensaje
        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "¡Te damos la más cordial bienvenida a Store-It!\n\n" +
                        "A partir de ahora, cuentas con una solución confiable y eficiente para gestionar el almacenamiento de tus productos.\n" +
                        "Nuestro equipo está listo para acompañarte en cada etapa y asegurarnos de que tengas la mejor experiencia con nuestros servicios.\n\n" +
                        "Si tienes alguna duda o necesitas asistencia, no dudes en contactarnos.\n\n" +
                        "Gracias por confiar en nosotros.\n\n" +
                        "Saludos cordiales,\n" +
                        "El equipo de Store-It",
                cliente.getNombre());

        // Enviamos el correo electrónico
        enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);

        // Creamos el logger del registro del cliente
        logger.info("Se ha registrado un nuevo cliente: {}.",
                cliente.getUser().getEmail());
    }


    /**
     * Recupera un cliente desde él {@link ClienteRepo} usando su ID.
     * Si el cliente no existe, lanza una excepción personalizada.
     *
     * @param id ID único del cliente a buscar.
     * @return El objeto {@link Cliente} correspondiente al ID proporcionado.
     * @throws ElementoNoEncontradoException Si no se encuentra ningún cliente con el ID dado.
     */
    private Cliente obtenerClientePorId(Long id) throws ElementoNoEncontradoException {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new
                        ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }


    /**
     * Recupera un cliente desde él {@link ClienteRepo} usando su correo electrónico.
     * Si no se encuentra un cliente con el correo proporcionado, lanza una excepción personalizada.
     *
     * @param email Correo electrónico del cliente a buscar.
     * @return El objeto {@link Cliente} correspondiente al correo electrónico proporcionado.
     * @throws ElementoNoEncontradoException Si no se encuentra ningún cliente con el correo dado.
     */
    private Cliente obtenerClientePorEmail(String email) throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + email));
    }


    /**
     * Envía un correo electrónico con el destinatario, asunto y cuerpo especificados.
     * @param emailDestinatario la dirección de correo electrónico del destinatario
     * @param asunto el asunto del correo electrónico
     * @param cuerpo el contenido del cuerpo del correo electrónico
     */
    private void enviarEmail(String emailDestinatario, String asunto, String cuerpo){
        // Se crea un DTO de email con su estructura para enviarse al receptor
        EmailDto emailDto = new EmailDto(emailDestinatario, asunto, cuerpo);
        // Se envía el email del destinatario.
        emailServicio.enviarCorreo(emailDto);
    }


    /**
     * Modifica la información de un cliente en la base de datos.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Busca al cliente utilizando su ID.</li>
     *     <li>Actualiza los datos del cliente con la información proporcionada en el DTO.</li>
     *     <li>Formatea y actualiza los números de teléfono si se proporcionaron nuevos valores.</li>
     *     <li>Sube y actualiza la imagen de perfil del cliente si se proporciona una nueva imagen.</li>
     *     <li>Guarda los cambios actualizados del cliente en la base de datos.</li>
     * </ul>
     *
     * @param cuenta Objeto de tipo {@link EditarClienteDto} que contiene los nuevos datos del cliente a actualizar.
     * @throws ElementoNoEncontradoException Si no se encuentra el cliente en la base de datos.
     * @throws ElementoIncorrectoException Si alguno de los datos proporcionados es incorrecto o no válido.
     * @throws ElementoNulosException Si alguno de los campos necesarios para la actualización está nulo o vacío.
     */
    @Override
    public void editarCliente(EditarClienteDto cuenta)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoNulosException {

        // Busca al cliente en la base de datos por su cédula
        Cliente cliente = obtenerClientePorId(cuenta.id());

        // Actualiza los datos del cliente con la información del DTO
        clienteMapper.toEntity(cuenta, cliente);

        // Verifica si se proporcionó un nuevo teléfono y lo formatea
        if (cuenta.telefono() != null && !cuenta.telefono().isEmpty()) {
            cliente.setTelefono(phoneServicio.obtenerTelefonoFormateado(cuenta.telefono(), cliente.getUbicacion().getPais()));}

        // Verifica si se proporcionó un teléfono secundario y lo formatea
        if (cuenta.telefonoSecundario() != null && !cuenta.telefonoSecundario().isEmpty()) {
            cliente.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(cuenta.telefonoSecundario(), cliente.getUbicacion().getPais()));}

        // Sí se proporcionó una nueva imagen
        if (cuenta.imagenPerfil() != null && !cuenta.imagenPerfil().isEmpty()) {
            // Verificar si la imagen actual no es la imagen por defecto
            if (!cliente.getImagenPerfil().equals(IMAGEN_URL)) {
                // Eliminar la imagen anterior del cliente
                cloudinaryServicio.eliminarImagen(cliente.getImagenPerfil());
            }
            // Subir la nueva imagen de perfil y actualizarla
            cliente.setImagenPerfil(cloudinaryServicio.uploadImage(cuenta.imagenPerfil()));
        }


        // Guarda los cambios actualizados en la base de datos
        clienteRepo.save(cliente);

        // Registra un mensaje en los logs con el ID del cliente modificado exitosamente
        logger.warn("Cuenta del cliente con ID {} ha sido modificada exitosamente.", cuenta.id());
    }


    /**
     * Elimina un cliente de la base de datos de manera lógica, cambiando su estado a "eliminado".
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Obtiene el cliente utilizando un método reutilizable.</li>
     *     <li>Válida la contraseña ingresada por el usuario.</li>
     *     <li>Verifica si el cliente tiene contratos activos o productos en bodega que impidan su eliminación.</li>
     *     <li>Envía un correo al cliente informando sobre la eliminación de su cuenta.</li>
     *     <li>Finalmente, elimina al cliente de la base de datos.</li>
     * </ul>
     *
     * @param dto Objeto de tipo {@link EliminarClienteDto} que contiene la información necesaria para eliminar al cliente,
     *            como su ID y la contraseña para verificar la autenticidad de la acción.
     * @throws ElementoNoEncontradoException Si el cliente no existe en el sistema.
     * @throws ElementoIncorrectoException Si la contraseña ingresada no coincide con la registrada en el sistema.
     * @throws ElementoAunEnUsoException Si el cliente tiene contratos activos o productos en bodega, lo que impide su eliminación.
     */
    @Override
    public void eliminarCliente(EliminarClienteDto dto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException {

        // Obtener el cliente usando el método reutilizable
        Cliente cliente = obtenerClientePorId(dto.id());

        // Validar la contraseña ingresada
        if (!passwordEncoder.matches(dto.password(), cliente.getUser().getPassword())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);}

        // Verifica si el cliente tiene contratos aún activos (ni finalizados ni cancelados).
        if (cliente.getContratos().stream()
                .anyMatch(contrato ->
                        contrato.getEstadoContrato() != EstadoContrato.FINALIZADO &&
                                contrato.getEstadoContrato() != EstadoContrato.CANCELADO)) {
            throw new ElementoAunEnUsoException(MensajeError.CLIENTE_CONTRATOS_ACTIVOS);}

        // Verifica si el cliente tiene productos aun en bodega.
        if (cliente.getProductos().stream()
                .anyMatch(producto ->
                        producto.getEstadoProducto() != EstadoProducto.RETIRADO)) {
            throw new ElementoAunEnUsoException(MensajeError.CLIENTE_PRODUCTOS_EN_BODEGA);}

        // Definimos el asunto del correo
        String asunto = "Confirmación de eliminación de cuenta - Store-It";

        // Definimos el cuerpo del correo
        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "Te informamos que tu cuenta en Store-It ha sido eliminada exitosamente.\n\n" +
                        "Si tú no solicitaste esta acción o consideras que se trata de un error, por favor contáctanos de inmediato para brindarte asistencia.\n\n" +
                        "Gracias por haber sido parte de Store-It.\n\n" +
                        "Saludos cordiales,\n" +
                        "El equipo de Store-It",
                cliente.getNombre());

        // Enviamos el correo
        enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);

        // Eliminar al cliente
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ELIMINADO);
        clienteRepo.save(cliente);

        logger.warn("Cuenta del cliente con ID {} eliminada exitosamente.",dto.id());
    }


    /**
     * Obtiene un cliente a partir de su ID y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el cliente correspondiente al ID proporcionado.</li>
     *     <li>Convierte la entidad {@link Cliente} a un objeto DTO de tipo {@link ClienteDto}.</li>
     * </ul>
     *
     * @param id El ID del cliente que se desea obtener.
     * @return {@link ClienteDto} Objeto DTO con la información del cliente.
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el ID proporcionado.
     */
    @Override
    public ClienteDto obtenerPorId(Long id) throws ElementoNoEncontradoException {
        // Obtiene el cliente por ID utilizando el método reutilizable
        return clienteMapper.toDTO(obtenerClientePorId(id));
    }


    /**
     * Obtiene un cliente a partir de su correo electrónico y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el cliente correspondiente al correo electrónico proporcionado.</li>
     *     <li>Convierte la entidad {@link Cliente} a un objeto DTO de tipo {@link ClienteDto}.</li>
     * </ul>
     *
     * @param email El correo electrónico del cliente que se desea obtener.
     * @return {@link ClienteDto} Objeto DTO con la información del cliente.
     * @throws ElementoNoEncontradoException Si no se encuentra un cliente con el email proporcionado.
     */
    @Override
    public ClienteDto obtenerPorEmail(String email) throws ElementoNoEncontradoException {
        // Obtiene el cliente por email utilizando el método reutilizable
        return clienteMapper.toDTO(obtenerClientePorEmail(email));
    }


    /**
     * Actualiza la información del usuario (correo electrónico y contraseña) de un cliente existente.
     * <p>
     * Este método verifica que la contraseña proporcionada sea la correcta, valida que el nuevo correo electrónico
     * no esté ya registrado en otro cliente, y asegura que la nueva contraseña sea diferente de la actual.
     * Si todas las validaciones pasan, se actualizan los datos del usuario en la base de datos.
     *
     * @param cuenta Objeto DTO que contiene el ID del cliente, la contraseña actual, el nuevo correo electrónico
     *               y la nueva contraseña que se desea establecer.
     * @throws ElementoNoEncontradoException Si no se encuentra el cliente con el ID proporcionado.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada es incorrecta o si la nueva contraseña es igual a la anterior.
     * @throws ElementoRepetidoException Si el correo electrónico proporcionado ya está registrado en otro cliente.
     */
    @Override
    public void editarUserCliente(EditarUserPersona cuenta)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoRepetidoException {

        // Obtiene el cliente mediante su ID
        Cliente cliente = obtenerClientePorId(cuenta.id());

        // Bandera para saber si hubo cambios
        boolean cambiosRealizados = false;
        String emailAnterior = cliente.getUser().getEmail(); // Guardamos el email actual

        // Verificar si la contraseña proporcionada coincide con la almacenada
        if (!cliente.getUser().getPassword().equals(cuenta.password())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);
        }

        // Verificar si la contraseña ha sido modificada
        if (cuenta.passwordModificado() != null && !cuenta.passwordModificado().isEmpty()) {
            if (cuenta.passwordModificado().equals(cuenta.password())) {
                throw new ElementoIncorrectoException(MensajeError.PASSWORD_REPETIDO);
            }
            // Actualizar la contraseña
            String passwordEncriptada = passwordEncoder.encode(cuenta.passwordModificado());
            cliente.getUser().setPassword(passwordEncriptada);
            cambiosRealizados = true;
        }

        // Verificar si el email ha sido modificado
        if (cuenta.email() != null && !cuenta.email().isEmpty() && !cuenta.email().equals(emailAnterior)) {
            validacionCuentaServicio.validarEmailNoRepetido(cuenta.email());
            cliente.getUser().setEmail(cuenta.email());
            cambiosRealizados = true;
        }

        // Si hubo cambios, enviamos los correos de notificación
        if (cambiosRealizados) {
            String asunto = "Actualización de credenciales - Store-It";

            // Construir el cuerpo del mensaje
            String cuerpo = String.format(
                    "Hola %s,\n\n" +
                            "Te informamos que se han actualizado exitosamente tus credenciales de acceso en Store-It.\n\n" +
                            "A continuación te proporcionamos tus nuevos datos de acceso:\n" +
                            "- Nuevo correo electrónico: %s\n" +
                            "- Nueva contraseña: %s\n\n" +
                            "Si no reconoces esta modificación, te pedimos que nos contactes de inmediato para proteger tu cuenta.\n\n" +
                            "Gracias por confiar en Store-It.\n\n" +
                            "Saludos cordiales,\n" +
                            "Equipo de Store-It",
                    cliente.getNombre(),
                    cliente.getUser().getEmail(),
                    cliente.getUser().getPassword()
            );

            // Si el correo fue cambiado, primero notificamos al correo anterior
            if (!emailAnterior.equals(cliente.getUser().getEmail())) {
                enviarEmail(emailAnterior, asunto, cuerpo);
            }

            // Luego notificamos al nuevo correo
            enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);
        }

        // Guardar los cambios en el repositorio
        clienteRepo.save(cliente);

        logger.warn("La cuenta con ID {} ha modificado su user", cuenta.id());
    }


    /**
     *
     * <p>Restaura la cuenta de un cliente si su estado actual es ELIMINADO.</p>
     *
     * <p>Este método obtiene el cliente asociado al correo electrónico proporcionado.
     * Si el estado de la cuenta no es ELIMINADO, lanza una excepción indicando que
     * la cuenta no puede ser restaurada. En caso contrario, cambia el estado a ACTIVO
     * y guarda el cambio en la base de datos.</p>
     * @param email Correo electrónico del cliente cuya cuenta se desea restaurar.
     * @throws ElementoNoEncontradoException si no se encuentra un cliente con ese correo.
     * @throws ElementoIncorrectoException si la cuenta no está eliminada y no puede ser restaurada.
     */
    @Override
    public void restablecerCuenta(String email) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        // Obtenemos el Cliente mediante su email
        Cliente cliente = obtenerClientePorEmail(email);

        // Verificamos si la cuenta del cliente se encuentra eliminada
        if (!cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
            throw new ElementoIncorrectoException(MensajeError.CUENTA_NO_ELIMINADA);}

        // Cambiamos el estado del cliente volviendo activarlo.
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteRepo.save(cliente);

        String asunto = "Reactivación de cuenta - Store-It";

        String cuerpo = String.format(
                "Hola %s,\n\n" +
                        "Te informamos que tu cuenta en Store-It ha sido reactivada exitosamente.\n\n" +
                        "Ahora puedes volver a acceder a todos nuestros servicios sin inconvenientes.\n" +
                        "Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.\n\n" +
                        "¡Nos alegra tenerte de vuelta!\n\n" +
                        "Saludos cordiales,\n" +
                        "Equipo Store-It",
                cliente.getNombre());

        enviarEmail(cliente.getUser().getEmail(), asunto, cuerpo);

        logger.info("Cuenta restaurada exitosamente para el cliente con email {}", email);
    }


    /**
     * Listar clientes con filtros opcionales por país, ciudad y tipo de usuario, y aplica paginación.
     *<p>
     * <ul>
     * <li> Crea una instancia de {@code Pageable}, que indica el número de página y
     * el tamaño por página para realizar la consulta paginada en base de datos.
     * <li> Inicializa {@code Specification} de búsqueda como null. A partir de ahí,
     * se le van agregando condiciones según los parámetros que no estén vacíos o nulos.</li>
     *
     * @param pais         (Opcional) País de residencia del cliente.
     * @param ciudad       (Opcional) Ciudad de residencia del cliente.
     * @param tipoCliente  (Opcional) Tipo de cuenta asociado.
     * @param pagina       Número de página a consultar (inicia en 0).
     * @param size         Cantidad de elementos por página.
     * @return Lista de objetos ClienteDto que cumplen con los filtros proporcionados y la paginación.
     */
    @Override
    public List<ClienteDto> listarClientes(String pais, String ciudad,
                                           TipoCliente tipoCliente , int pagina, int size) {
        // Construir objeto Pageable para aplicar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Crear predicado dinámico con filtros opcionales
        Specification<Cliente> spec = Specification.where(null);

        // Si se proporciona un país, añade un filtro para buscar solo los clientes cuya ubicación coincida con ese país.
        if (pais != null && !pais.isEmpty()) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(root.get("ubicacion").get("pais"), pais));}

        // Filtra por ciudad si se proporcionó dicho valor, aplicándolo sobre el atributo ubicación ciudad
        if (ciudad != null && !ciudad.isEmpty()) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(root.get("ubicacion").get("ciudad"), ciudad));}

        // Filtrar por tipoCliente si se proporcionó dicho valor
        if (tipoCliente != null) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(root.get("tipoCliente"), tipoCliente));}

        // Obtener la lista de clientes paginados y filtrados
        Page<Cliente> clientesPage = clienteRepo.findAll(spec, pageable);

        // Convertir la lista de clientes a DTO
        return clientesPage.getContent().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }









}