package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A login response")
public class LoginResponseDTO {
    private PersonDTO person;
    private String token;

    private LoginResponseDTO(PersonDTO person, String token) {
        this.person = person;
        this.token = token;
    }

    public static LoginResponseDTO create(PersonDTO person, String token) {
        return new LoginResponseDTO(person, token);
    }

    // Getters
    public PersonDTO getPerson() {
        return person;
    }

    public String getToken() {
        return token;
    }
}