package org.mindera.fur.code.dto.shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShelterDTO {
    private Long id;

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

    public ShelterDTO(String name) {
        this.name = name;
    }

    public ShelterDTO() {
    }
}
