package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "shelter")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "shelter", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;


    @OneToOne(mappedBy = "shelter")
    private Pet pet;
}
