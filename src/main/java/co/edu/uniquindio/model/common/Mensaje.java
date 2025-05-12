package co.edu.uniquindio.model.common;

import co.edu.uniquindio.model.common.enums.RemitenteChat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa un mensaje de chat en el sistema.
 * <p>
 * Un mensaje está asociado a una sesión de chat específica y contiene información
 * detallada acerca de su remitente, contenido y la fecha en la que fue enviado.
 * <p>
 * Cada mensaje es persistido en la base de datos en la tabla {@code mensajes_chat}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mensajes_chat")
public class Mensaje {

    /**
     * Identificador único del mensaje.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Sesión de chat a la que pertenece este mensaje.
     */
    @ManyToOne
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;


    @Column(name = "remitente", nullable = false)
    @Comment("Remitente del mensaje: 'CLIENTE' o 'AGENTE'")
    private RemitenteChat remitente;

    @Column(name = "contenido", nullable = false, length = 1000)
    @Comment("Contenido del mensaje de chat.")
    private String contenido;

    @CreationTimestamp
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}

