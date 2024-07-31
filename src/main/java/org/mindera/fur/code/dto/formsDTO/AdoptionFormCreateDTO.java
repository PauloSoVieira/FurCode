package org.mindera.fur.code.dto.formsDTO;

import lombok.Data;

import java.util.Date;

@Data
public class AdoptionFormCreateDTO {

    private String name;
    private Date createdAt;
    private Date updatedAt;
    private Long shelterId;
    private Long personId;
    private Long petId;
}
