package co.edu.uniquindio.service.impl;

import co.edu.uniquindio.dto.Login.LoginDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.ClienteMapper;
import co.edu.uniquindio.model.Cliente;
import co.edu.uniquindio.model.PersonalBodega;
import co.edu.uniquindio.repository.ClienteRepo;
import co.edu.uniquindio.repository.PersonalBodegaRepository;
import co.edu.uniquindio.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indica que esta clase es un componente de servicio en Spring y puede ser inyectado donde se necesite
@RequiredArgsConstructor // Genera automáticamente un constructor con todos los atributos finales (clienteRepo, clienteMapper)
public class LoginServicioImpl implements LoginService {

    private final ClienteRepo clienteRepo;
    private final PersonalBodegaRepository personalBodegaRepo;

    @Override
    public int login(LoginDto login) throws Exception {
        // Buscar en el repositorio de Cliente
        Optional<Cliente> clienteOptional = clienteRepo.findByEmail(login.email());
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            if (cliente.getPassword().equals(login.password())) {
                return 1; // CLIENTE
            } else {
                throw new ElementoIncorrectoException("Contraseña incorrecta");
            }
        }


        // Si no se encontró el correo en ningún repositorio
        throw new ElementoNoEncontradoException("Usuario no encontrado");
    }



}
