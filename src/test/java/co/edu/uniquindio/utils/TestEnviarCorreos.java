package co.edu.uniquindio.utils;

import co.edu.uniquindio.utils.service.Notify;
import org.junit.jupiter.api.Test;

public class TestEnviarCorreos {
    @Test
    public void enviarCorreo() {

        // declaramos el asunto del mensaje
        String asunto= "Prueba de enviar mensaje";
        //declaramos a quien se le enviara el mensaje (debe terminar con un @gmail.com )
        String destinatario = "andrey3681.ay@gmail.com";
        // declaramos el mensaje
        String mensaje = "hola esto es una prueba ";
        // creamos una instancia de la interfaz Notify que se encuentra en el paquete utils.service
        Notify notify;
        //la inicializamos con el constructuro de la clase EnviarMensajeCorreo
        // y le damos los atributos anteriormente creados
        notify= new EnviarMensajeCorreo(destinatario,asunto,mensaje);
        //llamamos el metodo enviar notificacion de la interface y se envia el correo
        notify.enviarNotificacion();
    }
}
