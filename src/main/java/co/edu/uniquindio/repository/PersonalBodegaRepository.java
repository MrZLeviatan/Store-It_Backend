/*
*Permite realizar operaciones sin anotar en la bd
 */
package co.edu.uniquindio.repository;

import co.edu.uniquindio.model.PersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface PersonalBodegaRepository extends JpaRepository<PersonalBodega, String> {
    // 🔹 Buscar personal con un correo específico
    Optional<PersonalBodega> findByEmail(String gmail);

    // 🔹 Buscar personal con una cedula específico
    Optional<PersonalBodega> findById(String id);
}