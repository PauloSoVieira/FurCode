package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "person")
@Tag(name = "Person", description = "Details about the person entity")

public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the person", example = "1", required = true)
    private Long id;

    @Schema(description = "The first name of the person", example = "John", required = true)
    private String firstName;

    @Schema(description = "The last name of the person", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "The nif of the person", example = "1234567890", required = true)
    private Long nif; //TODO nif cant be changed

    @Schema(description = "The email of the person", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "The password of the person", example = "password", required = true)
    private String password;

    @Schema(description = "The address1 of the person", example = "123 Main Street", required = true)
    private String address1;

    @Schema(description = "The address2 of the person", example = "Apartment 1", required = true)
    private String address2;

    @Schema(description = "The postal code of the person", example = "12345", required = true)
    private String postalCode;

    @Schema(description = "The cell phone of the person", example = "1234567890", required = true)
    private Long cellPhone;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The role of the person", example = "ROLE_USER", required = true)
    private Role role;


    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "The shelter person roles of the person", required = true)
    private Set<ShelterPersonRoles> shelterPersonRoles;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "The donations of the person", required = true)
    private Set<Donation> donations;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "The shelters of the person", required = true)
    private Set<Shelter> shelters;

    /**
     * Constructor with id.
     *
     * @param id The id of the person.
     */
    public Person(Long id) {
        this.id = id;
    }

    /**
     * Default no-argument constructor.
     */
    public Person() {
    }


    /**
     * Constructor with firstName, email and number.
     *
     * @param johnDoe The firstName of the person.
     * @param mail    The email of the person.
     * @param number  The nif of the person.
     */
    public Person(String johnDoe, String mail, Long number) {
        this.firstName = johnDoe;
        this.email = mail;
        this.nif = number;
    }

    /**
     * Get the authorities of the person.
     *
     * @return The authorities of the person.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.MASTER) {
            return List.of(new SimpleGrantedAuthority("MASTER"),
                    new SimpleGrantedAuthority("MANAGER"),
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("USER"));
        }
        if (this.role == Role.MANAGER) {
            return List.of(new SimpleGrantedAuthority("MANAGER"),
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("USER"));
        }
        if (this.role == Role.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("USER"));
        }
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Get the username of the person.
     *
     * @return The username of the person.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Check if the account is not expired.
     *
     * @return True if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Check if the account is not locked.
     *
     * @return True if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Check if the credentials are not expired.
     *
     * @return True if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Check if the account is enabled.
     *
     * @return True if the account is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
