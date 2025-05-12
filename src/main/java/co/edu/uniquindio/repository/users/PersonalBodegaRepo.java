package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.users.PersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import co.edu.uniquindio.model.users.base.User;
import co.edu.uniquindio.model.users.common.DatosLaborales;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repositorio encargado de gestionar las operaciones de persistencia
 * relacionadas con la entidad {@link PersonalBodega}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar operaciones CRUD básicas,
 * y de {@link JpaSpecificationExecutor} para permitir consultas dinámicas
 * utilizando especificaciones.</p>
 *
 * <p>Este repositorio facilita la interacción con la base de datos, permitiendo
 * búsquedas, inserciones, actualizaciones y eliminaciones sobre la entidad
 * de personal de bodega.</p>
 */
@Repository
public interface PersonalBodegaRepo extends JpaRepository<PersonalBodega, Long>, JpaSpecificationExecutor<PersonalBodega> {

    /**
     * Busca un miembro del personal de bodega por su correo electrónico asociado al {@link User}
     * @param email correo electrónico del usuario.
     * @return un Optional con el resultado encontrado o vacío si no existe.
     */
    Optional<PersonalBodega> findByUser_Email(String email);


    /**
     * Busca un agente de ventas por su correo empresarial asociado a sus {@link DatosLaborales}
     * @param emailEmpresarial correo empresarial del agente.
     * @return un Optional con el agente encontrado o vacío si no existe.
     */
    Optional<PersonalBodega> findByDatosLaborales_EmailEmpresarial(String emailEmpresarial);


    /**
     * Busca un miembro del personal de bodega por su teléfono principal.
     * @param telefono número telefónico principal.
     * @return un Optional con el resultado encontrado o vacío si no existe.
     */
    Optional<PersonalBodega> findByTelefono(String telefono);


    /**
     * Busca un miembro del personal de bodega por su teléfono secundario.
     * @param telefonoSecundario número telefónico secundario.
     * @return un Optional con el resultado encontrado o vacío si no existe.
     */
    Optional<PersonalBodega> findByTelefonoSecundario(String telefonoSecundario);

}