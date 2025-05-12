package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.objects.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de persistencia de la entidad {@link Sede}.
 * <p>
 * Esta interfaz extiende de {@link JpaRepository} para proporcionar operaciones CRUD básicas,
 * permitiendo crear, leer, actualizar y eliminar entidades Sede.
 * <p>
 * Además, extiende de {@link JpaSpecificationExecutor} para permitir consultas dinámicas
 * y complejas utilizando especificaciones JPA.
 * <p>
 * Esta interfaz está anotada con {@code @Repository} para que sea detectada como un
 * componente manejado por el contenedor de Spring, facilitando la inyección de dependencias
 * y el manejo automático de excepciones relacionadas con la persistencia.
 */
@Repository
public interface SedeRepo extends JpaRepository<Sede, Long>, JpaSpecificationExecutor<Sede> {



}
