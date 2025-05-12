package co.edu.uniquindio.repository.users;


import co.edu.uniquindio.model.users.RecursosHumanos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import co.edu.uniquindio.model.users.base.User;
import co.edu.uniquindio.model.users.common.DatosLaborales;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio encargado de gestionar las operaciones de persistencia
 * relacionadas con la entidad {@link RecursosHumanos}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar operaciones CRUD básicas,
 * y de {@link JpaSpecificationExecutor} para permitir consultas dinámicas
 * utilizando especificaciones.</p>
 *
 * <p>Este repositorio permite realizar búsquedas, inserciones, actualizaciones y eliminaciones
 * sobre el personal del área de recursos humanos dentro del sistema.</p>
 *
 */
@Repository
public interface RecursosHumanosRepo extends JpaRepository<RecursosHumanos, Long>, JpaSpecificationExecutor<RecursosHumanos> {

    /**
     * Busca un recurso humano a partir del correo electrónico registrado
     * en el objeto embebido {@link User}.
     * @param email Correo electrónico del recurso humano.
     * @return Un {@link Optional} que contiene el recurso humano si se encuentra, o vacío si no.
     */
    Optional<RecursosHumanos> findByUser_Email(String email);


    /**
     * Busca un recurso humano a partir del correo electrónico empresarial registrado
     * en el objeto embebido {@link DatosLaborales}.
     * @param emailEmpresarial Correo electrónico empresarial del recurso humano.
     * @return Un {@link Optional} que contiene el recurso humano si se encuentra, o vacío si no.
     */
    Optional<RecursosHumanos> findByDatosLaborales_EmailEmpresarial(String emailEmpresarial);


    /**
     * Busca un recurso humano por su número de teléfono principal.
     * @param telefono Número de teléfono principal del recurso humano.
     * @return Un {@link Optional} que contiene el recurso humano si se encuentra, o vacío si no.
     */
    Optional<RecursosHumanos> findByTelefono(String telefono);

    /**
     * Busca un recurso humano por su número de teléfono secundario
     * @param telefonoSecundario Número de teléfono secundario del recurso humano.
     * @return Un {@link Optional} que contiene el recurso humano si se encuentra, o vacío si no.
     */
    Optional<RecursosHumanos> findByTelefonoSecundario(String telefonoSecundario);


}
