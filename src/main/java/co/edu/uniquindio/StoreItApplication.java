package co.edu.uniquindio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication  // Indica que es la clase principal de Spring Boot
@EntityScan(basePackages = "co.edu.uniquindio.model") // Indica donde se generan las entidades de la BD.

public class StoreItApplication {

    private static final Logger logger = LoggerFactory.getLogger(StoreItApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(StoreItApplication.class, args);  // Inicia la aplicación
        // Enviar un log de ejemplo
        logger.info("La aplicación Store-It ha arrancado exitosamente Natalia.");
    }
}
