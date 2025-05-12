package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.users.common.EditarUserPersona;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.dto.users.personalBodega.*;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.mapper.objects.BodegaMapper;
import co.edu.uniquindio.mapper.users.PersonalBodegaMapper;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.users.PersonalBodega;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.model.users.enums.TipoCargo;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.service.users.PersonalBodegaServicio;
import co.edu.uniquindio.service.utils.CloudinaryServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import co.edu.uniquindio.service.utils.PhoneServicio;
import co.edu.uniquindio.service.utils.ValidacionCuentaServicio;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación concreta del servicio {@link PersonalBodegaDto} encargado de gestionar
 * las operaciones relacionadas con el personal de bodega dentro del sistema Store-It.
 * <p>
 * Esta clase integra funcionalidades como:
 * <ul>
 *     <li>Registro de nuevo personal con validación de correo, teléfono e imagen de perfil.</li>
 *     <li>Eliminación lógica del personal considerando sus contratos.</li>
 *     <li>Actualización de información personal y profesional del personal.</li>
 * </ul>
 * <p>
 * Además, incorpora servicios externos como:
 * <ul>
 *     <li>{@code Cloudinary} para la gestión de imágenes.</li>
 *     <li>{@code libphonenumber} para la validación y formateo de teléfonos.</li>
 * </ul>
 * <p>
 * La configuración necesaria se gestiona a través del archivo {@code application.yml}.
 * @see PersonalBodegaRepo
 * @see PersonalBodegaMapper
 * @see CloudinaryServicio
 * @see EmailServicio
 * @see PhoneServicio
 * @see ValidacionCuentaServicio
 */
@Service
@RequiredArgsConstructor // Genera automáticamente un constructor con todos los atributos finales.
public class PersonalBodegaServicioImpl implements PersonalBodegaServicio {


    private final PersonalBodegaRepo personalBodegaRepo;
    private final PersonalBodegaMapper personalBodegaMapper;
    private final CloudinaryServicio cloudinaryServicio;
    private final PasswordEncoder passwordEncoder;
    private final EmailServicio emailServicio;
    private final PhoneServicio phoneServicio;
    private final ValidacionCuentaServicio validacionCuentaServicio;
    private final BodegaRepo bodegaRepo;
    private final BodegaMapper bodegaMapper;
    private static final Logger logger = LoggerFactory.getLogger(PersonalBodegaServicioImpl.class);


    /**
     * Registra un nuevo miembro del personal de bodega en el sistema.
     *
     * <p>Este método realiza las siguientes acciones:</p>
     * <ol>
     *     <li>Válida que el correo electrónico personal, el correo empresarial y los teléfonos no estén registrados previamente.</li>
     *     <li>Sube la imagen de perfil del usuario a Cloudinary y encripta su contraseña.</li>
     *     <li>Convierte el DTO recibido a una entidad {@link PersonalBodega} utilizando MapStruct.</li>
     *     <li>Formatea los teléfonos con base en el país de la ubicación de la bodega.</li>
     *     <li>Asigna la imagen y la contraseña encriptada a la entidad.</li>
     *     <li>Guarda el nuevo personal de bodega en la base de datos.</li>
     *     <li>Envía un correo de bienvenida al nuevo personal.</li>
     * </ol>
     *
     * @param personal DTO con los datos necesarios para registrar un nuevo {@link PersonalBodega}.
     * @throws ElementoRepetidoException si alguno de los datos como el correo o teléfono ya está registrado.
     * @throws ElementoIncorrectoException si alguno de los datos no cumple con los criterios esperados.
     * @throws ElementoNulosException si alguno de los campos requeridos es nulo.
     * @throws ElementoNoEncontradoException si no se encuentra una bodega o dato relacionado al registrar.
     */
    @Override
    public void registrarPersonal(CrearPersonalBodegaDto personal)
            throws ElementoRepetidoException, ElementoIncorrectoException,
            ElementoNulosException, ElementoNoEncontradoException {

        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentaServicio.validarEmailNoRepetido(personal.user().email());
        validacionCuentaServicio.validarEmailEmpresarialNoRepetido(personal.datosLaborales().emailEmpresarial());
        validacionCuentaServicio.validarTelefonoNoRepetido(personal.telefono());
        validacionCuentaServicio.validarTelefonoNoRepetido(personal.telefonoSecundario());

        // 2. Subimos imagen y encriptamos contraseña
        String urlImagen = cloudinaryServicio.uploadImage(personal.imagenPerfil());
        String passwordEncriptada = passwordEncoder.encode(personal.user().password());

        // 3. Convertimos DTO a entidad
        PersonalBodega personalBodega = personalBodegaMapper.toEntity(personal);

        Bodega bodega = bodegaRepo.findById(personal.idBodega())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA));
        personalBodega.setBodega(bodega);

        // 4. Formateamos teléfonos
        personalBodega.setTelefono(phoneServicio.obtenerTelefonoFormateado(personalBodega.getTelefono(), personal.codigoTelefono()));
        if (personalBodega.getTelefonoSecundario() != null && !personalBodega.getTelefonoSecundario().isEmpty()) {
            personalBodega.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(personalBodega.getTelefonoSecundario(), personal.codigoTelefonoSecundario()));}

        // 5. Asignamos imagen y contraseña encriptada
        personalBodega.setImagenPerfil(urlImagen);
        personalBodega.getUser().setPassword(passwordEncriptada);

        // 6. Guardamos en la base de datos
        personalBodegaRepo.save(personalBodega);

        // Enviamos el correo de bienvenida al Personal de Bodega
        EmailDto emailDto = new EmailDto(
                personalBodega.getUser().getEmail(),
                "Bienvenido a Store-It",
                String.format("Hola %s,\n\n¡Bienvenido a Store-It!\nNos alegra tenerte como parte del equipo logístico de nuestra empresa.\n" +
                                "Tu labor es fundamental para asegurar el correcto funcionamiento de nuestras bodegas.\n\n" +
                                "Te deseamos muchos éxitos en esta nueva etapa.\n\nSaludos cordiales,\nEquipo Store-It",
                        personalBodega.getNombre()));
        emailServicio.enviarCorreo(emailDto);

        logger.info("Se ha registrado un nuevo Personal de Bodega con email: {} y cargo: {}.",
                personalBodega.getUser().getEmail(),
                personalBodega.getTipoCargo());
    }


    /**
     * Modifica la información de un personal de bodega en la base de datos.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Busca al personal de bodega utilizando su ID.</li>
     *     <li>Actualiza los datos del personal con la información proporcionada en el DTO.</li>
     *     <li>Formatea y actualiza los números de teléfono si se proporcionaron nuevos valores.</li>
     *     <li>Sube y actualiza la imagen de perfil del personal si se proporciona una nueva imagen.</li>
     *     <li>Guarda los cambios actualizados del personal en la base de datos.</li>
     * </ul>
     *
     * @param personalBodegaDto Objeto de tipo {@link EditarPersonalBodegaDto} que contiene los nuevos datos del personal de bodega a actualizar.
     * @throws ElementoNoEncontradoException Si no se encuentra el personal de bodega en la base de datos.
     * @throws ElementoIncorrectoException Si alguno de los datos proporcionados es incorrecto o no válido.
     * @throws ElementoNulosException Si alguno de los campos necesarios para la actualización está nulo o vacío.
     */
    @Override
    public void editarPersonal(EditarPersonalBodegaDto personalBodegaDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoNulosException {

        // Se obtiene el personal de bodega mediante su ID
        PersonalBodega personalBodega = obtenerPersonalPorId(personalBodegaDto.id());

        // Actualiza los datos del personal con la información del DTO
        personalBodegaMapper.toEntity(personalBodegaDto, personalBodega);

        // Verifica si se proporcionó un nuevo teléfono y lo formatea
        if (personalBodegaDto.telefono() != null && !personalBodegaDto.telefono().isEmpty()) {
            personalBodega.setTelefono(phoneServicio.obtenerTelefonoFormateado(
                    personalBodegaDto.telefono(),
                    personalBodega.getBodega().getUbicacion().getPais()));}

        // Verifica si se proporcionó un teléfono secundario y lo formatea
        if (personalBodegaDto.telefonoSecundario() != null && !personalBodegaDto.telefonoSecundario().isEmpty()) {
            personalBodega.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(
                    personalBodegaDto.telefonoSecundario(),
                    personalBodega.getBodega().getUbicacion().getPais()));}

        // Si se proporcionó una nueva imagen, se sube y actualiza
        if (personalBodegaDto.imagenPerfil() != null && !personalBodegaDto.imagenPerfil().isEmpty()) {
            // Elimina la imagen anterior del personal de bodega
            cloudinaryServicio.eliminarImagen(personalBodega.getImagenPerfil());
            // Sube la imagen nueva de perfil
            personalBodega.setImagenPerfil(cloudinaryServicio.uploadImage(personalBodegaDto.imagenPerfil()));}

        // Guarda los cambios actualizados en la base de datos
        personalBodegaRepo.save(personalBodega);

        // Registra un mensaje en los logs con el ID del personal modificado exitosamente
        logger.info("Cuenta del Personal de Bodega con ID {} ha sido modificada exitosamente.", personalBodegaDto.id());
    }

    @Override
    public BodegaDto obtenerBodegaPersonal(Long id) throws ElementoNoEncontradoException {
        PersonalBodega personalBodega = obtenerPersonalPorId(id);
        Bodega bodega = personalBodega.getBodega();
        return bodegaMapper.toDto(bodega);
    }


    /**
     * Recupera un Personal de Bodega desde él {@code repository} usando su ID.
     * Si el personal no existe, lanza una excepción personalizada.
     *
     * @param id ID único del Personal de Bodega a buscar.
     * @return El objeto {@link PersonalBodega} correspondiente al ID proporcionado.
     * @throws ElementoNoEncontradoException Si no se encuentra ningún Personal de Bodega con el ID dado.
     */
    private PersonalBodega obtenerPersonalPorId(Long id)
            throws ElementoNoEncontradoException {
        return personalBodegaRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + id));
    }


    /**
     * Busca un Personal de Bodega desde él {@code repository} por su correo electrónico utilizando dos estrategias de búsqueda.
     *
     * @param email Correo electrónico del Personal de Bodega.
     * @return {@link PersonalBodega} encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra el Personal con el email dado.
     */
    private PersonalBodega obtenerPersonalPorEmail(String email) throws ElementoNoEncontradoException {
        // Intentamos buscar primero por email exacto
        return personalBodegaRepo.findByUser_Email(email)
                .or(() -> personalBodegaRepo.findByDatosLaborales_EmailEmpresarial(email)) // Segunda estrategia: email empresarial
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + email));
    }


    /**
     * Elimina un personal de bodega de la base de datos de manera lógica.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Obtiene el personal de bodega utilizando un método reutilizable.</li>
     *     <li>Válida la contraseña ingresada por el usuario.</li>
     *     <li>Verifica si el personal de bodega tiene movimientos de productos activos que impidan su eliminación.</li>
     *     <li>Envía un correo al personal de bodega informando sobre la eliminación de su cuenta.</li>
     *     <li>Finalmente, elimina al personal de bodega de la base de datos.</li>
     * </ul>
     *
     * @param personal Objeto de tipo {@link EliminarClienteDto} que contiene la información necesaria para eliminar al personal de bodega,
     *            como su ID y la contraseña para verificar la autenticidad de la acción.
     * @throws ElementoNoEncontradoException Si el personal de bodega no existe en el sistema.
     * @throws ElementoIncorrectoException Si la contraseña ingresada no coincide con la registrada en el sistema.
     * @throws ElementoAunEnUsoException Si el personal de bodega tiene movimientos de productos activos, lo que impide su eliminación.
     */
    @Override
    public void eliminarPersonal(EliminarClienteDto personal)
            throws ElementoNoEncontradoException,
            ElementoIncorrectoException {

        // Obtener el personal de bodega usando el método reutilizable
        PersonalBodega personalBodega = obtenerPersonalPorId(personal.id());

        // Validar la contraseña ingresada
        if (!passwordEncoder.matches(personal.password(), personalBodega.getUser().getPassword())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);
        }

        // Enviar correo al personal de bodega informando sobre la eliminación de su cuenta
        EmailDto emailDto = new EmailDto(
                personalBodega.getUser().getEmail(),
                "Cuenta eliminada de Store-It",
                "Hola " + personalBodega.getNombre() + ",\n\n" +
                        "Tu cuenta en Store-It ha sido eliminada exitosamente. " +
                        "Si tú no solicitaste esta acción, por favor contáctanos inmediatamente.");
        emailServicio.enviarCorreo(emailDto);

        // Eliminar al personal de bodega
        personalBodega.getUser().setEstadoCuenta(EstadoCuenta.ELIMINADO);
        personalBodegaRepo.save(personalBodega);

        logger.info("Cuenta del Personal de Bodega con ID {} eliminada exitosamente.", personal.id());
    }


    /**
     * Obtiene un personal de bodega a partir de su ID y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el personal de bodega correspondiente al ID proporcionado.</li>
     *     <li>Convierte la entidad {@link PersonalBodega} a un objeto DTO de tipo {@link PersonalBodegaDto}.</li>
     * </ul>
     *
     * @param id El ID del personal de bodega que se desea obtener.
     * @return {@link PersonalBodegaDto} Objeto DTO con la información del personal de bodega.
     * @throws ElementoNoEncontradoException Si no se encuentra un personal de bodega con el ID proporcionado.
     */
    @Override
    public PersonalBodegaDto obtenerPersonalId(Long id)
            throws ElementoNoEncontradoException {
        return personalBodegaMapper.toDto(obtenerPersonalPorId(id));
    }


    /**
     * Obtiene un personal de bodega a partir de su correo electrónico y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el personal de bodega correspondiente al correo electrónico proporcionado.</li>
     *     <li>Convierte la entidad {@link PersonalBodega} a un objeto DTO de tipo {@link PersonalBodegaDto}.</li>
     * </ul>
     *
     * @param email El correo electrónico del personal de bodega que se desea obtener.
     * @return {@link PersonalBodegaDto} Objeto DTO con la información del personal de bodega.
     * @throws ElementoNoEncontradoException Si no se encuentra un personal de bodega con el email proporcionado.
     */
    @Override
    public PersonalBodegaDto obtenerPersonalEmail(String email)
            throws ElementoNoEncontradoException {
        return personalBodegaMapper.toDto(obtenerPersonalPorEmail(email));
    }


    /**
     * Actualiza la información del usuario (correo electrónico y contraseña) de un personal de bodega existente.
     * <p>
     * Este método verifica que la contraseña proporcionada sea la correcta, valida que el nuevo correo electrónico
     * no esté ya registrado en otro personal, y asegura que la nueva contraseña sea diferente de la actual.
     * Si todas las validaciones pasan, se actualizan los datos del usuario en la base de datos.
     * </p>
     *
     * @param personal Objeto DTO que contiene el ID del personal, la contraseña actual, el nuevo correo electrónico
     *                     y la nueva contraseña que se desea establecer.
     * @throws ElementoNoEncontradoException Si no se encuentra el personal de bodega con el ID proporcionado.
     * @throws ElementoIncorrectoException Si la contraseña proporcionada es incorrecta o si la nueva contraseña es igual a la anterior.
     * @throws ElementoRepetidoException Si el correo electrónico proporcionado ya está registrado en otro personal de bodega.
     */
    @Override
    public void editarUserPersonalBodega(EditarUserPersona personal) throws ElementoNoEncontradoException, ElementoIncorrectoException,
            ElementoRepetidoException {

        // Obtener el personal de bodega por ID
        PersonalBodega personalBodega = obtenerPersonalPorId(personal.id());

        // Verificar si la contraseña proporcionada coincide con la almacenada
        if (!personalBodega.getUser().getPassword().equals(personal.password())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);}

        // Verificamos si la contraseña nueva no es nula
        if(personal.passwordModificado() != null && !personal.passwordModificado().isEmpty()) {
            // Verificar si la nueva contraseña es diferente a la anterior
            if (personal.passwordModificado().equals(personal.password())) {
                throw new ElementoIncorrectoException(MensajeError.PASSWORD_REPETIDO);}

            String passwordEncriptada = passwordEncoder.encode(personal.passwordModificado());
            // Actualizar la información del usuario
            personalBodega.getUser().setPassword(passwordEncriptada);}


        if (personal.email() != null && !personal.email().isEmpty()) {
            // Validar que el nuevo email no esté repetido
            validacionCuentaServicio.validarEmailNoRepetido(personal.email());

            // Enviar correo notificando la modificación
            EmailDto emailDto = new EmailDto(
                    personalBodega.getUser().getEmail(),
                    "Actualización de credenciales - Store-It",
                    String.format("Hola %s,\n\nTe informamos que tus credenciales de acceso a Store-It han sido modificadas correctamente.\n\n" +
                                    "Nuevo correo electrónico: %s\nNueva contraseña: %s\n\n" +
                                    "Si tú no realizaste esta modificación, por favor contáctanos de inmediato.\n\n" +
                                    "Saludos cordiales,\nEquipo Store-It",
                            personalBodega.getNombre(),
                            personal.email(),
                            personalBodega.getUser().getPassword()));
            emailServicio.enviarCorreo(emailDto);
            personalBodega.getUser().setEmail(personal.email());}


        // Enviar correo notificando la modificación
        EmailDto emailDto = new EmailDto(
                personalBodega.getUser().getEmail(),
                "Actualización de credenciales - Store-It",
                String.format("Hola %s,\n\nTe informamos que tus credenciales de acceso a Store-It han sido modificadas correctamente.\n\n" +
                                "Nuevo correo electrónico: %s\nNueva contraseña: %s\n\n" +
                                "Si tú no realizaste esta modificación, por favor contáctanos de inmediato.\n\n" +
                                "Saludos cordiales,\nEquipo Store-It",
                        personalBodega.getNombre(),
                        personalBodega.getUser().getEmail(),
                        personalBodega.getUser().getPassword()));
        emailServicio.enviarCorreo(emailDto);

        // Guardar cambios en el repositorio
        personalBodegaRepo.save(personalBodega);

        logger.warn("La cuenta del Personal de Bodega con ID {} ha modificado su user", personal.id());
    }


    /**
     * Lista el Personal de Bodega aplicando filtros opcionales y paginación.
     * <p>
     * <ul>
     * <li> Crea una instancia de {@code Pageable}, que indica el número de página y
     * el tamaño por página para realizar la consulta paginada en base de datos.
     * <li> Inicializa {@code Specification} de búsqueda como null. A partir de ahí,
     * se le van agregando condiciones según los parámetros que no estén vacíos o nulos.</li>
     * </ul>
     * </p>
     *
     * @param idBodega ID de la bodega a la que pertenece el personal (opcional).
     * @param fechaContratacion Fecha de ingreso del personal de bodega (opcional).
     * @param tipoCargo Puesto o rol del personal de bodega (opcional).
     * @param estadoContrato Estado del personal de bodega (opcional).
     * @param tipoContrato Tipo de contrato laboral (opcional).
     * @param pagina Número de página para la paginación.
     * @param size Tamaño de página para la paginación.
     * @return Lista paginada de personal de bodega en forma de DTO.
     */
    @Override
    public List<PersonalBodegaDto> listarPersonalBodega (String idBodega, LocalDate fechaContratacion,
                                                  EstadoContratoLaboral estadoContrato,
                                                  TipoContrato tipoContrato, TipoCargo tipoCargo,
                                                  int pagina, int size ) {
        // Construir objeto Pageable para aplicar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Crear predicado dinámico con filtros opcionales
        Specification<PersonalBodega> spec = Specification.where(null);


        // Si se proporciona un, id de bodega, filtrar por la bodega del personal
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("bodega").get("id"), idBodega));}

        // Si se proporciona una fecha de contratación, filtrar por dicha fecha
        if (fechaContratacion != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("fechaContratacion").as(LocalDate.class), fechaContratacion));}

        // Sí se proporciona un tipo de contrato, filtrar por ese tipo
        if (tipoContrato != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("tipoContrato"), tipoContrato));}

        // Sí se proporciona un estado de contrato, filtrar por ese estado
        if (estadoContrato != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("datosLaborales").get("estadoContratoLaboral"), estadoContrato));}

        // Si se proporciona un tipo cargo del personam filtra por ese estado
        if (tipoCargo != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoCargo"), tipoCargo));}

        // Obtener la página de personal bodega que cumplen con los filtros
        Page<PersonalBodega> personalBodegasPage = personalBodegaRepo.findAll(spec, pageable);

        // Convertir cada entidad PersonalBodega a su respectivo DTO
        return personalBodegasPage.getContent().stream()
                .map(personalBodegaMapper::toDto)
                .collect(Collectors.toList());
    }
}
