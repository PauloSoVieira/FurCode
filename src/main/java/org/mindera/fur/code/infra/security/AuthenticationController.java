package org.mindera.fur.code.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.mindera.fur.code.dto.person.LoginResponseDTO;
import org.mindera.fur.code.dto.person.PersonAuthenticationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.messages.token.TokenMessage;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Schema(description = "Authentication controller")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {


    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PersonRepository personRepository;
    private PersonMapper personMapper;

    @Autowired
    public AuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager, PersonRepository personRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
    }

    @Schema(description = "Login a person")
    /**
     * Login a person
     *
     * @param personAuthenticationDTO The person authentication information
     * @return The login response
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid PersonAuthenticationDTO personAuthenticationDTO) {
        try {

            if (personAuthenticationDTO.getEmail() == null || personAuthenticationDTO.getPassword() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TokenMessage.INVALID_EMAIL_OR_PASSWORD);
            }

            Person person = personRepository.findByEmail(personAuthenticationDTO.getEmail());

            if (person == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TokenMessage.INVALID_EMAIL_OR_PASSWORD);
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(personAuthenticationDTO.getPassword(), person.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TokenMessage.INVALID_EMAIL_OR_PASSWORD);
            }

            PersonDTO personDTO = personMapper.INSTANCE.toDTO(person);
            String token = tokenService.generateToken(personDTO);
            return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new LoginResponseDTO(TokenMessage.INVALID_EMAIL_OR_PASSWORD), HttpStatus.BAD_REQUEST);
        }
    }

}
