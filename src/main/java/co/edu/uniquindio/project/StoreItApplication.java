package co.edu.uniquindio.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication  // Indica que es la clase principal de Spring Boot
@ComponentScan(basePackages = "co.edu.uniquindio.controller")
public class StoreItApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreItApplication.class, args);  // Inicia la aplicación
    }
}
