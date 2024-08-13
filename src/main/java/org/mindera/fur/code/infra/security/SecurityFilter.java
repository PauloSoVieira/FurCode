package org.mindera.fur.code.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindera.fur.code.exceptions.person.PersonException;
import org.mindera.fur.code.exceptions.token.TokenException;
import org.mindera.fur.code.messages.token.TokenMessage;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Schema(description = "Security filter")
@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    PersonRepository personRepository;

    @Override

    /**
     * Do filter internal
     *
     * @param request The request
     * @param response The response
     * @param filterChain The filter chain
     * @throws ServletException The servlet exception
     * @throws IOException The io exception
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverToken(request);

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = tokenService.validateToken(token);
            UserDetails person = personRepository.findByEmail(email);

            if (person == null || person.equals(" ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new PersonException(TokenMessage.PERSON_NOT_FOUND);
            }
        } catch (TokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(TokenMessage.TOKEN_CREATION_ERROR);
        }
    }

    /**
     * Recover token
     *
     * @param request The request
     * @return The token
     */
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.replace("Bearer ", "").trim();

//        if (!isValidTokenFormat(token)) { // Verifica o formato do token
//            throw new InvalidTokenFormatException("Token format is invalid.");
//        }

        return token;
    }
}
