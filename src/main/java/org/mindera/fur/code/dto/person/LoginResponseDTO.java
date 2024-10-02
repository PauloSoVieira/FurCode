package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A response object for login, containing person details and an authentication token.")
public class LoginResponseDTO {

    @Schema(description = "Details of the person logging in.", required = true)
    private PersonDTO person;

    @Schema(description = "JWT token generated upon successful login.", example = "eyJhbGciOiJIUzI1NiIsInR5...")
    private String token;

    /**
     * Creates a new login response.
     *
     * @param person The person details.
     * @param token The authentication token.
     */
    private LoginResponseDTO(PersonDTO person, String token) {
        this.person = person;
        this.token = token;
    }

    /**
     * Static factory method to create a new instance of LoginResponseDTO.
     *
     * @param person The person details.
     * @param token The authentication token.
     * @return A new instance of LoginResponseDTO.
     */
    public static LoginResponseDTO create(PersonDTO person, String token) {
        return new LoginResponseDTO(person, token);
    }

    /**
     * Returns the person details of the login response.
     *
     * @return The person details.
     */
    public PersonDTO getPerson() {
        return person;
    }

    /**
     * Returns the authentication token of the login response.
     *
     * @return The authentication token.
     */
    public String getToken() {
        return token;
    }
}
