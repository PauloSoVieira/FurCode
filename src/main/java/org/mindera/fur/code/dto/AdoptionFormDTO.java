package org.mindera.fur.code.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AdoptionFormDTO {

    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private Long shelterId;
    private Long personId;
    private Long petId;
}
