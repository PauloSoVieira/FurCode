package org.mindera.fur.code.dto.ShelterDTO;

import lombok.Data;

@Data
public class ShelterDTO {
    private Long id;
    private String name;
    private Integer VAT;
    private String email;
    private String address1;
    private String address2;
    private String postCode;
    private Integer phone;
    private Integer size;
    private Boolean isActive;
}
