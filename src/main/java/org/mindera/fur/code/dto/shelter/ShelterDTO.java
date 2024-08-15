package org.mindera.fur.code.dto.shelter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * DTO for a shelter.
 */
@Data
@Schema(description = "A shelter")
public class ShelterDTO implements Serializable {
    private Long id;
    private String name;
    private Long vat;
    private String email;
    private String address1;
    private String address2;
    private String postalCode;
    private Long phone;
    private Long size;
    private Boolean isActive;
    private LocalDate creationDate;


    /**
     * Constructor with parameters.
     *
     * @param id           The id of the shelter.
     * @param name         The name of the shelter.
     * @param vat          The vat number of the shelter.
     * @param email        The email of the shelter.
     * @param address1     The address1 of the shelter.
     * @param address2     The address2 of the shelter.
     * @param postalCode   The postal code of the shelter.
     * @param phone        The phone number of the shelter.
     * @param size         The size of the shelter.
     * @param isActive     The isActive status of the shelter.
     * @param creationDate The creation date of the shelter.
     */
    public ShelterDTO(Long id, String name, Long vat, String email, String address1, String address2, String postalCode, Long phone, Long size, Boolean isActive, LocalDate creationDate) {
        this.id = id;

        this.name = name;
        this.vat = vat;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.size = size;
        this.isActive = isActive;
        this.creationDate = creationDate;
    }
}
