package co.edu.uniquindio.service.objects.Impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.objects.contrato.ContratoDto;
import co.edu.uniquindio.dto.objects.contrato.CrearContratoDto;
import co.edu.uniquindio.dto.objects.contrato.EditarContratoDto;
import co.edu.uniquindio.exception.ElementoAunEnUsoException;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoActivadoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.ContratoMapper;
import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.enums.EstadoBodega;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import co.edu.uniquindio.model.users.base.enums.EstadoCuenta;
import co.edu.uniquindio.repository.objects.ContratoRepo;
import co.edu.uniquindio.repository.objects.EspacioRepo;
import co.edu.uniquindio.repository.users.AgenteVentasRepo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.objects.ContratoServicio;
import co.edu.uniquindio.service.utils.EmailServicio;
import co.edu.uniquindio.service.utils.PdfService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Implementación del servicio de contratos para el sistema Store-It.
 *
 * <p>
 * Esta clase se encarga de la lógica relacionada con la gestión de contratos,
 * incluyendo su creación, firma por parte del cliente y del agente de ventas,
 * y el envío de correos electrónicos con el contrato en formato PDF adjunto.
 * </p>
 *
 * <p>
 * Funcionalidades principales:
 * <ul>
 *     <li>Validación de clientes, agentes de ventas y espacios antes de crear un contrato.</li>
 *     <li>Generación de contrato en PDF utilizando {@link PdfService}.</li>
 *     <li>Notificación vía correo electrónico a través de {@link EmailServicio}.</li>
 *     <li>Control del estado del contrato según las firmas de los involucrados.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Esta clase implementa la interfaz {@link ContratoServicio} y está marcada como un {@link Service},
 * lo que permite su inyección automática mediante Spring.
 * </p>
 *
 * <p>
 * Además, utiliza la anotación {@link RequiredArgsConstructor} de Lombok para inyectar las dependencias
 * necesarias mediante constructor, promoviendo la inmutabilidad.
 */

@Service
@RequiredArgsConstructor
public class ContratoServicioImpl implements ContratoServicio {


    private final ContratoRepo contratoRepo;
    private final ContratoMapper contratoMapper;
    private final ClienteRepo clienteRepo;
    private final AgenteVentasRepo agenteVentasRepo;
    private final EspacioRepo espacioRepo;
    private final PdfService pdfService;
    private final EmailServicio emailServicio;
    private static final Logger logger = LoggerFactory.getLogger(ContratoServicioImpl.class);


    /**
     * Crea un nuevo contrato en el sistema a partir del DTO recibido.
     * <p>
     * Este método valida que el cliente, el agente de ventas y el espacio asociados al contrato existan
     * y se encuentren activos o disponibles antes de proceder con la creación.
     * Si alguna validación falla, se lanzan excepciones correspondientes.
     * <p>
     * Una vez creado el contrato, se genera un archivo PDF con los detalles del contrato y
     * se envía un correo al agente de ventas con el contrato adjunto.
     *
     * @param contratoDto DTO con los datos necesarios para crear el contrato.
     * @throws ElementoNoEncontradoException si el cliente, agente de ventas o espacio no existen.
     * @throws ElementoAunEnUsoException si el espacio no se encuentra disponible (no está en estado LIBRE).
     * @throws ElementoNoActivadoException si el cliente o el agente de ventas no están activos.
     */
    @Override
    public void crearContrato(CrearContratoDto contratoDto)
            throws ElementoNoEncontradoException{

        // Validamos la existencia del cliente
        Cliente cliente = clienteRepo.findByUser_Email(contratoDto.emailCliente())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Validamos la existencia del agente de ventas
        AgenteVentas agenteVentas = agenteVentasRepo.findById(contratoDto.idAgenteVentas())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));

        // Validamos la existencia del espacio
        Espacio espacio = espacioRepo.findById(contratoDto.idEspacio())
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.ESPACIO_NO_ENCONTRADO));

        // Verificamos que el espacio esté libre para asignarlo al contrato
        if (espacio.getEstadoEspacio() != EstadoEspacio.LIBRE){
            throw new ElementoAunEnUsoException(MensajeError.ESPACIO_NO_DISPONIBLE);}

        // Verificamos que la bodega está activa
        if (espacio.getBodega().getEstadoBodega() != EstadoBodega.ACTIVA){
            throw new ElementoNoActivadoException(MensajeError.BODEGA_NO_ACTIVA);}

        // Verificamos si el cliente está activo
        if (!cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new ElementoNoActivadoException(MensajeError.PERSONA_NO_ACTIVADA);}

        // Verificamos si el agente está activo
        if (!agenteVentas.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new ElementoNoActivadoException(MensajeError.PERSONA_NO_ACTIVADA);}

        // Convertimos el DTO a la entidad Contrato usando el mapper
        Contrato contrato = contratoMapper.toEntity(contratoDto);
        contrato.setCliente(cliente);
        contrato.setEspacio(espacio);
        contrato.setAgenteVentas(agenteVentas);

        // Guardamos todos los cambios.
        contratoRepo.save(contrato);

        cliente.getContratos().add(contrato);
        clienteRepo.save(cliente);

        agenteVentas.getContratos().add(contrato);
        agenteVentasRepo.save(agenteVentas);

        espacio.setEstadoEspacio(EstadoEspacio.CONTRATADO_DISPONIBLE); // o el valor correcto del enum
        espacio.setContrato(contrato);
        espacioRepo.save(espacio); // Guardar el nuevo estado


        // Creamos el mensaje de correo para el agente de ventas con notificación del contrato adjunto
        EmailDto emailAgente = new EmailDto(
                agenteVentas.getUser().getEmail(),
                "Bienvenido a Store-It - Contrato Adjunto",
                String.format(
                        "Hola %s,\n\n" +
                                "Nos complace darte la bienvenida al equipo de Store-It como Agente de Ventas.\n\n" +
                                "Adjunto encontrarás el contrato correspondiente a tu incorporación. Por favor, revísalo cuidadosamente y no dudes en contactarnos si tienes alguna pregunta o necesitas más información.\n\n" +
                                "Estamos seguros de que harás un excelente trabajo. ¡Muchos éxitos!\n\n" +
                                "Saludos cordiales,\n" +
                                "Equipo Store-It",
                        agenteVentas.getNombre()));

        // Creamos el mensaje de correo para el cliente con notificación del contrato.
        EmailDto emailCliente = new EmailDto(
                contrato.getCliente().getUser().getEmail(),
                "Store-It - Firma de Contrato de Servicio Requerida",
                String.format(
                        "Hola %s,\n\n" +
                                "Gracias por confiar en Store-It para el almacenamiento de tus productos.\n\n" +
                                "Adjunto a este correo encontrarás tu contrato de servicio. Por favor, revísalo y asegúrate de que toda la información esté correcta.\n\n" +
                                "Recuerda que debes **firmar digitalmente el contrato dentro de las próximas 24 horas** para que podamos activar tu servicio. Si tienes alguna duda o requerimiento, no dudes en contactarnos.\n\n" +
                                "Esperamos brindarte un excelente servicio.\n\n" +
                                "Saludos cordiales,\n" +
                                "Equipo Store-It",
                        contrato.getCliente().getNombre()));

        // Generamos el PDF del contrato y enviamos el correo
        byte[] pdfContent = pdfService.generarContratoPdf(contrato);
        emailServicio.enviarCorreoConPdf(emailAgente, pdfContent);
        emailServicio.enviarCorreoConPdf(emailCliente, pdfContent);

        logger.info("Contrato creado exitosamente para cliente: {}", cliente.getNombre());
    }


    /**
     * Tarea programada que se ejecuta cada hora para verificar los contratos pendientes.
     *
     * Revisa todos los contratos en estado "PENDIENTE_VERIFICACION" y si ha pasado al menos
     * un día desde la fecha de inicio y el cliente no ha firmado, el contrato se cancela automáticamente.
     * Al cancelar el contrato:
     * - Se libera el espacio asociado.
     * - Se notifica al cliente mediante correo electrónico.
     * - Se notifica al agente de ventas mediante correo electrónico.
     */
    @Scheduled(cron = "0 0 * * * *") // Ejecutar cada hora (al minuto 0 de cada hora)
    public void verificarContratosPendientes() {
        // Obtener la fecha límite: un día antes de la fecha actual
        LocalDate limite = LocalDate.now().minusDays(1); // Ya pasó al menos 1 día

        // Obtener todos los contratos con estado "PENDIENTE_VERIFICACION"
        List<Contrato> contratosPendientes = contratoRepo.findByEstadoContrato(EstadoContrato.PENDIENTE_VERIFICACION);

        // Recorrer todos los contratos pendientes
        for (Contrato contrato : contratosPendientes) {

            // Verificar si la fecha de inicio es anterior a la fecha límite
            // y el cliente aún no ha firmado
            if (contrato.getFechaInicio().isBefore(limite)
                    && contrato.getFirmaCliente() == null) {

                // Cambiar el estado del contrato a "CANCELADO"
                contrato.setEstadoContrato(EstadoContrato.CANCELADO);
                contratoRepo.save(contrato); // Guardar los cambios

                // Liberar el espacio asociado al contrato
                Espacio espacio = contrato.getEspacio();
                espacio.setEstadoEspacio(EstadoEspacio.LIBRE); // Marcar como libre
                espacio.setContrato(null); // Eliminar referencia al contrato
                espacioRepo.save(espacio); // Guardar los cambios del espacio

                // Notificar al cliente mediante correo
                Cliente cliente = contrato.getCliente();
                EmailDto correoCliente = new EmailDto(
                        cliente.getUser().getEmail(), // Correo del cliente
                        "Cancelación de Contrato - Store-It", // Asunto
                        String.format("Hola %s,\n\nTu contrato fue cancelado automáticamente por no ser confirmado en las últimas 24 horas.\n\n" +
                                        "Puedes iniciar un nuevo proceso en cualquier momento.\n\nAtentamente,\nEquipo Store-It",
                                cliente.getNombre()) // Cuerpo del mensaje
                );
                emailServicio.enviarCorreo(correoCliente); // Enviar el correo al cliente

                // Notificar al agente de ventas mediante correo
                AgenteVentas agente = contrato.getAgenteVentas();
                EmailDto correoAgente = new EmailDto(
                        agente.getUser().getEmail(), // Correo del agente
                        "Cancelación de Contrato Asignado - Store-It", // Asunto
                        String.format("Hola %s,\n\nEl contrato con el cliente %s ha sido cancelado por falta de confirmación en 24 horas.\n\n" +
                                        "Te recomendamos contactar al cliente si deseas reactivar el proceso.\n\nEquipo Store-It",
                                agente.getNombre(), cliente.getNombre()) // Cuerpo del mensaje
                );
                emailServicio.enviarCorreo(correoAgente); // Enviar el correo al agente

                // Registrar en los logs la cancelación del contrato
                logger.info("Contrato ID {} cancelado por falta de firma del cliente.", contrato.getId());
            }
        }
    }


    /**
     * Permite que un cliente firme digitalmente un contrato existente.
     * <p>
     * Este método busca el contrato por su ID y le asigna la imagen de la firma proporcionada por el cliente.
     * Una vez firmado, el estado del contrato se actualiza a "VERIFICADO_POR_CLIENTE" y se genera un PDF actualizado,
     * el cual es enviado por correo electrónico al agente de ventas asignado al contrato.
     *
     * @param idContrato ID del contrato a firmar.
     * @param firmaImagen Archivo que contiene la firma del cliente en formato imagen.
     * @throws ElementoNoEncontradoException Si no se encuentra un contrato con el ID proporcionado.
     * @throws RuntimeException Si ocurre un error al procesar el archivo de firma.
     */
    @Override
    public void firmarContratoPorCliente(Long idContrato, MultipartFile firmaImagen) throws ElementoNoEncontradoException {

        Contrato contrato = contratoRepo.findById(idContrato)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.CONTRATO_NO_ENCONTRADO));

        try {

            if (!contrato.getEstadoContrato().equals(EstadoContrato.PENDIENTE_VERIFICACION)) {
                throw new ElementoNoActivadoException(MensajeError.CONTRATO_NO_VERIFICADO);
            }

            contrato.setFirmaCliente(firmaImagen.getBytes());
            contrato.setEstadoContrato(EstadoContrato.VERIFICADO_POR_CLIENTE); // Marcamos como verificado
            byte[] pdfContent = pdfService.generarContratoPdf(contrato);

            EmailDto emailDto = new EmailDto(
                    contrato.getAgenteVentas().getUser().getEmail(),
                    "Store-It - Contrato firmado por el cliente",
                    String.format("Hola %s,\n\nNos complace informarte que el cliente ha firmado el contrato correspondiente.\n\n" +
                                    "Puedes encontrar el contrato firmado adjunto a este correo. Por favor, revisa que todo esté en orden y continúa con el proceso de asignación o seguimiento según corresponda.\n\n" +
                                    "Gracias por tu compromiso y dedicación.\n\nSaludos cordiales,\nEquipo Store-It",
                            contrato.getAgenteVentas().getNombre()));

            emailServicio.enviarCorreoConPdf(emailDto, pdfContent);

            // Salvamos en la base de datos.
            contratoRepo.save(contrato);

            logger.info("Se firma el contrato {} Por el cliente {}", contrato.getId(), contrato.getCliente().getNombre());

        } catch (IOException e) {

            throw new RuntimeException("Error al procesar la firma del cliente", e);
        }
    }


    /**
     * Firma el contrato por parte del agente de ventas y activa el contrato.
     * <p>
     * Este método valida que el contrato ya haya sido firmado previamente por el cliente.
     * Luego, agrega la firma del agente, cambia el estado del contrato a ACTIVO,
     * genera el PDF actualizado y envía un correo de notificación al cliente con el contrato adjunto.
     *
     * @param idContrato ID del contrato a firmar por el agente de ventas.
     * @param firmaImagen Archivo con la firma del agente en formato imagen.
     * @throws ElementoNoEncontradoException Si no se encuentra el contrato con el ID proporcionado.
     * @throws ElementoNoActivadoException Si el contrato aún no ha sido firmado por el cliente.
     * @throws RuntimeException Si ocurre un error al procesar la firma del agente.
     */
    @Override
    public void firmarContratoPorAgente(Long idContrato, MultipartFile firmaImagen) throws ElementoNoEncontradoException {
        Contrato contrato = contratoRepo.findById(idContrato)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.CONTRATO_NO_ENCONTRADO));

        try {

            if (contrato.getEstadoContrato() != EstadoContrato.VERIFICADO_POR_CLIENTE) {
                throw new ElementoNoActivadoException(MensajeError.CONTRATO_NO_FIRMADO_CLIENTE);
            }

            contrato.setFirmaAgenteVentas(firmaImagen.getBytes());
            contrato.setEstadoContrato(EstadoContrato.ACTIVO);

            EmailDto emailDto = new EmailDto(
                    contrato.getCliente().getUser().getEmail(),
                    "Store-It - Contrato Activado",
                    String.format("Hola %s,\n\nTe informamos que tu contrato con Store-It ha sido activado exitosamente y ya está en marcha.\n\n" +
                                    "A partir de ahora puedes hacer uso del espacio asignado y contar con todos los beneficios de nuestro servicio.\n" +
                                    "Si tienes alguna pregunta o necesitas asistencia, no dudes en comunicarte con nosotros.\n\n" +
                                    "¡Gracias por confiar en Store-It!\n\nSaludos cordiales,\nEquipo Store-It",
                            contrato.getCliente().getNombre()));

            EmailDto emailAgente = new EmailDto(
                    contrato.getAgenteVentas().getUser().getEmail(),
                    "Store-It - Contrato Activado con Cliente",
                    String.format(
                            "Hola %s,\n\n" +
                                    "Te informamos que el contrato con el cliente **%s** ha sido activado exitosamente.\n\n" +
                                    "A partir de este momento, el cliente puede hacer uso del espacio asignado y acceder a todos los beneficios del servicio.\n" +
                                    "Gracias por tu gestión y compromiso con Store-It.\n\n" +
                                    "Si necesitas hacer seguimiento o brindar soporte adicional, recuerda que puedes acceder a la plataforma para ver los detalles.\n\n" +
                                    "Saludos cordiales,\n" +
                                    "Equipo Store-It",
                            contrato.getAgenteVentas().getNombre(),
                            contrato.getCliente().getNombre()
                    )
            );

            byte[] pdfContent = pdfService.generarContratoPdf(contrato);
            emailServicio.enviarCorreoConPdf(emailDto, pdfContent);
            emailServicio.enviarCorreoConPdf(emailAgente, pdfContent);

            contrato.getCliente().getContratos().add(contrato);

            contratoRepo.save(contrato);
            logger.info("Contrato ID {} guardado exitosamente tras firma del agente.", idContrato);

        }catch (IOException e) {

            logger.error("Error al procesar la firma del agente de ventas", e);
            throw new RuntimeException("Error al procesar la firma del agente de ventas", e);
        }
    }


    /**
     * Edita un contrato existente si aún no ha sido verificado por el cliente.
     * <p>
     * Solo se permite la edición de contratos cuyo estado es {@code PENDIENTE_VERIFICACION}.
     * Luego de aplicar los cambios, se notifica al cliente mediante un correo electrónico que contiene el contrato actualizado en formato PDF.
     *
     * @param contratoDto DTO con los nuevos datos del contrato a editar.
     * @throws ElementoNoEncontradoException Si el contrato no existe.
     * @throws ElementoAunEnUsoException Si el contrato ya ha sido verificado y no puede ser editado.
     */
    @Override
    public void editarContrato(EditarContratoDto contratoDto) throws ElementoNoEncontradoException {

        Contrato contrato = obtenerContrato(contratoDto.id());

        if (contrato.getEstadoContrato() != EstadoContrato.PENDIENTE_VERIFICACION){
            throw new ElementoAunEnUsoException(MensajeError.CONTRATO_EN_USO);
        }

        contratoMapper.toEntity(contratoDto,contrato);

        // Ya está relacionado con el cliente y agente, no es necesario volver a agregarlo
        contratoRepo.save(contrato);


        EmailDto emailDto = new EmailDto(
                contrato.getCliente().getUser().getEmail(),
                "Store-It - Contrato Modificado",
                String.format("Hola %s,\n\nTe informamos que tu contrato con Store-It ha sido modificado recientemente.\n\n" +
                                "Por favor revisa los cambios realizados. Si tienes alguna duda o necesitas más información, no dudes en comunicarte con nuestro equipo de soporte.\n\n" +
                                "Gracias por confiar en Store-It.\n\nSaludos cordiales,\nEquipo Store-It",
                        contrato.getCliente().getNombre())
        );
        byte[] pdfContent = pdfService.generarContratoPdf(contrato);
        emailServicio.enviarCorreoConPdf(emailDto, pdfContent);
        logger.info("Contrato {} modificado y enviado al cliente {}", contrato.getId(), contrato.getCliente().getNombre());
    }


    /**
     * Obtiene un contrato por su ID.
     * <p>
     * Si no se encuentra el contrato con el ID proporcionado, lanza una excepción personalizada.
     *
     * @param idContrato ID del contrato a buscar.
     * @return El contrato correspondiente al ID proporcionado.
     * @throws ElementoNoEncontradoException Si no se encuentra el contrato.
     */
    public Contrato obtenerContrato(Long idContrato) throws ElementoNoEncontradoException {
        return contratoRepo.findById(idContrato)
                .orElseThrow(()-> new ElementoNoEncontradoException(MensajeError.CONTRATO_NO_ENCONTRADO));
    }


    /**
     * Obtiene un contrato por su ID y lo convierte a DTO.
     * <p>
     * Este método busca un contrato en la base de datos por su ID. Si lo encuentra,
     * lo transforma a un objeto {@link ContratoDto} utilizando el mapper correspondiente.
     *
     * @param id ID del contrato que se desea obtener.
     * @return Un {@link ContratoDto} con los datos del contrato encontrado.
     * @throws ElementoNoEncontradoException Si no se encuentra un contrato con el ID especificado.
     */
    @Override
    public ContratoDto obtenerContratoId(Long id) throws ElementoNoEncontradoException {
        return contratoMapper.toDTO(obtenerContrato(id));
    }


    /**
     * Cancela un contrato existente si se encuentra en estado ACTIVO.
     * <p>
     * Este método actualiza el estado del contrato a CANCELADO, lo guarda en la base de datos,
     * genera un documento PDF del contrato cancelado y envía una notificación al cliente
     * adjuntando dicho PDF por correo electrónico.
     * </p>
     *
     * @param id el identificador único del contrato que se desea cancelar
     * @throws ElementoNoEncontradoException si no se encuentra un contrato con el ID proporcionado
     * @throws ElementoIncorrectoException si el contrato no se encuentra en estado ACTIVO y, por lo tanto, no puede ser cancelado
     */
    @Override
    public void cancelarContrato(Long id) throws ElementoNoEncontradoException, ElementoIncorrectoException {
        Contrato contrato = obtenerContrato(id);

        if(contrato.getEstadoContrato().equals(EstadoContrato.PENDIENTE_VERIFICACION)){
            contrato.setEstadoContrato(EstadoContrato.CANCELADO);
            contratoRepo.save(contrato);
        }

        if (contrato.getEstadoContrato().equals(EstadoContrato.ACTIVO)){

            contrato.setEstadoContrato(EstadoContrato.CANCELADO);
            contratoRepo.save(contrato);

            byte[] pdfContent = pdfService.generarAvisoDeudaPdf(contrato);

            EmailDto emailDto = new EmailDto(
                    contrato.getCliente().getUser().getEmail(),
                    "Store-It - Contrato Cancelado",
                    String.format("Hola %s,\n\nLamentamos informarte que tu contrato con Store-It ha sido cancelado.\n\n" +
                                    "Adjunto a este correo encontrarás una copia del contrato cancelado en formato PDF para tu referencia.\n\n" +
                                    "Si tienes alguna pregunta o necesitas más información sobre esta decisión, no dudes en comunicarte con nuestro equipo de soporte.\n\n" +
                                    "Agradecemos el tiempo que compartiste con nosotros.\n\nSaludos cordiales,\nEquipo Store-It",
                            contrato.getCliente().getNombre()));

            emailServicio.enviarCorreoConPdf(emailDto, pdfContent);
            logger.info("El Contrato {} ha sido cancelado", contrato.getId());

        }else {
            throw new ElementoIncorrectoException(MensajeError.CONTRATO_NO_ACTIVO);
        }
    }


    /**
     * Lista los contratos de un cliente filtrando por fecha de inicio y estado del contrato, con soporte para paginación.
     *
     * @param clienteId ID del cliente (obligatorio)
     * @param fechaInicio (opcional) fecha de inicio del contrato
     * @param estadoContrato (opcional) estado del contrato
     * @param pagina número de la página (empieza en 0)
     * @param size tamaño de la página
     * @return lista de contratos como DTOs
     */
    @Override
    public List<ContratoDto> obtenerContratoCliente(Long clienteId,
                                                    LocalDate fechaInicio,
                                                    EstadoContrato estadoContrato,
                                                    int pagina, int size) {

        // Objeto Pageable para gestionar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Construir especificación dinámica
        Specification<Contrato> spec = Specification.where((root, query, builder) ->
                builder.equal(root.get("cliente").get("id"), clienteId)); // Filtro obligatorio por cliente

        // Filtro opcional: fecha de inicio
        if (fechaInicio != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("fechaInicio").as(LocalDate.class), fechaInicio));
        }

        // Filtro opcional: estado del contrato
        if (estadoContrato != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoContrato"), estadoContrato));
        }

        // Consultar y mapear resultados a DTO
        Page<Contrato> contratos = contratoRepo.findAll(spec, pageable);
        return contratos.getContent()
                .stream()
                .map(contratoMapper::toDTO)
                .collect(Collectors.toList());
    }


    /**
     * Lista los contratos gestionados por un agente de ventas, filtrando por fecha de inicio y estado del contrato.
     * Incluye soporte para paginación.
     *
     *
     * @param idAgenteVentas ID del agente de ventas (obligatorio)
     * @param fechaInicio (opcional) fecha de inicio del contrato
     * @param estadoContrato (opcional) estado del contrato
     * @param pagina número de la página (empieza en 0)
     * @param size tamaño de la página
     * @return lista de contratos como DTOs
     */
    @Override
    public List<ContratoDto> obtenerContratoAgenteVentas(Long idAgenteVentas,
                                                         LocalDate fechaInicio,
                                                         EstadoContrato estadoContrato,
                                                         int pagina, int size) {
        // Objeto para paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // Construcción de especificación dinámica
        Specification<Contrato> spec = Specification.where((root, query, builder) ->
                builder.equal(root.get("agenteVentas").get("id"), idAgenteVentas)); // Filtro por agente

        // Filtro opcional: fecha de inicio
        if (fechaInicio != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("fechaInicio").as(LocalDate.class), fechaInicio));
        }

        // Filtro opcional: estado del contrato
        if (estadoContrato != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoContrato"), estadoContrato));
        }

        // Consulta y conversión a DTO
        Page<Contrato> contratos = contratoRepo.findAll(spec, pageable);
        return contratos.getContent()
                .stream()
                .map(contratoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
