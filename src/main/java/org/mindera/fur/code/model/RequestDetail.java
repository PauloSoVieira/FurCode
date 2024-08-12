package org.mindera.fur.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Class representing a RequestDetail.
 */
@Entity
@Data
@Table(name = "request_details")
public class RequestDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    @Enumerated(EnumType.STRING)
    private State state;

    @DateTimeFormat
    private Date date;

    @Max(value = 1000, message = "Observation must be less than 1000 characters")
    private String observation;
    /**
     * The id of the AdoptionRequest.
     */
    @ManyToOne
    @JoinColumn(name = "adoption_request_id")
    private AdoptionRequest adoptionRequest;
}
