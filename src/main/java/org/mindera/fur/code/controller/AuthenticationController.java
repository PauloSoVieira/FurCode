package org.mindera.fur.code.controller;

import jakarta.validation.Valid;
import org.mindera.fur.code.dto.person.PersonAuthenticationDTO;
import org.mindera.fur.code.dto.person.PersonCreationDTO;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid PersonAuthenticationDTO personAuthenticationDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(personAuthenticationDTO.getEmail(), personAuthenticationDTO.getPassword());
        Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid PersonCreationDTO personCreationDTO) {
        if (personRepository.findByEmail(personCreationDTO.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(personCreationDTO.getPassword());
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
