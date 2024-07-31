package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;


}
