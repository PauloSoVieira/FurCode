package org.mindera.fur.code.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.messages.token.TokenMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Schema(description = "Token service")
@Service
public class TokenService {

    /**
     * The secret password
     */
    private final String SECRET = "my-secret-key";

    /**
     * Generate token
     *
     * @param personDTO The person dto
     * @return The token
     */
    public String generateToken(PersonDTO personDTO) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create()
                    .withIssuer("fur-code")
                    .withSubject(personDTO.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException(TokenMessage.TOKEN_CREATION_ERROR, exception);
        }
    }

    /**
     * Validate token
     *
     * @param token The token
     * @return The email
     */
    public String validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm)
                    .withIssuer("fur-code")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (PersonException exception) {
            throw new RuntimeException("You don't have permission ", exception);
        }
    }

    /**
     * Generate expiration date
     *
     * @return The expiration date
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("+0"));
    }

}
