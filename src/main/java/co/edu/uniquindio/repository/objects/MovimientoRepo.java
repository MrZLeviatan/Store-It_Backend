package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio de acceso a datos para la entidad {@link Movimiento}.
 * <p>
 * Extiende de {@link JpaRepository} para proporcionar operaciones de persistencia
 * básicas como creación, lectura, actualización y eliminación de objetos {@code Movimiento}.
 * <p>
 * Además, hereda de {@link JpaSpecificationExecutor} para soportar la ejecución de
 * consultas dinámicas y complejas utilizando especificaciones JPA.
 * <p>
 * Este repositorio está específicamente diseñado para manejar las interacciones
 * con la base de datos relacionadas con la entidad {@link Movimiento}.
 */
public interface MovimientoRepo extends JpaRepository<Movimiento, Long>, JpaSpecificationExecutor<Movimiento> {
}
