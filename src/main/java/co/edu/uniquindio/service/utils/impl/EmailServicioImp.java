package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.email.MensajeContactoDTO;
import co.edu.uniquindio.exception.CargaFallidaException;
import co.edu.uniquindio.service.utils.EmailServicio;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;




/**
 * Implementación del {@code Service} de envío de correos electrónicos utilizando la librería
 * <a href="https://www.simplejavamail.org/">Simple Java Mail</a>.
 * Permite enviar correos de forma asíncrona configurando el servidor SMTP mediante propiedades.
 *
 * @author MrZ. Leviatan
 */
@Service
public class EmailServicioImp implements EmailServicio {

    @Value("${smtp.host}")
    private String host;  // Dirección del servidor SMTP

    @Value("${smtp.port}")
    private String port; // Puerto del servidor SMTP

    @Value("${smtp.user}")
    private String user; // Correo electrónico del remitente (usado como autenticación)

    @Value("${smtp.password}")
    private String password;  // Contraseña del remitente

    /**
     * Envía un correo electrónico de forma asíncrona utilizando los datos proporcionados en el DTO.
     * Utiliza Simple Java Mail para construir y enviar el mensaje.
     *
     * @param emailDto Objeto que contiene la información del correo: destinatario, asunto y cuerpo.
     * @throws CargaFallidaException Si ocurre un error al construir o enviar el correo electrónico.
     */
    @Override
    @Async
    public void enviarCorreo(EmailDto emailDto) {
        try {
            // Construcción del correo electrónico
            Email email = EmailBuilder.startingBlank()
                    .from(user)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withPlainText(emailDto.cuerpo())
                    .buildEmail();

            // Creación del mailer con la configuración SMTP y envío del correo
            try (Mailer mailer = MailerBuilder
                    .withSMTPServer(host, Integer.parseInt(port), user, password)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withDebugLogging(true)
                    .buildMailer()) {
                mailer.sendMail(email);}

        } catch (Exception e) {
            // Lanza excepción personalizada si algo falla al construir o enviar el correo
            throw new CargaFallidaException(MensajeError.ERROR_ENVIO_CORREO + emailDto.destinatario(), e);
        }}


    /**
     * Envía un correo electrónico de forma asíncrona con el PDF adjunto.
     *
     * @param emailDto Objeto que contiene la información del correo: destinatario, asunto y cuerpo.
     * @param pdfContent El contenido del PDF generado en bytes.
     * @throws CargaFallidaException Si ocurre un error al construir o enviar el correo electrónico.
     */
    @Override
    @Async
    public void enviarCorreoConPdf(EmailDto emailDto, byte[] pdfContent) {
        try {
            // Crear el DataSource a partir del byte array del PDF
            DataSource dataSource = new ByteArrayDataSource(pdfContent, "application/pdf");

            // Construcción del correo electrónico
            Email email = EmailBuilder.startingBlank()
                    .from(user)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withPlainText(emailDto.cuerpo())
                    .withAttachment("Contrato_Servicio.pdf", dataSource) // Usamos DataSource
                    .buildEmail();

            // Creación del mailer con la configuración SMTP y envío del correo
            try (Mailer mailer = MailerBuilder
                    .withSMTPServer(host, Integer.parseInt(port), user, password)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withDebugLogging(true)
                    .buildMailer()) {
                mailer.sendMail(email);}

        } catch (Exception e) {
            // Lanza excepción personalizada si algo falla al construir o enviar el correo
            throw new CargaFallidaException(MensajeError.ERROR_ENVIO_CORREO + emailDto.destinatario(), e);
        }}


    /**
     * Envía un mensaje de contacto de un cliente a la dirección fija de Store-It.
     * Aunque el remitente real es el correo SMTP autenticado, se incluye la información del cliente.
     *
     * @param mensajeContactoDTO DTO con los datos del mensaje enviado desde el formulario.
     */
    @Override
    public void enviarMensajeContacto(MensajeContactoDTO mensajeContactoDTO) {
        try {
            String cuerpo = "📌 Nombre: " + mensajeContactoDTO.nombre() + "\n"
                    + "📧 Email: " + mensajeContactoDTO.email() + "\n\n"
                    + "📝 Mensaje:\n" + mensajeContactoDTO.mensaje();

            Email email = EmailBuilder.startingBlank()
                    .from(user)  // siempre desde el correo autenticado
                    .to("storeit771@gmail.com") // o cualquier correo receptor fijo
                    .withReplyTo(mensajeContactoDTO.email()) // permite responderle al cliente
                    .withSubject("📩 Contacto: " + mensajeContactoDTO.asunto())
                    .withPlainText(cuerpo)
                    .buildEmail();

            try (Mailer mailer = MailerBuilder
                    .withSMTPServer(host, Integer.parseInt(port), user, password)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withDebugLogging(true)
                    .buildMailer()) {
                mailer.sendMail(email);
            }
        } catch (Exception e) {
            throw new CargaFallidaException("Error al enviar mensaje de contacto: " + mensajeContactoDTO.email(), e);
        }
    }

}