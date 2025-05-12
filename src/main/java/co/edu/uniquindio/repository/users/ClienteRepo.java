package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.objects.Producto;
import co.edu.uniquindio.model.users.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import co.edu.uniquindio.model.users.base.User;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Repositorio para gestionar operaciones de acceso a datos sobre la entidad {@link Cliente}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar métodos CRUD básicos como guardar, buscar,
 * actualizar y eliminar clientes en la base de datos.</p>
 *
 * <p>También extiende de {@link JpaSpecificationExecutor}, lo cual permite ejecutar consultas
 * avanzadas y dinámicas utilizando la API de especificaciones de JPA (Specifications), ideal
 * para aplicar filtros personalizados y combinables en tiempo de ejecución.</p>
 *
 * <p>Este repositorio es una parte fundamental en la capa de persistencia del módulo de clientes.</p>
 *
 */
@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {


    /**
     * Busca un cliente a partir del correo electrónico registrado en el objeto {@link User}.
     * @param email Correo electrónico del cliente.
     * @return Un {@link Optional} que contiene el cliente si se encuentra, o vacío si no.
     */
    Optional<Cliente> findByUser_Email(String email);


    /**
     * Busca un cliente por su número de teléfono principal.
     * @param telefono Número de teléfono principal del cliente.
     * @return Un {@link Optional} que contiene el cliente si se encuentra, o vacío si no.
     */
    Optional<Cliente> findByTelefono(String telefono);


    /**
     * Busca un cliente por su número de teléfono secundario.
     * @param telefonoSecundario Número de teléfono secundario del cliente.
     * @return Un {@link Optional} que contiene el cliente si se encuentra, o vacío si no.
     */
    Optional<Cliente> findByTelefonoSecundario(String telefonoSecundario);



}
