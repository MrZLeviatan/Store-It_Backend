package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.email.MensajeContactoDTO;

/**
 * {@code interface} que define el contrato para el servicio {@link co.edu.uniquindio.service.utils.impl.EmailServicioImp} de envío de correos electrónicos.
 * <p>
 * Esta interfaz utiliza la librería <a href="https://www.simplejavamail.org/">Simple Java Mail</a>
 * para enviar correos a través de un servidor SMTP configurado.
 *
 * @author MrZ.Leviatan
 */
public interface EmailServicio {

    /**
     * Envía un correo electrónico con los datos proporcionados en el DTO.
     *
     * @param email Objeto {@link EmailDto} que contiene la información necesaria como el destinatario,
     *              asunto y cuerpo del mensaje.
     */
    void enviarCorreo(EmailDto email);

    /**
     * Envía un correo electrónico con los datos proporcionados en el DTO y un PDF.
     * @param emailDto Objeto {@link EmailDto} que contiene la información necesaria como el destinatario.
     * @param pdfContent Objeto generado en {@link PdfService}, sirve como PDF.
     */
    public void enviarCorreoConPdf(EmailDto emailDto, byte[] pdfContent);


    /**
     * Envía un mensaje de contacto utilizando los datos proporcionados en el DTO.
     * @param mensajeContactoDTO Objeto {@link MensajeContactoDTO} que contiene la información del mensaje,
     *                           incluyendo el nombre del remitente, email, asunto, y contenido del mensaje.
     */
    void enviarMensajeContacto(MensajeContactoDTO mensajeContactoDTO);
}
