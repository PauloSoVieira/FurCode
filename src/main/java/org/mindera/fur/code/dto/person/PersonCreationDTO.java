package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A person creation request")
public class PersonCreationDTO {
    @NotNull
    @NotBlank
    @Schema(description = "The first name of the person", example = "John", required = true)
    private String firstName;

    @NotNull
    @NotBlank
    @Schema(description = "The last name of the person", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "The NIF of the person", example = "123456789012", required = true)
    private Long nif;

    @NotNull
    @NotBlank
    @Schema(description = "The email address of the person", example = "john.doe@example.com", required = true)
    private String email;

    @NotNull
    @NotBlank
    @Schema(description = "The password of the person", example = "password", required = true)
    private String password;

    @NotNull
    @NotBlank
    @Schema(description = "The first line of the address of the person", example = "123 Main St", required = true)
    private String address1;

    @Schema(description = "The second line of the address of the person", example = "123 Main St", required = true)
    private String address2;

    @NotNull
    @NotBlank
    @Schema(description = "The postal code of the person", example = "12345", required = true)
    private Long postalCode;

    @Schema(description = "The cell phone number of the person", example = "1234567890", required = true)
    private Long cellPhone;

    /**
     * Default constructor for PersonCreationDTO
     */
    public PersonCreationDTO() {
    }

    /**
     * Constructor for PersonCreationDTO
     *
     * @param firstName  The first name of the person
     * @param lastName   The last name of the person
     * @param nif        The NIF of the person
     * @param email      The email address of the person
     * @param password   The password of the person
     * @param address1   The first line of the address of the person
     * @param address2   The second line of the address of the person
     * @param postalCode The postal code of the person
     * @param cellPhone  The cell phone number of the person
     */
    public PersonCreationDTO(String firstName, String lastName, Long nif, String email, String password, String address1, String address2, Long postalCode, Long cellPhone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nif = nif;
        this.email = email;
        this.password = password;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.cellPhone = cellPhone;
    }
}
