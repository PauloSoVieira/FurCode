package org.mindera.fur.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Class representing a RequestDetail.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "request_details")
public class RequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "date")
    private LocalDate date;

    @NotBlank(message = "Observation must be provided")
    @Size(max = 1000, message = "Observation must be less than 1000 characters")
    @Column(name = "observation")
    private String observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_request_id")
    private AdoptionRequest adoptionRequest;
}
