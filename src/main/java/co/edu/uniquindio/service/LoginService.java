package co.edu.uniquindio.service;

import co.edu.uniquindio.dto.Login.LoginDto;



public interface LoginService {

    /**
     * Método para registrar una acción o evento en el sistema (logging).
     *
     * @param login Objeto de tipo LoginDto que contiene los datos a registrar.
     * @throws Exception Puede lanzar una excepción si ocurre algún error durante el proceso de registro.
     */
    int login(LoginDto login) throws Exception;


}
