package co.edu.uniquindio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * Clase utilitaria para generar y validar tokens JWT.
 * Esta clase es responsable de crear tokens JWT con una duración determinada
 * y de validar la autenticidad de los mismos.
 * <p>
 * El token JWT es utilizado para la autenticación y autorización del usuario en el sistema.
 * Los métodos de esta clase permiten generar tokens con una fecha de expiración establecida,
 * y también validar los tokens al ser enviados por el cliente para asegurar que son válidos.
 * </p>
 */
@Component
public class JWTUtils {


    // Clave secreta usada para firmar los tokens (mínimo 256 bits para HS256)
    private static final String SECRET = "store-it-secret-key-para-firmar-tokens-jwt-de-forma-segura";

    /**
     * Genera un token JWT con los datos proporcionados.
     * Este token es utilizado para autenticar y autorizar al usuario en el sistema.
     * <p>
     * El token contiene información adicional (claims), un subject (normalmente el ID del usuario),
     * una fecha de emisión y una fecha de expiración.
     * </p>
     *
     * @param id El identificador del usuario que será el subject del token.
     * @param claims Un mapa de claims que contiene información adicional que se incluirá en el token.
     * @return Un token JWT como cadena de texto.
     */
    public String generateToken(String id, Map<String, String> claims) {

        // Se obtiene el instante actual para usar como fecha de emisión y para la expiración
        Instant now = Instant.now(); // Se obtiene el instante actual

        // Se construye el token JWT con los datos proporcionados y firma con una clave secreta
        return Jwts.builder()
                .subject(id) // ID del usuario
                .issuedAt(Date.from(now)) // Fecha de emisión
                .expiration(Date.from(now.plus(1L, ChronoUnit.HOURS))) // Expira en 1 hora
                .claims(claims) // ✅ Añade los claims personalizados como el tipo de usuario
                .signWith(getKey(), Jwts.SIG.HS256) // Firma el token con HS256
                .compact();
    }


    /**
     * Válida y analiza un token JWT recibido.
     * @param jwtString Token JWT como cadena
     * @return Objeto Jws con los claims extraídos
     * @throws JwtException si el token es inválido, expirado o mal formado
     */
    public Jws<Claims> parseJwt(String jwtString) throws JwtException {
        JwtParser parser = Jwts.parser()
                .verifyWith(getKey()) // Verifica la firma
                .build();

        return parser.parseSignedClaims(jwtString); // ✅ Devuelve los claims seguros (firmados)
    }


    /**
     * Obtiene la clave secreta HMAC usada para firmar/verificar los tokens.
     * @return Clave secreta como SecretKey
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

}
