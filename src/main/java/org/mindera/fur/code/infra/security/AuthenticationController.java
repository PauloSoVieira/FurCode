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
    private final PersonMapper personMapper;

    @Autowired
    public AuthenticationController(TokenService tokenService,
                                    AuthenticationManager authenticationManager,
                                    PersonRepository personRepository,
                                    PersonMapper personMapper) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Schema(description = "Login a person")
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

            PersonDTO personDTO = personMapper.toDTO(person);

            String token = tokenService.generateToken(personDTO);

            LoginResponseDTO response = LoginResponseDTO.create(personDTO, token);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(LoginResponseDTO.create(null, TokenMessage.INVALID_EMAIL_OR_PASSWORD), HttpStatus.BAD_REQUEST);
        }
    }}