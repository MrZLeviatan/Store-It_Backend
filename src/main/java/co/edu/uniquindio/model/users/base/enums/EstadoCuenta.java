package co.edu.uniquindio.model.users.base.enums;

import co.edu.uniquindio.model.users.base.User;

/**
 * Enumeración que representa los posibles estados de una cuenta de usuario dentro del sistema.
 * <p>
 * Este enum es utilizado en entidades como {@link User} para controlar el acceso y visibilidad
 * del cliente según su estado actual. Puede emplearse para aplicar filtros en autenticación,
 * visualización de clientes activos, o lógicas de negocio asociadas.
 * <p>
 * Estados disponibles:
 * <ul>
 * <li> {@code ACTIVO}: La cuenta está habilitada y puede iniciar sesión.</li>
 * <li> {@code ELIMINADO}: La cuenta ha sido eliminada del sistema.
 */
public enum EstadoCuenta {


    ACTIVO,
    ELIMINADO,

}
