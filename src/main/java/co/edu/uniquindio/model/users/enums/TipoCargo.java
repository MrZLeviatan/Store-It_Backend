package co.edu.uniquindio.model.users.enums;

/**
 * Enum que representa los distintos tipos de cargo que puede tener un miembro del personal en Store-It.
 *
 * <p>Se utiliza principalmente para categorizar las funciones laborales dentro de la bodega u organización.</p>
 *
 * <ul>
 *   <li>{@code ENCARGADO}: Persona responsable de la supervisión general de las actividades.</li>
 *   <li>{@code OPERARIO}: Personal que realiza tareas operativas como manejo de productos.</li>
 *   <li>{@code PASANTE}: Personal en formación o prácticas, con tareas asignadas bajo supervisión.</li>
 * </ul>
 *
 * <p>Este enum puede ser utilizado en entidades como {@code PersonalBodega} o similares para definir el rol laboral.</p>
 *
 */
public enum TipoCargo {
    ENCARGADO,
    OPERARIO,
    PASANTE
}
