package co.edu.uniquindio.utils;

import co.edu.uniquindio.utils.service.Notify;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EnviarMensajeCorreo implements Notify {


    private String asunto,destinatario,mensaje;
    private String username,password;

    /**
     *
     * @param asunto aca va el asunto que se vera en el mensaje
     * @param destinatario aca debe ir un correo que termine en gmail y es a quien le va llegar el mensaje
     * @param mensaje el mensaje que sera enviado
     */

    public EnviarMensajeCorreo(String asunto, String destinatario, String mensaje) {
        this.asunto = asunto;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    @Override
    public void enviarNotificacion() {
        Session session =   crearSesion();
        try {
// Se crea un objeto de tipo Message
            Message message = new MimeMessage(session);
// Se configura el remitente
            message.setFrom(new InternetAddress( "prgmonavanzada@gmail.com" ));
// Se configura el destinatario
            message.setRecipients( Message.RecipientType.TO, InternetAddress.parse(this.destinatario));
// Se configura el asunto del mensaje
            message.setSubject( this.asunto );
// Se configura el mensaje a enviar
            message.setText(this.mensaje);
// Se envía el mensaje
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Session crearSesion() {
// Se definen las credenciales de la cuenta de correo
        this.username = "prgmonavanzada@gmail.com";
        this.password = "sfkhuwecmnohyvwg";  //esto es una clave de aplicacion
// Se configuran las propiedades de la conexión
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
// Se crea un objeto de tipo Authenticator
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

// Se crea la sesión
        return Session.getInstance(props, authenticator);
    }

}

