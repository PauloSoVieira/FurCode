package org.mindera.fur.code.dto.shelter;

import lombok.Data;

@Data
public class ShelterDTO {
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


    public ShelterDTO(String name, Long vat, String email, String address1, String address2, String postalCode, Long phone, Long size, Boolean isActive) {
        this.name = name;
        this.vat = vat;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.size = size;
        this.isActive = isActive;

    }
}
