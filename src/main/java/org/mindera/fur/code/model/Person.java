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

    private String firstName;
    private String lastName;
    private Integer nif; //TODO nif cant be changed
    private String email;
    private String password;
    private String address1;
    private String address2;
    private Integer postalCode;
    private Integer cellPhone;


    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private Set<ShelterPersonRoles> shelterPersonRoles;


}
