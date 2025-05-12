package co.edu.uniquindio.model.users.enums;

/**
 * Enumeración que representa los tipos de {@code Cliente} que pueden registrarse en el sistema.
 * <p>
 * Este enum permite clasificar a los clientes según su naturaleza jurídica para aplicar
 * diferentes políticas, validaciones o visualizaciones en el sistema.
 * <p>
 * Tipos de cliente:
 * <ul>
 * <li> {@code PERSONA_NATURAL}: Cliente individual o persona física. </li>
 * <li> {@code EMPRESA}: Cliente que representa una entidad jurídica o compañía. </li>
 */
public enum TipoCliente {

    /**
     * Cliente individual (persona física).
     */
    PERSONA_NATURAL,

    /**
     * Cliente corporativo o entidad jurídica.
     */
    PERSONA_JURIDICA
}
