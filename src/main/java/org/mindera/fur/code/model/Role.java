package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "role")
@Data
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;
}
