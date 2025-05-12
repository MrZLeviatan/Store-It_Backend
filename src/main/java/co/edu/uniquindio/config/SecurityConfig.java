package co.edu.uniquindio.config;

import co.edu.uniquindio.security.AuthenticationEntryPoint;
import co.edu.uniquindio.security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro personalizado para procesar los tokens JWT antes de la autenticación estándar
    private final JWTFilter jwtFilter;


    /**
     * Configura la cadena de filtros de seguridad (SecurityFilterChain) para la aplicación.
     * <p>
     * <ul> Este método personaliza la seguridad de la aplicación, configurando aspectos clave como:
     * <li> Deshabilitar CSRF (Cross-Site Request Forgery) porque la API es sin estado (stateless).
     * <li> Configurar CORS (Cross-Origin Resource Sharing) usando el bean configurado previamente.
     * <li> Configurar el manejo de sesiones como stateless (sin estado), lo cual es típico en aplicaciones con JWT.
     * <li> Definir reglas de autorización para diferentes rutas (endpoints) de la API, incluyendo permisos basados en roles.
     * <li> Agregar un filtro JWT personalizado para la validación de tokens en cada solicitud.
     *
     * @param http la configuración de seguridad proporcionada por HttpSecurity.
     * @return una instancia de SecurityFilterChain configurada con las reglas de seguridad.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Este método configura la cadena de filtros de seguridad (SecurityFilterChain)

        http
                // Desactiva la protección CSRF (Cross-Site Request Forgery), ya que se utiliza JWT y no hay formularios tradicionales
                .csrf(AbstractHttpConfigurer::disable)
                // Configura CORS usando el bean de configuración CORS previamente definido
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Configura la gestión de sesiones para usar una política Stateless (sin estado)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define las reglas de autorización para las rutas de la API
                .authorizeHttpRequests(req -> req
                        // Permite acceso público a la documentación de Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/store-it/**").permitAll()
                        .requestMatchers("/api/agente-ventas/**").hasAuthority("ROLE_AGENTE_VENTAS")
                        .requestMatchers("/api/cliente/**").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers("/api/recursos-humanos/**").hasAuthority("ROLE_RECURSOS_HUMANOS")
                        .requestMatchers("/api/personal-bodega/**").hasAuthority("ROLE_PERSONAL_BODEGA")


                        // Todas las demás solicitudes requieren autenticación
                        .anyRequest().authenticated()
                )
                // Manejo de excepciones, configurando el punto de entrada de autenticación para cuando el usuario no está autenticado
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthenticationEntryPoint()))
                // Agrega el filtro JWT antes de la autenticación por nombre de usuario y contraseña
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // Retorna la configuración de la cadena de filtros de seguridad
        return http.build();
    }


    /**
     * Configura la política de CORS (Cross-Origin Resource Sharing) para la aplicación.
     *<p>
     * Esta configuración permite que clientes desde otros orígenes (por ejemplo, un frontend en Angular
     * corriendo en otro puerto o dominio) puedan realizar solicitudes al backend Spring Boot.
     *<p>
     * <ul> Características de la configuración:
     *
     * <li> Permite solicitudes desde cualquier origen con `*`.
     * <li> Acepta métodos HTTP comunes como GET, POST, PUT, DELETE y OPTIONS.
     * <li> Permite cualquier encabezado HTTP.
     * <li> Habilita el envío de credenciales (como cookies o tokens) en las solicitudes.
     * <li> Aplica esta configuración a todas las rutas del backend.
     *
     * @return una instancia de CorsConfigurationSource registrada como bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Configura CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde otros orígenes

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "*"
        ));
        // Permite solicitudes desde cualquier origen (en producción es mejor restringir esto)

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Métodos HTTP permitidos

        config.setAllowedHeaders(List.of("*"));
        // Permite cualquier encabezado

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // Aplica esta configuración a todas las rutas

        return source;
    }


    // Define el bean que Spring usará para inyectar PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Devuelve el AuthenticationManager que Spring Security usa internamente
        return configuration.getAuthenticationManager();
    }

}
