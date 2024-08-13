package org.mindera.fur.code.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mindera.fur.code.dto.person.PersonDTO;

import org.mindera.fur.code.exceptions.token.TokenException;

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

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("fur-code")
                    .build();

            verifier.verify(token);

            return JWT.require(algorithm)
                    .withIssuer("fur-code")
                    .build()
                    .verify(token)
                    .getSubject();


        } catch (SignatureVerificationException e) {
            throw new TokenException(TokenMessage.INVALID_TOKEN_SIGNATURE);
        } catch (TokenExpiredException e) {
            throw new TokenException(TokenMessage.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new TokenException(TokenMessage.TOKEN_VALIDATION_FAILED);

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
