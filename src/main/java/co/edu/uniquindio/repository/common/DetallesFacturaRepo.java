package co.edu.uniquindio.repository.common;

import co.edu.uniquindio.model.objects.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio para manejar operaciones de persistencia relacionadas con la entidad {@link DetalleFactura}.
 * <p>
 * Este repositorio extiende {@link JpaRepository} para proporcionar métodos CRUD básicos y
 * {@link JpaSpecificationExecutor} para permitir consultas personalizadas y más complejas.
 * <p>
 * Se utiliza como parte de la capa de acceso a datos para interactuar con la base de datos,
 * permitiendo realizar consultas, actualizaciones, eliminaciones y otras operaciones
 * relacionadas con los detalles de las facturas.
 */
public interface DetallesFacturaRepo extends JpaRepository<DetalleFactura, Long>, JpaSpecificationExecutor<DetalleFactura> {
}
