package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * Interfaz que sirve como repositorio para gestionar las entidades de {@link Producto} en la base de datos.
 * <p>
 * Extiende {@link JpaRepository} para proporcionar métodos estándar de acceso a datos, como guardar, eliminar y consultar entidades de {@link Producto}.
 * <p>
 * Extiende {@link JpaSpecificationExecutor} para permitir la ejecución de consultas complejas basadas en especificaciones.
 * <p>
 * Permite la interacción con la capa de persistencia subyacente para las operaciones de entidades de {@link Producto},
 * incluyendo operaciones CRUD y consultas personalizadas basadas en especificaciones.
 */
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {


}
