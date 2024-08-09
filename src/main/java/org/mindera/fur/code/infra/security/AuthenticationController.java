package org.mindera.fur.code.infra.security;

import jakarta.validation.Valid;
import org.mindera.fur.code.dto.person.LoginResponseDTO;
import org.mindera.fur.code.dto.person.PersonAuthenticationDTO;
import org.mindera.fur.code.dto.person.PersonDTO;
import org.mindera.fur.code.mapper.PersonMapper;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid PersonAuthenticationDTO personAuthenticationDTO) {
        if (personRepository.findByEmail(personAuthenticationDTO.getEmail()) == null) {
            return ResponseEntity.badRequest().build();
        }
        Person person = personRepository.findByEmail(personAuthenticationDTO.getEmail());
        PersonDTO personDTO = personMapper.INSTANCE.toDTO(person);
        String token = tokenService.generateToken(personDTO);

        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);

//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(personAuthenticationDTO.getEmail(), personAuthenticationDTO.getPassword());
//            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


//        var usernamePassoword = new UsernamePasswordAuthenticationToken(personAuthenticationDTO.getEmail(),
//                personAuthenticationDTO.getPassword());
//
//        var auth = authenticationManager.authenticate(usernamePassoword);
//        System.out.println("login realizado");
//        return ResponseEntity.ok().build();

//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(personAuthenticationDTO.getEmail(), personAuthenticationDTO.getPassword());
//        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//
//        String token = tokenService.generateToken((PersonDTO) auth.getPrincipal());
//
//        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }

}
