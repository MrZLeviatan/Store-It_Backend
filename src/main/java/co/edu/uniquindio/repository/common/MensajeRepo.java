package co.edu.uniquindio.repository.common;

import co.edu.uniquindio.dto.MensajeDTO;
import co.edu.uniquindio.model.common.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepo extends JpaRepository<Mensaje, Long> {

    // Obtener mensajes por sesión
    List<Mensaje> findByChatSession_IdOrderByFechaEnvioAsc(Long chatSessionId);


}

