package org.mindera.fur.code.dto.person;

import lombok.Data;
import org.mindera.fur.code.model.Role;

@Data
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address1;
    private String address2;
    private Integer postalCode;
    private Integer cellPhone;
    private Role role;


    public PersonDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonDTO() {
    }
}
