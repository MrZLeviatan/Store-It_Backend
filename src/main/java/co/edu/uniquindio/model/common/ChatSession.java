package co.edu.uniquindio.model.common;

import co.edu.uniquindio.model.common.enums.EstadoChat;
import co.edu.uniquindio.model.users.AgenteVentas;
import co.edu.uniquindio.model.users.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private AgenteVentas agenteVentas;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Comment("Estado de la sesión de chat: ACTIVA, FINALIZADA, ABANDONADA, etc.")
    private EstadoChat estado;

    @CreationTimestamp
    @Column(name = "inicio_chat")
    private LocalDateTime inicioChat;

    @UpdateTimestamp
    @Column(name = "fin_chat")
    private LocalDateTime finChat;

    /**
     * Lista de mensajes enviados en esta sesión de chat.
     * Cada sesión puede tener múltiples mensajes.
     */
    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Lista de mensajes asociados a esta sesión de chat.")
    private List<Mensaje> mensajes;
}