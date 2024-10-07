package org.mindera.fur.code.dto.shelter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ShelterUpdateDTO implements Serializable {

    @Schema(description = "The id of the shelter", example = "1")
    private Long id;

    @Size(max = 100, message = "Name must be between 1 and 100 characters")
    @Schema(description = "The name of the shelter", example = "Shelter 1")
    private String name;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @Size(max = 50, message = "Vat must be between 1 and 50 characters")
    @Schema(description = "The vat of the shelter", example = "12345")
    private String vat;

    @Size(max = 50, message = "Email must be between 1 and 50 characters")
    @Email(message = "Email must be valid")
    @Schema(description = "The email of the shelter", example = "shelter@example.com")
    private String email;

    @Size(max = 200, message = "Address1 must be between 1 and 200 characters")
    @Schema(description = "The address1 of the shelter", example = "123 Main Street")
    private String address1;

    @Size(max = 200, message = "Address2 must be between 1 and 200 characters")
    @Schema(description = "The address2 of the shelter", example = "Apartment 1")
    private String address2;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @Size(max = 20, message = "Postal code must be between 1 and 20 characters")
    @Schema(description = "The postal code of the shelter", example = "12345")
    private String postalCode;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @Size(max = 20, message = "Phone must be between 1 and 20 characters")
    @Schema(description = "The phone of the shelter", example = "1234567890")
    private String phone;

    @Pattern(regexp = "\\d+", message = "This field must contain only numbers")
    @Size(max = 20, message = "Size must be between 1 and 20 characters")
    @Schema(description = "The size of the shelter", example = "10")
    private String size;

    @Schema(description = "The is active of the shelter", example = "true")
    private Boolean isActive;
}
