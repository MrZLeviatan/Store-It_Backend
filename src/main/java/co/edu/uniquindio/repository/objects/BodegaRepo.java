package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Bodega}.
 * <p>
 * Extiende de {@link JpaRepository} para proporcionar operaciones CRUD básicas,
 * y de {@link JpaSpecificationExecutor} para permitir consultas dinámicas
 * y complejas mediante especificaciones JPA (criterios).
 * <p>
 * Este repositorio está enfocado en el manejo de persistencia de objetos
 * del tipo {@code Bodega}, como la creación, actualización, eliminación y búsqueda
 * en base de datos.
 * <p>
 * Anotado con {@code @Repository} para ser detectado como componente Spring
 * y manejar automáticamente las excepciones de persistencia.
 *
 * @author MrZ.Leviatan
 */
@Repository
public interface BodegaRepo extends JpaRepository<Bodega, Long>, JpaSpecificationExecutor<Bodega> {

    // Métodos personalizados pueden ser agregados aquí si es necesario
}
