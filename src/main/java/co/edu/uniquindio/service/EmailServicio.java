package co.edu.uniquindio.service;

import co.edu.uniquindio.dto.EmailDto;

public interface EmailServicio {


    void enviarCorreo(EmailDto email) throws Exception;
}
