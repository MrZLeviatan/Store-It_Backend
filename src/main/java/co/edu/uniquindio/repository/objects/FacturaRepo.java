package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Interfaz de repositorio para gestionar entidades {@link Factura} en el sistema Store-It.
 * <p>
 * Esta interfaz extiende {@link JpaRepository}, proporcionando operaciones CRUD básicas
 * para la entidad Factura, y {@link JpaSpecificationExecutor}, permitiendo la creación
 * de consultas dinámicas y el filtrado basado en criterios.
 * <p>
 * FacturaRepo es un componente esencial para la capa de acceso a datos, que permite la interacción
 * con la base de datos, a la vez que abstrae los detalles de implementación.
 */
public interface FacturaRepo extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {
}
