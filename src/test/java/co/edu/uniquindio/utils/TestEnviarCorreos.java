package co.edu.uniquindio.utils;


import co.edu.uniquindio.dto.EmailDto;
import co.edu.uniquindio.service.EmailServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestEnviarCorreos {
    @Autowired
    EmailServicio emailServicio;

    @Test
    public void enviarCorreo() throws Exception {

        // declaramos el asunto del mensaje
        String asunto= "Prueba de enviar mensaje";
        //declaramos a quien se le enviara el mensaje (debe terminar con un @gmail.com )
        String destinatario = "andrey3681.ay@gmail.com";
        // declaramos el mensaje
        String mensaje = "hola esto es una prueba ";
        // creamos una instancia de la interfaz Notify que se encuentra en el paquete utils.service
        emailServicio.enviarCorreo(new EmailDto(asunto,mensaje,destinatario));
    }
}
