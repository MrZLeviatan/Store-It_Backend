package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.users.AgenteVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import co.edu.uniquindio.model.users.base.User;
import co.edu.uniquindio.model.users.common.DatosLaborales;

import java.util.List;
import java.util.Optional;


/**
 * Repositorio para gestionar operaciones de acceso a datos sobre la entidad {@link AgenteVentas}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar métodos CRUD básicos como guardar, buscar,
 * actualizar y eliminar agentes de ventas en la base de datos.</p>
 *
 * <p>También extiende de {@link JpaSpecificationExecutor}, lo cual permite ejecutar consultas
 * avanzadas y dinámicas utilizando la API de especificaciones de JPA (Specifications), ideal
 * para aplicar filtros personalizados y combinables en tiempo de ejecución.</p>
 *
 * <p>Este repositorio es una parte fundamental en la capa de persistencia del módulo de gestión comercial.</p>
 */
@Repository
public interface AgenteVentasRepo extends JpaRepository<AgenteVentas, Long> , JpaSpecificationExecutor<AgenteVentas> {


    /**
     * Busca un agente de ventas a partir del correo electrónico registrado
     * en el objeto embebido {@link User}.
     *
     * @param email Correo electrónico del agente de ventas.
     * @return Un {@link Optional} que contiene el agente de ventas si se encuentra, o vacío si no.
     */
    Optional<AgenteVentas> findByUser_Email(String email);


    /**
     * Busca un agente de ventas a partir del correo electrónico empresarial registrado
     * en el objeto embebido {@link DatosLaborales}
     * @param emailEmpresarial correo electrónico empresarial del agente de ventas
     * @return Un {@link Optional} que contiene el agente de ventas si se encuentra, o vacío si no.
     */
    Optional<AgenteVentas> findByDatosLaborales_EmailEmpresarial(String emailEmpresarial);


    /**
     * Busca un agente de ventas por su número de teléfono principal.
     * @param telefono Número de teléfono principal del agente de ventas
     * @return Un {@link Optional} que contiene el agente de ventas si se encuentra, o vacío si no.
     */
    Optional<AgenteVentas> findByTelefono(String telefono);


    /**
     * Busca un agente de ventas por su número de teléfono secundario.
     * @param telefonoSecundario Número de teléfono secundario del agente de ventas
     * @return Un {@link Optional} que contiene el agente de ventas si se encuentra, o vacío si no.
     */
    Optional<AgenteVentas> findByTelefonoSecundario(String telefonoSecundario);


    /**
     * Recupera una lista de entidades {@link AgenteVentas} ordenadas por el número de entidades {@link co.edu.uniquindio.model.objects.Contrato} asociadas.
     * en orden descendente. Los agentes se agrupan por su identificador único, y el número de contratos determina
     * el orden de clasificación.
     * @return Una {@link List<AgenteVentas>} de entidades {@link AgenteVentas} ordenadas por el número de contratos en orden descendente.
     */
    @Query("SELECT a FROM AgenteVentas a JOIN a.contratos c GROUP BY a.id ORDER BY COUNT(c) DESC")
    List<AgenteVentas> findAllOrderedByContratos();

    /**
     * Recupera una lista de agentes de ventas que no están actualmente asociados a sesiones
     * de chat activas en el sistema.
     * <p>
     * La consulta selecciona todos los agentes de ventas desde la entidad {@link AgenteVentas}
     * que no tienen ninguna asociación con una sesión de chat activa (estado = 'ACTIVA')
     * en la entidad correspondiente.
     * <p>
     * @return Una {@link List} de entidades {@link AgenteVentas} que no están vinculadas a sesiones
     * de chat activas.
     */
    @Query("""
        SELECT a FROM AgenteVentas a
        WHERE NOT EXISTS (
            SELECT 1 FROM ChatSession s
            WHERE s.agenteVentas = a
              AND s.estado = 'ACTIVO'
        )
    """)
    List<AgenteVentas> findAgentesDisponibles();


    List<AgenteVentas> findBySedeId(Long sedeId);
}
