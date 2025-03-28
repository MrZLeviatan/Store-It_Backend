package co.edu.uniquindio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication  // Indica que es la clase principal de Spring Boot
@EntityScan(basePackages = "co.edu.uniquindio.model") // Indica donde se generan las entidades de la BD.
public class StoreItApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreItApplication.class, args);  // Inicia la aplicación
    }
}
