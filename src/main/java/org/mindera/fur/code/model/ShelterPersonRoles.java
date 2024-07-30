package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shelter_person_roles")
@Data
public class ShelterPersonRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;
}
