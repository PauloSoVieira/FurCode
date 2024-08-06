package org.mindera.fur.code.dto.shelterPersonRoles;

import lombok.Data;

@Data
public class ShelterPersonRolesDTO {
    private Long id;
    private Long personId;
    private Long shelterId;
    private String role;
}
