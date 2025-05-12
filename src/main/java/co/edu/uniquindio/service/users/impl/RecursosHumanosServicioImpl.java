package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.objects.sede.SedeDto;
import co.edu.uniquindio.dto.users.agenteVentas.TrasladoSedeAgenteDto;
import co.edu.uniquindio.dto.users.common.EditarDatosLaboralesDto;
import co.edu.uniquindio.dto.users.cliente.EliminarClienteDto;
import co.edu.uniquindio.dto.users.personalBodega.CambiarTipoCargoDto;
import co.edu.uniquindio.dto.users.personalBodega.TrasladoPersonalBodegaDto;
import co.edu.uniquindio.dto.users.recursosHumanos.CrearRHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.EditarRRHHDto;
import co.edu.uniquindio.dto.users.recursosHumanos.ReactivarCuentaDto;
import co.edu.uniquindio.dto.users.recursosHumanos.RecursosHumanosDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.mapper.objects.SedeMapper;
import co.edu.uniquindio.mapper.users.RecursosHumanosMapper;
import co.edu.uniquindio.model.users.common.DatosLaborales;
import co.edu.uniquindio.model.users.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.users.enums.TipoContrato;
import co.edu.uniquindio.model.objects.Bodega;
import co.edu.uniquindio.model.objects.Sede;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.PersonalBodega;
import co.edu.uniquindio.model.users.RecursosHumanos;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.objects.SedeRepo;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.repository.users.RecursosHumanosRepo;
import co.edu.uniquindio.service.users.RecursosHumanosServicio;
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
 * Implementación concreta del servicio {@link RecursosHumanosServicio} encargado de gestionar
 * las operaciones relacionadas con los recursos humanos dentro del sistema Store-It.
 * <p>
 * Esta clase integra funcionalidades como:
 * <ul>
 *     <li>Registro de nuevos recursos humanos con validación de correo, teléfono e imagen de perfil.</li>
 *     <li>Eliminación lógica de recursos.</li>
 *     <li>Actualización de información personal y laboral del recurso humano.</li>
 *     <li>Reactivar cuentas anteriormente eliminadas del personal y agentes de ventas.</li>
 *     <li>Editar los datos laborales del personal y agente de ventas.</li>
 *     <li>Gestionar los traslados de {@code Sedes} y {@code Bodegas} de ambas entidades.</li>
 * </ul>
 * <p>
 * Además, incorpora servicios externos como:
 * <ul>
 *     <li>{@code Cloudinary} para la gestión de imágenes.</li>
 *     <li>{@code libphonenumber} para la validación y formateo de teléfonos.</li>
 * </ul>
 * <p>
 * La configuración necesaria se gestiona a través del archivo {@code application.yml}.
 *
 * @see RecursosHumanosRepo
 * @see RecursosHumanosMapper
 * @see AgenteVentasRepo
 * @see CloudinaryServicio
 * @see EmailServicio
 * @see PhoneServicio
 * @see ValidacionCuentaServicio
 *
 * @author MrZ.Leviatan
 */
@Service
@RequiredArgsConstructor
public class RecursosHumanosServicioImpl implements RecursosHumanosServicio {

    private final RecursosHumanosRepo recursosHumanosRepo;
    private final RecursosHumanosMapper recursosHumanosMapper;
    private final CloudinaryServicio cloudinaryServicio;
    private final PasswordEncoder passwordEncoder;
    private final EmailServicio emailServicio;
    private final PhoneServicio phoneServicio;
    private final ValidacionCuentaServicio validacionCuentaServicio;
    private final AgenteVentasRepo agenteVentasRepo;
    private final PersonalBodegaRepo personalBodegaRepo;
    private final SedeRepo sedeRepo;
    private final BodegaRepo bodegaRepo;
    private final SedeMapper sedeMapper;
    private static final Logger logger = LoggerFactory.getLogger(RecursosHumanosServicioImpl.class);

    private static final String IMAGEN_URL = "https://res.cloudinary.com/dehltwwbu/image/upload/v1745680101/PerfilErroError_pgaehq.jpg";


    /**
     * Registra un nuevo miembro del personal de Recursos Humanos en el sistema utilizando un DTO de entrada.
     * Este método realiza las siguientes acciones principales:
     * <ol>
     *   <li>Válida que el correo electrónico no haya sido registrado previamente.</li>
     *   <li>Sube la imagen de perfil del personal de Recursos Humanos a Cloudinary.</li>
     *   <li>Encripta la contraseña proporcionada por el usuario.</li>
     *   <li>Convierte el DTO recibido en una entidad Recursos Humanos.</li>
     *   <li>Valida y formatea los números telefónicos (principal y secundario).</li>
     *   <li>Asigna la imagen de perfil y la contraseña encriptada a la entidad.</li>
     *   <li>Guarda al miembro de Recursos Humanos en la base de datos.</li>
     *   <li>Envía un correo al email del personal registrado.</li>
     * </ol>
     *
     * @param recursosHumanosDto DTO con los datos del personal de Recursos Humanos a registrar.
     * @throws ElementoRepetidoException     Si el correo electrónico ya está registrado.
     * @throws ElementoIncorrectoException   Si los datos proporcionados no cumplen con las validaciones esperadas.
     * @throws ElementoNulosException        Si se detectan campos nulos obligatorios.
     * @throws ElementoNoEncontradoException Si no se encuentra información requerida durante el proceso.
     */

    @Override
    public void registrarRecursosHumanos(CrearRHDto recursosHumanosDto) throws ElementoRepetidoException, ElementoIncorrectoException, ElementoNulosException, ElementoNoEncontradoException {
        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentaServicio.validarEmailNoRepetido(recursosHumanosDto.user().email());
        validacionCuentaServicio.validarEmailEmpresarialNoRepetido(recursosHumanosDto.datosLaborales().emailEmpresarial());
        validacionCuentaServicio.validarTelefonoNoRepetido(recursosHumanosDto.telefono());
        validacionCuentaServicio.validarTelefonoNoRepetido(recursosHumanosDto.telefonoSecundario());

        // 2. Subimos imagen y encriptamos contraseña
        String urlImagen = cloudinaryServicio.uploadImage(recursosHumanosDto.imagenPerfil());
        String passwordEncriptada = passwordEncoder.encode(recursosHumanosDto.user().password());

        // 3. Convertimos DTO a entidad
        RecursosHumanos recursosHumanos = recursosHumanosMapper.toEntity(recursosHumanosDto);

        // 4. Formateamos teléfonos
        recursosHumanos.setTelefono(phoneServicio.obtenerTelefonoFormateado(recursosHumanos.getTelefono(), recursosHumanos.getSede().getUbicacion().getPais()));
        if (recursosHumanos.getTelefonoSecundario() != null && !recursosHumanos.getTelefonoSecundario().isEmpty()) {
            recursosHumanos.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(recursosHumanos.getTelefonoSecundario(), recursosHumanos.getSede().getUbicacion().getPais()));}

        // 5. Asignamos imagen y contraseña encriptada
        recursosHumanos.setImagenPerfil(urlImagen);
        recursosHumanos.getUser().setPassword(passwordEncriptada);

        // Validamos si la sede existe
        Sede sede = sedeRepo.findById(recursosHumanos.getSede().getId())
                .orElseThrow(()-> new ElementoNoEncontradoException(MensajeError.SEDE_NO_ENCONTRADA));
        recursosHumanos.setSede(sede);

        // 6. Guardamos en la base de datos
        sedeRepo.save(sede);

        // Enviamos el correo de bienvenida al personal de Recursos Humanos
        EmailDto emailDto = new EmailDto(
                recursosHumanos.getUser().getEmail(),
                "Bienvenido a Store-It",
                String.format("Hola %s,\n\n¡Bienvenido a Store-It!\nNos complace contar contigo como parte del equipo de Recursos Humanos.\n\n" +
                                "Esperamos que tengas una experiencia enriquecedora y llena de logros junto a nosotros.\n\nSaludos cordiales,\nEquipo Store-It",
                        recursosHumanos.getNombre()));
        emailServicio.enviarCorreo(emailDto);

        logger.info("Se ha registrado un nuevo miembro de Recursos Humanos: {}.",
                recursosHumanos.getUser().getEmail());

    }

    /**
     * Modifica la información de un empleado de recursos humanos en la base de datos.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Busca al empleado de RRHH utilizando su ID.</li>
     *     <li>Actualiza los datos del empleado con la información proporcionada en el DTO.</li>
     *     <li>Formatea y actualiza los números de teléfono si se proporcionaron nuevos valores.</li>
     *     <li>Sube y actualiza la imagen de perfil si se proporciona una nueva imagen.</li>
     *     <li>Guarda los cambios actualizados del empleado en la base de datos.</li>
     * </ul>
     *
     * @param recursosHumanosDto Objeto de tipo {@link EditarRRHHDto} que contiene los nuevos datos a actualizar.
     * @throws ElementoNoEncontradoException Si no se encuentra el empleado en la base de datos.
     * @throws ElementoIncorrectoException Si alguno de los datos proporcionados es incorrecto o no válido.
     * @throws ElementoNulosException Si alguno de los campos necesarios está nulo o vacío.
     */
    @Override
    public void editarRecursosHumanos(EditarRRHHDto recursosHumanosDto) throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNulosException {

        RecursosHumanos recursosHumanos = obtenerRecursosHumanosPorId(recursosHumanosDto.idRRHH());

        // Actualiza los datos del empleado con la información del DTO
        recursosHumanosMapper.toEntity(recursosHumanosDto, recursosHumanos);

        // Formatea teléfonos si son proporcionados
        if (recursosHumanosDto.telefono() != null && !recursosHumanosDto.telefono().isEmpty()) {
            recursosHumanos.setTelefono(phoneServicio.obtenerTelefonoFormateado(
                    recursosHumanosDto.telefono(), recursosHumanosDto.codigoPais()));
        }

        if (recursosHumanosDto.telefonoSecundario() != null && !recursosHumanosDto.telefonoSecundario().isEmpty()) {
            recursosHumanos.setTelefonoSecundario(phoneServicio.obtenerTelefonoFormateado(
                    recursosHumanosDto.telefonoSecundario(), recursosHumanosDto.codigoPaisSecundario()));
        }

        // Actualiza imagen de perfil si se proporciona una nueva
        if (recursosHumanosDto.imagenPerfil() != null && !recursosHumanosDto.imagenPerfil().isEmpty()) {
            recursosHumanos.setImagenPerfil(cloudinaryServicio.uploadImage(recursosHumanosDto.imagenPerfil()));
        }

        // Guarda los cambios en la base de datos
        recursosHumanosRepo.save(recursosHumanos);

        logger.info("Cuenta del Empleado RRHH con ID {} ha sido modificada exitosamente.", recursosHumanosDto.idRRHH());
    }


    /**
     * Recupera un empleado de recursos humanos desde el repositorio usando su ID.
     *
     * @param id ID único del empleado.
     * @return El objeto {@link RecursosHumanos} correspondiente.
     * @throws ElementoNoEncontradoException Si no se encuentra ningún empleado con el ID dado.
     */
    private RecursosHumanos obtenerRecursosHumanosPorId(Long id) throws ElementoNoEncontradoException {
        return recursosHumanosRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + id));
    }


    /**
     * Busca un recurso humano por su correo electrónico utilizando dos estrategias de búsqueda:
     * primero por el correo del usuario y luego por el correo empresarial en los datos laborales.
     *
     * @param email Correo electrónico del recurso humano.
     * @return RecursosHumanos encontrado.
     * @throws ElementoNoEncontradoException si no se encuentra el recurso humano con el email dado.
     */
    private RecursosHumanos obtenerRecursosHumanosPorEmail(String email) throws ElementoNoEncontradoException {
        // Intentamos buscar primero por email exacto
        return recursosHumanosRepo.findByUser_Email(email)
                .or(() -> recursosHumanosRepo.findByDatosLaborales_EmailEmpresarial(email)) // Segunda estrategia: email empresarial
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + email));
    }


    /**
     * Elimina un recurso humano de la base de datos de manera lógica.
     * <p>
     * Este método realiza las siguientes acciones principales:
     * <ul>
     *     <li>Obtiene el recurso humano utilizando un método reutilizable.</li>
     *     <li>Válida la contraseña ingresada por el usuario.</li>
     *     <li>Verifica si el recurso humano tiene contratos activos que impidan su eliminación.</li>
     *     <li>Envía un correo al recurso humano informando sobre la eliminación de su cuenta.</li>
     *     <li>Finalmente, elimina al recurso humano de la base de datos.</li>
     * </ul>
     *
     * @param recursoHumano Objeto de tipo {@link EliminarClienteDto} que contiene la información necesaria para eliminar al recurso humano,
     *                      como su ID y la contraseña para verificar la autenticidad de la acción.
     * @throws ElementoNoEncontradoException Si el recurso humano no existe en el sistema.
     * @throws ElementoIncorrectoException Si la contraseña ingresada no coincide con la registrada en el sistema.
     */
    @Override
    public void eliminarRecursosHumanos(EliminarClienteDto recursoHumano) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        // Obtener el recurso humano usando el método reutilizable
        RecursosHumanos rh = obtenerRecursosHumanosPorId(recursoHumano.id());

        // Validar la contraseña ingresada
        if (!passwordEncoder.matches(recursoHumano.password(), rh.getUser().getPassword())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);
        }

        // Crear el correo informativo para el recurso humano
        EmailDto emailDto = new EmailDto(
                rh.getUser().getEmail(),
                "Cuenta eliminada de Store-It",
                "Hola " + rh.getNombre() + ",\n\n" +
                        "Tu cuenta en Store-It ha sido eliminada exitosamente. " +
                        "Si tú no solicitaste esta acción, por favor contáctanos inmediatamente.");
        emailServicio.enviarCorreo(emailDto);

        // Marcar el estado de la cuenta como eliminado y guardar cambios
        rh.getUser().setEstadoCuenta(EstadoCuenta.ELIMINADO);

        // Guardamos en la base
        recursosHumanosRepo.save(rh);

        logger.info("Cuenta de Recursos Humanos con ID {} " +
                "eliminada exitosamente.", recursoHumano.id());
    }


    @Override
    public SedeDto obtenerSedeRRHH(Long id) throws ElementoNoEncontradoException {
        RecursosHumanos rh = obtenerRecursosHumanosPorId(id);
        Sede sede = sedeRepo.findById(rh.getSede().getId()).orElseThrow(()
                -> new ElementoNoEncontradoException(MensajeError.SEDE_NO_ENCONTRADA));
        return sedeMapper.toDto(sede);
    }


    /**
     * Obtiene un recurso humano a partir de su ID y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el recurso humano correspondiente al ID proporcionado.</li>
     *     <li>Convierte la entidad {@link RecursosHumanos} a un objeto DTO de tipo {@link RecursosHumanosDto}.</li>
     * </ul>
     *
     * @param id El ID del recurso humano que se desea obtener.
     * @return {@link RecursosHumanosDto} Objeto DTO con la información del recurso humano.
     * @throws ElementoNoEncontradoException Si no se encuentra un recurso humano con el ID proporcionado.
     */
    @Override
    public RecursosHumanosDto obtenerRecursosId(Long id) throws ElementoNoEncontradoException {
        return recursosHumanosMapper.toDto(obtenerRecursosHumanosPorId(id));
    }


    /**
     * Obtiene un recurso humano a partir de su correo electrónico y lo convierte a un DTO.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Obtiene el recurso humano correspondiente al correo electrónico proporcionado.</li>
     *     <li>Convierte la entidad {@link RecursosHumanos} a un objeto DTO de tipo {@link RecursosHumanosDto}.</li>
     * </ul>
     *
     * @param email El correo electrónico del recurso humano que se desea obtener.
     * @return {@link RecursosHumanosDto} Objeto DTO con la información del recurso humano.
     * @throws ElementoNoEncontradoException Si no se encuentra un recurso humano con el email proporcionado.
     */
    @Override
    public RecursosHumanosDto obtenerRecursosEmail(String email) throws ElementoNoEncontradoException {
        return recursosHumanosMapper.toDto(obtenerRecursosHumanosPorEmail(email));
    }


    /**
     * Reactiva la cuenta de un trabajador en el sistema, cambiando su estado a ACTIVO.
     * <p>
     * Este método permite reactivar cuentas previamente eliminadas o desactivadas para los siguientes tipos de usuarios:
     * <ul>
     *     <li>Agente de ventas</li>
     *     <li>Personal de bodega</li>
     *     <li>Recursos humanos</li>
     * </ul>
     * <p>
     * Realiza las siguientes validaciones:
     * <ul>
     *     <li>Verifica si el ID proporcionado corresponde a un usuario existente.</li>
     *     <li>Valida que el estado actual de la cuenta permita la reactivación.</li>
     *     <li>Actualiza el estado de la cuenta a {@code ACTIVO} y guarda los cambios en la base de datos.</li>
     * </ul>
     *
     * @param reactivarCuentaDto Objeto que contiene el ID del usuario y el tipo de cuenta a reactivar.
     * @throws ElementoNoEncontradoException Si no se encuentra el usuario con el ID proporcionado.
     * @throws ElementoIncorrectoException Si el tipo de usuario no es válido o si el estado actual no permite reactivación.
     */
    @Override
    public void reactivarCuentaLaboral(ReactivarCuentaDto reactivarCuentaDto) throws ElementoNoEncontradoException, ElementoIncorrectoException {

        switch (reactivarCuentaDto.tipo().toLowerCase()){

            // Válida y reactiva la cuenta de los agentes de ventas
            case "agente_ventas": {
                AgenteVentas agenteVentas = agenteVentasRepo.findById(reactivarCuentaDto.id())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
                validarEstado(agenteVentas.getUser().getEstadoCuenta());

                agenteVentas.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
                agenteVentasRepo.save(agenteVentas);
                break;
            }

            // Válida y reactiva las cuentas del personal de bodega.
            case "personal_bodega":
                PersonalBodega personal = personalBodegaRepo.findById(reactivarCuentaDto.id())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
                validarEstado(personal.getUser().getEstadoCuenta());
                personal.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
                personalBodegaRepo.save(personal);
                break;


            // Válida y reactiva la cuenta del personal de recursos humanos
            case "recursos_humanos":
                RecursosHumanos rrhh = recursosHumanosRepo.findById(reactivarCuentaDto.id())
                        .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
                validarEstado(rrhh.getUser().getEstadoCuenta());
                rrhh.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
                recursosHumanosRepo.save(rrhh);
                break;

            // Si el usuario no está asociado a ninguna clase.
            default:
                throw new ElementoIncorrectoException(
                        MensajeError.TIPO_USUARIO_NO_VALIDO);}
    }


    /**
     * Válida que el estado de la cuenta sea ELIMINADO antes de proceder con su reactivación.
     * <p>
     * Este método se utiliza como una verificación previa para asegurar que solo las cuentas
     * que han sido eliminadas puedan ser reactivadas. Si la cuenta no se encuentra en estado {@code ELIMINADO},
     * se lanza una excepción para impedir la operación.
     *
     * @param estado Estado actual de la cuenta que se desea verificar.
     * @throws ElementoAunEnUsoException Si la cuenta no se encuentra en estado ELIMINADO.
     */
    private void validarEstado (EstadoCuenta estado){
        if (estado != EstadoCuenta.ELIMINADO) {
            throw new ElementoAunEnUsoException(MensajeError.CUENTA_NO_ELIMINADA);}
    }


    /**
     * Actualiza los datos laborales de un agente de ventas específico.
     * <p>
     * Este método permite modificar campos como la fecha de finalización del contrato,
     * el sueldo, el tipo de contrato y el estado del contrato, siempre y cuando dichos
     * valores sean proporcionados en el DTO. Los campos no enviados serán ignorados
     * para preservar la información existente.
     * </p>
     *
     * @param agenteDatosLaboral DTO que contiene el ID del agente y los nuevos valores a actualizar.
     *                           Solo se modificarán los atributos que no sean nulos.
     *
     * @throws ElementoNoEncontradoException si no existe un agente de ventas con el ID proporcionado.
     *
     * @see DatosLaborales para más detalles sobre los campos que pueden ser modificados.
     * @see EditarDatosLaboralesDto para la estructura del DTO de entrada.
     */
    @Override
    public void editarDatosLaboralesAgente(EditarDatosLaboralesDto agenteDatosLaboral) throws ElementoNoEncontradoException {

        // Obtenemos el agente de ventas por su ID o lanzamos excepción si no existe
        AgenteVentas agenteVentas = agenteVentasRepo.findById(agenteDatosLaboral.id())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtenemos el objeto embebido DatosLaborales
        DatosLaborales datosLaborales = agenteVentas.getDatosLaborales();

        // Actualizamos solo los campos no nulos del DTO
        if (agenteDatosLaboral.fechaFinContratacion() != null) {
            datosLaborales.setFechaFinContrato(agenteDatosLaboral.fechaFinContratacion());}

        if (agenteDatosLaboral.sueldo() != null) {
            datosLaborales.setSueldo(agenteDatosLaboral.sueldo());}

        if (agenteDatosLaboral.tipoContrato() != null) {
            datosLaborales.setTipoContrato(agenteDatosLaboral.tipoContrato());}

        if (agenteDatosLaboral.estadoContratoLaboral() != null) {
            datosLaborales.setEstadoContratoLaboral(agenteDatosLaboral.estadoContratoLaboral());}

        agenteVentas.setDatosLaborales(datosLaborales);

        // Guardamos el agente con los datos actualizados
        agenteVentasRepo.save(agenteVentas);

        // Enviamos un correo notificando los cambios al agente
        EmailDto emailDto = new EmailDto(
                agenteVentas.getUser().getEmail(),
                "Actualización de datos laborales - Store-It",
                String.format("Hola %s,\n\nTus datos laborales han sido actualizados:\n\n" +
                                "- Fecha fin contrato: %s\n" +
                                "- Sueldo: %s\n" +
                                "- Tipo de contrato: %s\n" +
                                "- Estado del contrato: %s\n\n" +
                                "Si no solicitaste estos cambios, por favor comunícate con RRHH.\n\nSaludos,\nEquipo Store-It",
                        agenteVentas.getNombre(),
                        datosLaborales.getFechaFinContrato() != null ? datosLaborales.getFechaFinContrato() : "Sin cambios",
                        datosLaborales.getSueldo() != null ? datosLaborales.getSueldo() : "Sin cambios",
                        datosLaborales.getTipoContrato() != null ? datosLaborales.getTipoContrato() : "Sin cambios",
                        datosLaborales.getEstadoContratoLaboral() != null ? datosLaborales.getEstadoContratoLaboral() : "Sin cambios"
                ));
        emailServicio.enviarCorreo(emailDto);

        // Registramos la acción en el log
        logger.info("Se actualizaron los datos laborales del agente con ID {}. Nuevos datos: {}",
                agenteVentas.getId(), datosLaborales);
    }


    /**
     * Actualiza los datos laborales de un personal de bodega específico.
     * <p>
     * Este método permite modificar campos como la fecha de finalización del contrato,
     * el sueldo, el tipo de contrato y el estado del contrato, siempre y cuando dichos
     * valores sean proporcionados en el DTO. Los campos no enviados serán ignorados
     * para preservar la información existente.
     * </p>
     *
     * @param personalDatosLaboral DTO que contiene el ID del personal y los nuevos valores a actualizar.
     *                             Solo se modificarán los atributos que no sean nulos.
     *
     * @throws ElementoNoEncontradoException si no existe un personal de bodega con el ID proporcionado.
     *
     * @see DatosLaborales para más detalles sobre los campos que pueden ser modificados.
     * @see EditarDatosLaboralesDto para la estructura del DTO de entrada.
     */
    @Override
    public void editarDatosLaboralesPersonal(EditarDatosLaboralesDto personalDatosLaboral) throws ElementoNoEncontradoException {

        // Obtenemos el personal de bodega por su ID o lanzamos excepción si no existe
        PersonalBodega personalBodega = personalBodegaRepo.findById(personalDatosLaboral.id())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtenemos el objeto embebido DatosLaborales
        DatosLaborales datosLaborales = personalBodega.getDatosLaborales();

        // Actualizamos solo los campos no nulos del DTO
        if (personalDatosLaboral.fechaFinContratacion() != null) {
            datosLaborales.setFechaFinContrato(personalDatosLaboral.fechaFinContratacion());
        }

        if (personalDatosLaboral.sueldo() != null) {
            datosLaborales.setSueldo(personalDatosLaboral.sueldo());
        }

        if (personalDatosLaboral.tipoContrato() != null) {
            datosLaborales.setTipoContrato(personalDatosLaboral.tipoContrato());
        }

        if (personalDatosLaboral.estadoContratoLaboral() != null) {
            datosLaborales.setEstadoContratoLaboral(personalDatosLaboral.estadoContratoLaboral());
        }

        personalBodega.setDatosLaborales(datosLaborales);

        // Guardamos el personal con los datos actualizados
        personalBodegaRepo.save(personalBodega);

        // Enviamos un correo notificando los cambios al personal
        EmailDto emailDto = new EmailDto(
                personalBodega.getUser().getEmail(),
                "Actualización de datos laborales - Store-It",
                String.format("Hola %s,\n\nTus datos laborales han sido actualizados:\n\n" +
                                "- Fecha fin contrato: %s\n" +
                                "- Sueldo: %s\n" +
                                "- Tipo de contrato: %s\n" +
                                "- Estado del contrato: %s\n\n" +
                                "Si no solicitaste estos cambios, por favor comunícate con RRHH.\n\nSaludos,\nEquipo Store-It",
                        personalBodega.getNombre(),
                        datosLaborales.getFechaFinContrato() != null ? datosLaborales.getFechaFinContrato() : "Sin cambios",
                        datosLaborales.getSueldo() != null ? datosLaborales.getSueldo() : "Sin cambios",
                        datosLaborales.getTipoContrato() != null ? datosLaborales.getTipoContrato() : "Sin cambios",
                        datosLaborales.getEstadoContratoLaboral() != null ? datosLaborales.getEstadoContratoLaboral() : "Sin cambios"
                ));
        emailServicio.enviarCorreo(emailDto);

        // Registramos la acción en el log
        logger.info("Se actualizaron los datos laborales del personal de bodega con ID {}. Nuevos datos: {}",
                personalBodega.getId(), datosLaborales);
    }


    /**
     * Actualiza los datos laborales de un miembro del equipo de Recursos Humanos específico.
     * <p>
     * Este método permite modificar campos como la fecha de finalización del contrato,
     * el sueldo, el tipo de contrato y el estado del contrato, siempre y cuando dichos
     * valores sean proporcionados en el DTO. Los campos no enviados serán ignorados
     * para preservar la información existente.
     * </p>
     *
     * @param recursosHumanosDto DTO que contiene el ID del miembro y los nuevos valores a actualizar.
     *                            Solo se modificarán los atributos que no sean nulos.
     *
     * @throws ElementoNoEncontradoException si no existe un recurso humano con el ID proporcionado.
     *
     * @see DatosLaborales para más detalles sobre los campos que pueden ser modificados.
     * @see EditarDatosLaboralesDto para la estructura del DTO de entrada.
     */
    @Override
    public void editarDatosLaboralesRecursosHumanos(EditarDatosLaboralesDto recursosHumanosDto) throws ElementoNoEncontradoException {

        // Obtenemos el recurso humano por su ID o lanzamos excepción si no existe
        RecursosHumanos recurso = recursosHumanosRepo.findById(recursosHumanosDto.id())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtenemos el objeto embebido DatosLaborales
        DatosLaborales datosLaborales = recurso.getDatosLaborales();

        // Actualizamos solo los campos no nulos del DTO
        if (recursosHumanosDto.fechaFinContratacion() != null) {
            datosLaborales.setFechaFinContrato(recursosHumanosDto.fechaFinContratacion());
        }

        if (recursosHumanosDto.sueldo() != null) {
            datosLaborales.setSueldo(recursosHumanosDto.sueldo());
        }

        if (recursosHumanosDto.tipoContrato() != null) {
            datosLaborales.setTipoContrato(recursosHumanosDto.tipoContrato());
        }

        if (recursosHumanosDto.estadoContratoLaboral() != null) {
            datosLaborales.setEstadoContratoLaboral(recursosHumanosDto.estadoContratoLaboral());
        }

        recurso.setDatosLaborales(datosLaborales);

        // Guardamos los datos actualizados
        recursosHumanosRepo.save(recurso);

        // Enviamos un correo notificando los cambios al recurso humano
        EmailDto emailDto = new EmailDto(
                recurso.getUser().getEmail(),
                "Actualización de datos laborales - Store-It",
                String.format("Hola %s,\n\nTus datos laborales han sido actualizados:\n\n" +
                                "- Fecha fin contrato: %s\n" +
                                "- Sueldo: %s\n" +
                                "- Tipo de contrato: %s\n" +
                                "- Estado del contrato: %s\n\n" +
                                "Si no solicitaste estos cambios, por favor comunícate con RRHH.\n\nSaludos,\nEquipo Store-It",
                        recurso.getNombre(),
                        datosLaborales.getFechaFinContrato() != null ? datosLaborales.getFechaFinContrato() : "Sin cambios",
                        datosLaborales.getSueldo() != null ? datosLaborales.getSueldo() : "Sin cambios",
                        datosLaborales.getTipoContrato() != null ? datosLaborales.getTipoContrato() : "Sin cambios",
                        datosLaborales.getEstadoContratoLaboral() != null ? datosLaborales.getEstadoContratoLaboral() : "Sin cambios"
                ));
        emailServicio.enviarCorreo(emailDto);

        // Registramos la acción en el log
        logger.info("Se actualizaron los datos laborales del recurso humano con ID {}. Nuevos datos: {}",
                recurso.getId(), datosLaborales);
    }


    /**
     * Cambia el tipo de cargo asignado a un miembro del personal de bodega.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     *     <li>Busca al miembro del personal de bodega mediante su ID.</li>
     *     <li>Actualiza el tipo de cargo con el nuevo valor proporcionado.</li>
     *     <li>Guarda los cambios en la base de datos.</li>
     *     <li>Envía una notificación por correo al personal afectado informando del cambio.</li>
     *     <li>Registra la acción en el log del sistema.</li>
     * </ul>
     *
     * @param cambiarTipoCargoDto DTO que contiene el ID del personal y el nuevo tipo de cargo.
     * @throws ElementoNoEncontradoException Si no se encuentra un personal de bodega con el ID proporcionado.
     */
    @Override
    public void cambiarTipoCargo(CambiarTipoCargoDto cambiarTipoCargoDto) throws ElementoNoEncontradoException {

        // Obtenemos el personal de bodega por su ID o lanzamos excepción si no existe
        PersonalBodega personalBodega = personalBodegaRepo.findById(cambiarTipoCargoDto.idPersonalBodega())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Actualizamos el Tipo cargo del personal.
        personalBodega.setTipoCargo(cambiarTipoCargoDto.nuevoTipoCargo());
        personalBodegaRepo.save(personalBodega);

        // Enviamos un correo notificando el cambio de tipo de cargo al personal de bodega
        EmailDto emailDto = new EmailDto(
                personalBodega.getUser().getEmail(),
                "Cambio de cargo - Store-It",
                String.format("Hola %s,\n\nTu tipo de cargo ha sido actualizado a: %s.\n\n" +
                                "Si no reconoces este cambio, por favor comunícate con Recursos Humanos.\n\nSaludos,\nEquipo Store-It",
                        personalBodega.getNombre(),
                        personalBodega.getTipoCargo() != null ? personalBodega.getTipoCargo() : "Sin especificar")
        );
        emailServicio.enviarCorreo(emailDto);

        // Registramos la acción en el log
        logger.info("Se actualizó el tipo de cargo del personal de bodega con ID {}. Nuevo tipo de cargo: {}",
                personalBodega.getId(), personalBodega.getTipoCargo());
    }


    /**
     * Realiza el traslado de un miembro del personal de bodega a una nueva bodega.
     *
     * @param trasladoPersonalBodegaDto DTO que contiene el ID del personal de bodega y el ID de la nueva bodega
     * @throws ElementoNoEncontradoException si el personal de bodega o la bodega no existen
     *
     * <p>Este método actualiza la asignación de bodega del personal,
     * registra la acción en los logs del sistema y envía una notificación por correo
     * al personal afectado.</p>
     */
    @Override
    public void trasladoBodegaPersonal(TrasladoPersonalBodegaDto trasladoPersonalBodegaDto) throws ElementoNoEncontradoException {

        // Obtenemos el personal de bodega por su ID o lanzamos excepción si no existe
        PersonalBodega personalBodega = personalBodegaRepo.findById(trasladoPersonalBodegaDto.idPersonal())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtener la nueva bodega o lanzar excepción si no se encuentra
        Bodega nuevaBodega = bodegaRepo.findById(trasladoPersonalBodegaDto.idBodega())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.BODEGA_NO_ENCONTRADA+trasladoPersonalBodegaDto.idBodega()));

        // Obtenemos la bodega anterior (puede ser null si es la primera asignación)
        Bodega anteriorBodega = personalBodega.getBodega();

        // Si ya está en la misma bodega, evitamos el traslado innecesario
        if (anteriorBodega != null && anteriorBodega.getId().equals(nuevaBodega.getId())) {
            logger.warn("El personal de bodega ID {} ya pertenece a la bodega '{}'. No se realizó traslado.",
                    personalBodega.getId(), nuevaBodega.getId());
            return;
        }

        // Eliminamos al personal de la bodega anterior (si tenía)
        if (anteriorBodega != null) {
            anteriorBodega.getPersonalBodega().remove(personalBodega);
            bodegaRepo.save(anteriorBodega);
        }

        // Asignamos la nueva bodega
        personalBodega.setBodega(nuevaBodega);
        nuevaBodega.getPersonalBodega().add(personalBodega);

        // Guardamos los cambios
        bodegaRepo.save(nuevaBodega);
        personalBodegaRepo.save(personalBodega);

        // Registrar la acción en los logs
        logger.info("Personal de bodega ID {} trasladado de bodega '{}' a bodega '{}'",
                personalBodega.getId(),
                anteriorBodega != null ? anteriorBodega.getId() : "Sin bodega previa",
                nuevaBodega.getId());

        // Enviar notificación al personal de bodega
        emailServicio.enviarCorreo(new EmailDto(
                personalBodega.getUser().getEmail(),
                "Traslado de bodega - Store-It",
                String.format("Hola %s,\n\nHas sido asignado a la bodega '%s'.\n\nSaludos,\nEquipo Store-It",
                        personalBodega.getNombre(), nuevaBodega.getId())
        ));
    }



    /**
     * Realiza el traslado de un agente de ventas a una nueva sede.
     * <p>
     * Este método busca al agente por su ID, verifica que la nueva sede exista,
     * actualiza la relación del agente con la nueva sede y guarda los cambios.
     * Además, se registra la acción en los logs y se envía un correo de notificación
     * al agente informado sobre su nueva asignación.
     * </p>
     *
     * @param agente DTO que contiene el ID del agente y la nueva sede a la que será trasladado.
     * @throws ElementoNoEncontradoException si no se encuentra el agente o la sede indicada.
     */
    @Override
    public void trasladoSedeAgente(TrasladoSedeAgenteDto agente) throws ElementoNoEncontradoException {

        // Obtener agente por ID o lanzar excepción si no existe
        AgenteVentas agenteVentas = agenteVentasRepo.findById(agente.idAgente())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Obtener la nueva sede o lanzar excepción si no se encuentra
        Sede nuevaSede = sedeRepo.findById(agente.idSedeNueva())
                .orElseThrow(() -> new ElementoNoEncontradoException(
                        MensajeError.SEDE_NO_ENCONTRADA + agente.idSedeNueva()));

        // Obtener la sede actual del agente (puede ser null si es la primera asignación)
        Sede anteriorSede = agenteVentas.getSede();

        // Si ya está en la misma sede, evitamos el traslado innecesario
        if (anteriorSede != null && anteriorSede.getId().equals(nuevaSede.getId())) {
            logger.warn("El agente ID {} ya pertenece a la sede '{}'. No se realizó traslado.",
                    agenteVentas.getId(), nuevaSede.getNombre());
            return;
        }

        // Eliminamos al agente de la sede anterior si existe
        if (anteriorSede != null) {
            anteriorSede.getAgentesVentas().remove(agenteVentas);
            sedeRepo.save(anteriorSede);
        }

        // Asignamos la nueva sede al agente
        agenteVentas.setSede(nuevaSede);
        nuevaSede.getAgentesVentas().add(agenteVentas);

        // Guardamos cambios en la base de datos
        sedeRepo.save(nuevaSede);
        agenteVentasRepo.save(agenteVentas);

        // Registrar la acción en los logs
        logger.info("Agente ID {} trasladado de sede '{}' a sede '{}'",
                agenteVentas.getId(),
                anteriorSede != null ? anteriorSede.getNombre() : "Sin sede previa",
                nuevaSede.getNombre());

        // Enviar notificación por correo
        emailServicio.enviarCorreo(new EmailDto(
                agenteVentas.getUser().getEmail(),
                "Traslado de sede - Store-It",
                String.format("Hola %s,\n\nHas sido asignado a la sede '%s'.\n\nSaludos,\nEquipo Store-It",
                        agenteVentas.getNombre(), nuevaSede.getNombre())
        ));
    }

    /**
     * Lista los recursos humanos aplicando filtros opcionales y paginación.
     * <p>
     * Lists human resources by applying optional filters and pagination.
     *
     * @param idSede ID de la sede a filtrar. Si se proporciona, se obtendrán solo los recursos asociados a esa sede.
     *               <p>ID of the branch to filter. If provided, only resources associated with that branch will be retrieved.</p>
     * @param fechaContratacion Fecha de contratación exacta a filtrar.
     *                           <p>Exact hiring date to filter by.</p>
     * @param tipoContrato Tipo de contrato a filtrar (por ejemplo, término fijo, indefinido).
     *                     <p>Type of contract to filter (e.g., fixed-term, permanent).</p>
     * @param estadoContratoLaboral Estado actual del contrato laboral.
     *                               <p>Current employment contract status.</p>
     * @param pagina Número de página a obtener.
     *               <p>Page number to retrieve.</p>
     * @param size Tamaño de cada página.
     *             <p>Size of each page.</p>
     * @return Lista de {@link RecursosHumanosDto} que cumplen con los filtros proporcionados.
     *         <p>List of {@link RecursosHumanosDto} that match the given filters.</p>
     */
    @Override
    public List<RecursosHumanosDto> listarRecursosHumanos(String idSede, LocalDate fechaContratacion, TipoContrato tipoContrato, EstadoContratoLaboral estadoContratoLaboral, int pagina, int size) {
        // Construir objeto Pageable para aplicar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Crear predicado dinámico con filtros opcionales
        Specification<RecursosHumanos> spec = Specification.where(null);


        // Si se proporciona un, id de sede, filtrar por la sede del agente
        if (idSede != null && idSede.isEmpty()) {
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
        Page<RecursosHumanos> agentesPage = recursosHumanosRepo.findAll(spec, pageable);

        // Convertir cada entidad AgenteVentas a su respectivo DTO
        return agentesPage.getContent().stream()
                .map(recursosHumanosMapper::toDto)
                .collect(Collectors.toList());
    }
}
