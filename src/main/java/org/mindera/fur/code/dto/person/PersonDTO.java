package org.mindera.fur.code.dto.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Schema(description = "A person")
public class PersonDTO implements Serializable {
    @Schema(description = "The unique identifier of the person", example = "1", required = true)

    private Long id;
    @Schema(description = "The first name of the person", example = "John", required = true)
    private String firstName;
    @Schema(description = "The last name of the person", example = "Doe", required = true)
    private String lastName;
    @Schema(description = "The email address of the person", example = "john.doe@example.com", required = true)
    private String email;
    @Schema(description = "The phone number of the person", example = "1234567890", required = true)
    private String address1;
    @Schema(description = "The second line of the address of the person", example = "123 Main St", required = true)
    private String address2;
    @Schema(description = "The city of the person", example = "New York", required = true)
    private String postalCode;
    @Schema(description = "The state of the person", example = "NY", required = true)
    private Long cellPhone;
    @Schema(description = "The id of the shelters where the person is", example = "1")
    private Set<Long> shelterIds;

    /**
     * Constructor for PersonDTO
     *
     * @param firstName The first name of the person
     * @param lastName  The last name of the person
     */
    public PersonDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
