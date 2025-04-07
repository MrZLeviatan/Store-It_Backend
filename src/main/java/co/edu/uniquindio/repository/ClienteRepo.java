package co.edu.uniquindio.repository;

import co.edu.uniquindio.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, String> {

    // 🔹 Buscar clientes con un correo específico
    Optional<Cliente> findByEmail(String gmail);

    // 🔹 Buscar clientes con una cedula específico
    Optional<Cliente> findByCedula(String cedula);

}
