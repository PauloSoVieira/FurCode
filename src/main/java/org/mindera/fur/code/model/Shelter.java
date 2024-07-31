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

    private Integer vat;

    private String email;

    private String address1;

    private String address2;

    private String postCode;

    private Integer phone;

    private Integer size;

    private Boolean isActive;

    @OneToMany(mappedBy = "shelter", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;


    @OneToMany(mappedBy = "shelter")
    private Set<Pet> pets;

    @OneToMany(mappedBy = "shelter")
    private Set<AdoptionRequest> adoptionRequests;
}
