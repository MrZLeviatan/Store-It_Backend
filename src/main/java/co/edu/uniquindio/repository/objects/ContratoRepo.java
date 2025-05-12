package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Contrato;
import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.enums.EstadoContrato;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Contrato}.
 * <p>
 * Proporciona operaciones CRUD y soporte para consultas dinámicas usando {@link JpaSpecificationExecutor}.
 * Este repositorio permite interactuar con la tabla de contratos en la base de datos.
 * <p>
 * Este repositorio es gestionado por Spring a través de la anotación {@link Repository}.
 */
@Repository
public interface ContratoRepo extends JpaRepository<Contrato, Long> , JpaSpecificationExecutor<Contrato> {


    /**
     * Busca contratos por el estado del contrato.
     * @param estadoContrato el estado del contrato a buscar
     * @return una lista de contratos que coinciden con el estado especificado
     */
    List<Contrato> findByEstadoContrato(EstadoContrato estadoContrato);

    List<Contrato> findByCliente(Cliente cliente);

    List<Contrato> findByAgenteVentas(AgenteVentas agenteVentas);

    Optional<Contrato> findByEspacioAndCliente(Espacio espacio, Cliente cliente);


}


