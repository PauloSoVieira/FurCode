package org.mindera.fur.code.dto.shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShelterCreationDTO {
    @NotNull
    @NotBlank
    private String name;
    private Integer vat;
    private String email;
    private String address1;
    private String address2;
    private String postCode;
    private Integer phone;
    private Integer size;
    private Boolean isActive;

    public ShelterCreationDTO(String name) {
        this.name = name;
    }
}
