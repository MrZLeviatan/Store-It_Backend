package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.AgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.CrearAgenteVentasDto;
import co.edu.uniquindio.dto.users.agenteVentas.EditarAgenteVentasDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.mapper.objects.ContratoMapper;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.mapper.users.AgenteVentasMapper;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.users.RecursosHumanos;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.repository.objects.ContratoRepo;
import co.edu.uniquindio.repository.objects.SedeRepo;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.service.users.AgenteVentaServicio;
import co.edu.uniquindio.service.utils.CloudinaryServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import co.edu.uniquindio.service.utils.PhoneServicio;
import co.edu.uniquindio.service.utils.ValidacionCuentaServicio;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación concreta del servicio {@link AgenteVentaServicio} encargado de gestionar
 * las operaciones relacionadas con los agentes de ventas dentro del sistema Store-It.
 * <p>
 * Esta clase integra funcionalidades como:
 * <ul>
 *     <li>Registro de nuevos agentes de ventas con validación de correo, teléfono e imagen de perfil.</li>
 *     <li>Eliminación lógica de agentes de ventas considerando sus contratos.</li>
 *     <li>Actualización de información personal y profesional del agente de ventas.</li>
 * </ul>
 * <p>
 * Además, incorpora servicios externos como:
 * <ul>
 *     <li>{@code Cloudinary} para la gestión de imágenes.</li>
 *     <li>{@code libphonenumber} para la validación y formateo de teléfonos.</li>
 * </ul>
 * <p>
 * La configuración necesaria se gestiona a través del archivo {@code application.yml}.
 * @see AgenteVentasRepo
 * @see AgenteVentasMapper
 * @see CloudinaryServicio
 * @see EmailServicio
 * @see PhoneServicio
 * @see ValidacionCuentaServicio
 */
@Service // Indica que esta clase es un componente de servicio en Spring y puede ser inyectado donde se necesite
@RequiredArgsConstructor // Genera automáticamente un constructor con todos los atributos finales.
@RequestMapping("/api/agente-ventas")
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación Bearer
public class AgenteVentaServicioImpl implements AgenteVentaServicio {

    private final AgenteVentasRepo agenteVentasRepo;
    private final AgenteVentasMapper agenteVentasMapper;
    private final CloudinaryServicio cloudinaryServicio;
    private final PasswordEncoder passwordEncoder;
    private final EmailServicio emailServicio;
    private final PhoneServicio phoneServicio;
    private final ValidacionCuentaServicio validacionCuentaServicio;
    private final SedeRepo sedeRepo;
    private final SedeMapper sedeMapper;
    private final ContratoRepo contratoRepo;
    private final ContratoMapper contratoMapper;
    private static final Logger logger = LoggerFactory.getLogger(AgenteVentaServicioImpl.class);


    /**
     * Registra un nuevo agente de ventas en el sistema utilizando un DTO de entrada.
     * Este método realiza las siguientes acciones principales:
     * <ol>
     *   <li>Válida que el correo electrónico no haya sido registrado previamente.</li>
     *   <li>Sube la imagen de perfil del agente de ventas a Cloudinary.</li>
     *   <li>Encripta la contraseña proporcionada por el agente de ventas.</li>
     *   <li>Convierte el DTO recibido en una entidad agente de ventas.</li>
     *   <li>Valida y formatea los números telefónicos (principal y secundario).</li>
     *   <li>Asigna la imagen de perfil y la contraseña encriptada a la entidad.</li>
     *   <li>Guarda al agente de ventas en la base de datos.</li>
     *   <li>Envía un correo al email del agente de ventas.</li>
     * </ol>
     *
     * @param agente DTO con los datos del agente de ventas a registrar.
     * @throws ElementoRepetidoException    Si el correo electrónico ya está registrado.
     * @throws ElementoIncorrectoException  Si los datos proporcionados no cumplen con las validaciones esperadas.
     * @throws ElementoNulosException       Si se detectan campos nulos obligatorios.
     * @throws ElementoNoEncontradoException Si no se encuentra información requerida durante el proceso.
     */
    @Override
    public void registrarAgenteVentas(CrearAgenteVentasDto agente)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException, ElementoNoValido {

        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentaServicio.validarEmailNoRepetido(agente.user().email());
        validacionCuentaServicio.validarEmailEmpresarialNoRepetido(agente.datosLaborales().emailEmpresarial());
        validacionCuentaServicio.validarTelefonoNoRepetido(agente.telefono());
        validacionCuentaServicio.validarTelefonoNoRepetido(agente.telefonoSecundario());

        // 2. Subimos imagen y encriptamos contraseña
        String urlImagen = cloudinaryServicio.uploadImage(agente.imagenPerfil());
        String passwordEncriptada = passwordEncoder.encode(agente.user().password());



        // 3. Convertimos DTO a entidad
        AgenteVentas agenteVentas = agenteVentasMapper.toEntity(agente);


        // Asignamos la sede persistida para evitar DetachedEntityException
        Sede sede = sedeRepo.findById(agente.idSede())
                .orElseThrow(() -> new ElementoNulosException(MensajeError.SEDE_NO_ENCONTRADA));
        agenteVentas.setSede(sede);

        // 4. Formateamos teléfonos
        agenteVentas.setTelefono(phoneServicio.obtenerTelefonoFormateado(agenteVentas.getTelefono(), agente.codigoTelefono()));
        if (agenteVentas.getTelefonoSecundario() != null && !agenteVentas.getTelefonoSecundario().isEmpty()) {
            agenteVentas.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(agenteVentas.getTelefonoSecundario(), agente.codigoTelefonoSecundario()));
        }

        // 5. Asignamos imagen y contraseña encriptada
        agenteVentas.setImagenPerfil(urlImagen);
        agenteVentas.getUser().setPassword(passwordEncriptada);

        // 6. Guardamos en la base de datos
        agenteVentasRepo.save(agenteVentas);

        String asunto =  "Bienvenido a Store-It";
        String cuerpo = String.format( // Cuerpo del correo de bienvenida
                "Hola %s,\n\n" +
                        "¡Bienvenido a Store-It!\n" +
                        "Nos complace contar contigo como parte de nuestro equipo de ventas.\n\n" +
                        "Te deseamos muchos éxitos en esta nueva etapa.\n\n" +
                        "Saludos cordiales,\n" +
                        "Equipo Store-It",
                agenteVentas.getNombre());

        // Se envía un correo de bienvenida al agente de ventas
        enviarEmail(agenteVentas.getUser().getEmail(), asunto, cuerpo);

        logger.info("Se ha registrado un nuevo agente de ventas: {}.",
                agenteVentas.getUser().getEmail());
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
     * Modifica la información de un agente de ventas en la base de datos.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Busca al agente de ventas utilizando su ID.</li>
     *     <li>Actualiza los datos del agente de ventas con la información proporcionada en el DTO.</li>
     *     <li>Formatea y actualiza los números de teléfono si se proporcionaron nuevos valores.</li>
     *     <li>Sube y actualiza la imagen de perfil del agente de ventas si se proporciona una nueva imagen.</li>
     *     <li>Guarda los cambios actualizados del agente de ventas en la base de datos.</li>
     * </ul>
     *
     * @param agente Objeto de tipo {@link EditarAgenteVentasDto} que contiene los nuevos datos del agente de ventas a actualizar.
     * @throws ElementoNoEncontradoException Si no se encuentra el agente de ventas en la base de datos.
     * @throws ElementoIncorrectoException Si alguno de los datos proporcionados es incorrecto o no válido.
     * @throws ElementoNulosException Si alguno de los campos necesarios para la actualización está nulo o vacío.
     */
    @Override
    public void editarAgenteVentas(EditarAgenteVentasDto agente)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNulosException {

        AgenteVentas agenteVentas = obtenerAgentePorId(agente.id());

        // Actualiza los datos del cliente con la información del DTO
        agenteVentasMapper.toEntity(agente, agenteVentas);

        // Verifica si se proporcionó un nuevo teléfono y lo formatea
        if (agente.telefono() != null && !agente.telefono().isEmpty()) {
            agenteVentas.setTelefono(phoneServicio.obtenerTelefonoFormateado(agente.telefono(), agente.codigoPais()));}

        // Verifica si se proporcionó un teléfono secundario y lo formatea
        if (agente.telefonoSecundario() != null && !agente.telefonoSecundario().isEmpty()) {
            agenteVentas.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(agente.telefonoSecundario(), agente.codigoPaisSecundario()));}

        // Si se proporcionó una nueva imagen, se sube y actualiza
        if (agente.imagenPerfil() != null && !agente.imagenPerfil().isEmpty()) {
            cloudinaryServicio.eliminarImagen(agenteVentas.getImagenPerfil());
            agenteVentas.setImagenPerfil(cloudinaryServicio.uploadImage(agente.imagenPerfil()));
        }

        // Guarda los cambios actualizados en la base de datos
        agenteVentasRepo.save(agenteVentas);

        // Registra un mensaje en los logs con el ID del cliente modificado exitosamente
        logger.info("Cuenta del Agente con Ventas con ID {} ha sido modificada exitosamente.", agente.id());
    }

    @Override
    public SedeDto obtenerSedeAgente(Long id) throws ElementoNoEncontradoException {
        AgenteVentas agenteVentas = obtenerAgentePorId(id);
        Sede sede = sedeRepo.findById(agenteVentas.getSede().getId()).orElseThrow(()
                -> new ElementoNoEncontradoException(MensajeError.SEDE_NO_ENCONTRADA));
        return sedeMapper.toDto(sede);
    }


    @Override
    public List<AgenteVentasDto> obtenerAgentesVentas(Long idSede) {
        // Get the list of agents filtered by the sede ID
        List<AgenteVentas> agentes = agenteVentasRepo.findBySedeId((idSede));

        // Se convierte cada entidad a su DTO correspondiente
        // Each entity is mapped to its corresponding DTO
        return agentes.stream()
                .map(agenteVentasMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContratoDto> obtenerContratosPorAgenteVentas(AgenteVentasDto agenteVentasDto) {

        // Convertimos el DTO a entidad / Convert the DTO to entity
        AgenteVentas agenteVentas = agenteVentasMapper.toEntity(agenteVentasDto);

        // Buscamos los contratos asociados a ese agente / Find contracts related to the given agent
        List<Contrato> contratos = contratoRepo.findByAgenteVentas(agenteVentas);

        // Convertimos los contratos a DTO y retornamos / Convert contracts to DTO and return
        return contratos.stream()
                .map(contratoMapper::toDTO)
                .collect(Collectors.toList());
    }



    /**
     * Recupera un agente de ventas desde él {@code repository} usando su ID.
     * Si el agente de ventas no existe, lanza una excepción personalizada.
     *
     * @param id ID único del agente de ventas a buscar.
     * @return El objeto {@link AgenteVentas} correspondiente al ID proporcionado.
     * @throws ElementoNoEncontradoException Si no se encuentra ningún agente de ventas con el ID dado.
     */
    private AgenteVentas obtenerAgentePorId(Long id) throws ElementoNoEncontradoException {
        return agenteVentasRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }


    /**
     * Busca un agente de ventas por su correo electrónico utilizando dos estrategias de búsqueda.
     *
     * @param email Correo electrónico del agente de ventas.
     * @return AgenteVentas encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra el agente con el email dado.
     */
    private AgenteVentas obtenerAgentePorEmail(String email) throws ElementoNoEncontradoException {
        // Intentamos buscar primero por email exacto
        return agenteVentasRepo.findByUser_Email(email)
                .or(() -> agenteVentasRepo.findByDatosLaborales_EmailEmpresarial(email)) // Segunda estrategia: email empresarial
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }


    /**
     * Elimina un agente de ventas de la base de datos de manera lógica.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Obtiene el agente de ventas utilizando un método reutilizable.</li>
     *     <li>Válida la contraseña ingresada por el usuario.</li>
     *     <li>Verifica si el agente de ventas tiene contratos activos que impidan su eliminación.</li>
     *     <li>Envía un correo al agente de ventas informando sobre la eliminación de su cuenta.</li>
     *     <li>Finalmente, elimina al agente de ventas de la base de datos.</li>
     * </ul>
     * @throws ElementoNoEncontradoException Si el agente de ventas no existe en el sistema.
     * @throws ElementoIncorrectoException Si la contraseña ingresada no coincide con la registrada en el sistema.
     * @throws ElementoAunEnUsoException Si el agente de ventas tiene contratos activos o productos en bodega, lo que impide su eliminación.
     */
    @Override
    public void eliminarAgenteVentas(Long id)
            throws ElementoNoEncontradoException{

        // Obtener el agente de ventas usando el método reutilizable
        AgenteVentas agenteVentas = obtenerAgentePorId(id);

        // Verifica si el agente tiene contratos aún activos (ni finalizados ni cancelados).
        if (agenteVentas.getContratos().stream()
                .anyMatch(contrato ->
                        contrato.getEstadoContrato() != EstadoContrato.FINALIZADO &&
                                contrato.getEstadoContrato() != EstadoContrato.CANCELADO)) {
            throw new ElementoAunEnUsoException(MensajeError.AGENTE_CONTRATOS_ACTIVOS);}

        // Se notifica al agente de ventas.
        String asunto =  "Eliminación de cuenta - Store-It";
        String cuerpo = String.format
                        ("Hola " + agenteVentas.getNombre() + ",\n\n" +
                        "Te informamos que tu cuenta como Agente de Ventas en Store-It ha sido eliminada exitosamente.\n\n" +
                        "Si no reconoces esta acción o crees que se trata de un error, te pedimos que nos contactes de inmediato para tomar las medidas necesarias.\n\n" +
                        "Agradecemos tu tiempo y colaboración.\n\n" +
                        "Saludos cordiales,\n" +
                        "Equipo de Recursos Humanos - Store-It");

        enviarEmail(agenteVentas.getUser().getEmail(), asunto, cuerpo);

        // Eliminar al cliente y salvar los datos.
        agenteVentas.getUser().setEstadoCuenta(EstadoCuenta.ELIMINADO);
        agenteVentasRepo.save(agenteVentas);

        logger.info("Cuenta del Agente de Ventas con ID {} eliminada exitosamente.", id);
    }


    /**
     * Obtiene un agente de ventas a partir de su ID y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el agente de ventas correspondiente al ID proporcionado.</li>
     *     <li>Convierte la entidad {@link AgenteVentas} a un objeto DTO de tipo {@link AgenteVentasDto}.</li>
     * </ul>
     *
     * @param id El ID del agente de ventas que se desea obtener.
     * @return {@link AgenteVentasDto} Objeto DTO con la información del agente de ventas
     * @throws ElementoNoEncontradoException Si no se encuentra un agente de ventas con el ID proporcionado.
     */
    @Override
    public AgenteVentasDto obtenerAgenteVentasId(Long id) throws ElementoNoEncontradoException {
        return agenteVentasMapper.toDTO(obtenerAgentePorId(id));
    }


    /**
     * Obtiene un agente de ventas a partir de su correo electrónico y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el agente de ventas correspondiente al correo electrónico proporcionado.</li>
     *     <li>Convierte la entidad {@link AgenteVentas} a un objeto DTO de tipo {@link AgenteVentasDto}.</li>
     * </ul>
     *
     * @param email El correo electrónico del agente de ventas que se desea obtener.
     * @return {@link AgenteVentasDto} Objeto DTO con la información del agente de ventas.
     * @throws ElementoNoEncontradoException Si no se encuentra un agente de ventas con el email proporcionado.
     */
    @Override
    public AgenteVentasDto obtenerAgenteVentasEmail(String email) throws ElementoNoEncontradoException {
        return agenteVentasMapper.toDTO(obtenerAgentePorEmail(email));
    }


    /**
     * Actualiza la información del usuario (correo electrónico y contraseña) de un agente de ventas existente.
     * <p>
     * Este método verifica que la contraseña proporcionada sea la correcta, valida que el nuevo correo electrónico
     * no esté ya registrado en otro agente, y asegura que la nueva contraseña sea diferente de la actual.
     * Si todas las validaciones pasan, se actualizan los datos del usuario en la base de datos.
     *
     * @param agenteUser Objeto DTO que contiene el ID del cliente, la contraseña actual, el nuevo correo electrónico
     *               y la nueva contraseña que se desea establecer.
     * @throws ElementoNoEncontradoException Si no se encuentra el agente de ventas con el ID proporcionado.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada es incorrecta o si la nueva contraseña es igual a la anterior.
     * @throws ElementoRepetidoException Si el correo electrónico proporcionado ya está registrado en otro agente de ventas.
     */
    @Override
    public void editarUserAgenteVentas(EditarUserPersona agenteUser)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoRepetidoException {

        AgenteVentas agenteVentas = obtenerAgentePorId(agenteUser.id());

        boolean cambiosRealizados = false;
        boolean emailCambiado = false;
        String emailAnterior = agenteVentas.getUser().getEmail();

        // Verificar si la contraseña proporcionada coincide con la almacenada
        if (!agenteVentas.getUser().getPassword().equals(agenteUser.password())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);
        }

        // Verificar y actualizar contraseña nueva
        if (agenteUser.passwordModificado() != null && !agenteUser.passwordModificado().isEmpty()) {
            if (agenteUser.passwordModificado().equals(agenteUser.password())) {
                throw new ElementoIncorrectoException(MensajeError.PASSWORD_REPETIDO);
            }
            String passwordEncriptada = passwordEncoder.encode(agenteUser.passwordModificado());
            agenteVentas.getUser().setPassword(passwordEncriptada);
            cambiosRealizados = true;
        }

        // Verificar y actualizar email
        if (agenteUser.email() != null && !agenteUser.email().isEmpty()
                && !agenteUser.email().equals(agenteVentas.getUser().getEmail())) {
            validacionCuentaServicio.validarEmailNoRepetido(agenteUser.email());
            agenteVentas.getUser().setEmail(agenteUser.email());
            cambiosRealizados = true;
            emailCambiado = true;

        }

        if (cambiosRealizados) {
            // Definimos el asunto del correo
            String asunto = "Actualización de credenciales - Store-It (Agente de Ventas)";

            // Definimos el cuerpo del mensaje
            String cuerpo = String.format(
                    "Hola %s,\n\n" +
                            "Te informamos que se han actualizado exitosamente tus credenciales de acceso como Agente de Ventas en Store-It.\n\n" +
                            "A continuación te proporcionamos tus nuevos datos de acceso:\n" +
                            "- Nuevo correo electrónico: %s\n" +
                            "- Nueva contraseña: %s\n\n" +
                            "Si no reconoces esta modificación, te pedimos que nos contactes de inmediato para proteger tu cuenta.\n\n" +
                            "Gracias por formar parte de nuestro equipo.\n\n" +
                            "Saludos cordiales,\n" +
                            "Equipo de Recursos Humanos - Store-It",
                    agenteVentas.getNombre(),
                    agenteVentas.getUser().getEmail(),
                    agenteVentas.getUser().getPassword());

            // Enviar email ha ambos correos si el email fue cambiado
            if (emailCambiado) {
                enviarEmail(emailAnterior, asunto, cuerpo); // Enviar al correo anterior
            }
            enviarEmail(agenteVentas.getUser().getEmail(), asunto, cuerpo); // Enviar al nuevo correo
        }

        // Guardar los cambios en el repositorio
        agenteVentasRepo.save(agenteVentas);

        logger.warn("La cuenta con ID {} ha modificado su user", agenteUser.id());
    }



    /**
     * Lista los Agentes de Ventas aplicando filtros opcionales y paginación.
     *<p>
     *<ul>
     *<li> Crea una instancia de {@code Pageable}, que indica el número de página y
     *el tamaño por página para realizar la consulta paginada en base de datos.
     *<li> Inicializa {@code Specification} de búsqueda como null. A partir de ahí,
     *se le van agregando condiciones según los parámetros que no estén vacíos o nulos.</li>
     *
     * @param idSede ID de la sede a la que pertenece el agente (opcional).
     * @param fechaContratacion Fecha de contratación del agente (opcional).
     * @param tipoContrato Tipo de contrato laboral (opcional).
     * @param estadoContratoLaboral Estado del contrato laboral (opcional).
     * @param pagina Número de página para la paginación.
     * @param size Tamaño de página para la paginación.
     * @return Lista paginada de agentes de ventas en forma de DTO.
     */
    @Override
    public List<AgenteVentasDto> listarAgenteVentas(String idSede,
                                                    LocalDate fechaContratacion,
                                                    TipoContrato tipoContrato,
                                                    EstadoContratoLaboral estadoContratoLaboral,
                                                    int pagina, int size) {

        // Construir objeto Pageable para aplicar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Crear predicado dinámico con filtros opcionales
        Specification<AgenteVentas> spec = Specification.where(null);


        // Si se proporciona un, id de sede, filtrar por la sede del agente
        if (idSede != null && !idSede.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("sede").get("id"), Long.valueOf(idSede)));}

        // Si se proporciona una fecha de contratación, filtrar por dicha fecha
        if (fechaContratacion != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("fechaContratacion").as(LocalDate.class), fechaContratacion));}

        // Sí se proporciona un tipo de contrato, filtrar por ese tipo
        if (tipoContrato != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("tipoContrato"), tipoContrato));}

        // Sí se proporciona un estado de contrato, filtrar por ese estado
        if (estadoContratoLaboral != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("estadoContratoLaboral"), estadoContratoLaboral));}


        // Obtener la página de agentes que cumplen con los filtros
        Page<AgenteVentas> agentesPage = agenteVentasRepo.findAll(spec, pageable);

        // Convertir cada entidad AgenteVentas a su respectivo DTO
        return agentesPage.getContent().stream()
                .map(agenteVentasMapper::toDTO)
                .collect(Collectors.toList());
    }
}
