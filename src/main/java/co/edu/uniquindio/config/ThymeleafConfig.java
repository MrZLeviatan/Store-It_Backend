package co.edu.uniquindio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


/**
 * Clase de configuración de Thymeleaf.
 * <p>
 * Esta clase define beans necesarios para que Thymeleaf funcione correctamente como motor de plantillas HTML
 * dentro de un proyecto Spring Boot.
 */
@Configuration
public class ThymeleafConfig {

    /**
     * Bean que configura el `TemplateResolver` de Thymeleaf.
     * <p>
     * Este resolver busca las plantillas HTML dentro del directorio especificado (por defecto `src/main/resources/templates`)
     * y las interpreta como HTML.
     *
     * @return instancia configurada de {@link ClassLoaderTemplateResolver}
     */
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // Ruta donde se encuentran las plantillas
        templateResolver.setSuffix(".html");                        // Sufijo esperado para las plantillas
        templateResolver.setTemplateMode("HTML");                   // Tipo de plantilla: HTML
        templateResolver.setCharacterEncoding("UTF-8");             // Codificación utilizada
        templateResolver.setCacheable(false);                       // Desactiva cache para desarrollo
        return templateResolver;
    }

    /**
     * Bean que configura el `TemplateEngine` de Thymeleaf.
     * <p>
     * Este motor es responsable de procesar las plantillas HTML con los datos dinámicos
     * que se le pasen desde un contexto.
     *
     * @return instancia configurada de {@link TemplateEngine}
     */
    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver()); // Se utiliza el resolver configurado anteriormente
        return engine;
    }
}

