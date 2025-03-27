package co.edu.uniquindio.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "cookieAuth",
        type = SecuritySchemeType.APIKEY, // Usamos API Key porque las cookies funcionan de forma similar
        in = SecuritySchemeIn.COOKIE, // Especificamos que la autenticación está en una cookie
        paramName = "SESSIONID" // Nombre de la cookie que almacena el token de sesión
)
public class SwaggerConfig {
}
