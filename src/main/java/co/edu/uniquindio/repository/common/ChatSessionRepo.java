package co.edu.uniquindio.repository.common;

import co.edu.uniquindio.dto.common.chat.ChatSessionDto;
import co.edu.uniquindio.model.common.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepo extends JpaRepository<ChatSession, Long> {

    List<ChatSessionDto> findByCliente_Id(Long clienteId);


    List<ChatSessionDto> findByAgenteVentas_Id(Long agenteVentasId);
}

