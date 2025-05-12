package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Espacio;
import co.edu.uniquindio.model.objects.enums.EstadoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceder a datos de la entidad {@link Espacio}.
 * <p>
 * Repository for accessing {@link Espacio} entity data.
 * <p>
 * Extiende de {@link JpaRepository} para operaciones CRUD básicas y de {@link JpaSpecificationExecutor}
 * para soporte de consultas dinámicas utilizando Specifications.
 */
@Repository
public interface EspacioRepo extends JpaRepository<Espacio, Long> , JpaSpecificationExecutor<Espacio> {

}
