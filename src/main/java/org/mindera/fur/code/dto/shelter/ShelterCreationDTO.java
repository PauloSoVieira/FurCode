package org.mindera.fur.code.dto.shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShelterCreationDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private Long vat;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String address1;

    private String address2;

    @NotNull
    @NotBlank
    private String postCode;

    @NotNull
    @NotBlank
    private Long phone;

    @NotNull
    @NotBlank
    private Long size;

    @NotNull
    @NotBlank
    private Boolean isActive;

    public ShelterCreationDTO(String name, Long vat, String email, String address1, String address2, String postCode, Long phone, Long size, Boolean isActive) {
        this.name = name;
        this.vat = vat;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.postCode = postCode;
        this.phone = phone;
        this.size = size;
        this.isActive = isActive;
    }

    public ShelterCreationDTO() {
    }
}