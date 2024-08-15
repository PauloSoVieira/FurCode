package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Class representing a RequestDetail.
 */
@Entity
@Data
@Table(name = "request_details")
@Tag(name = "RequestDetail", description = "Details about the request detail entity")
public class RequestDetail {


    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the request detail", example = "1", required = true)
    private Long id;

    @Column(name = "person_id")
    @Schema(description = "The id of the person", example = "1", required = true)
    @Positive
    private Long personId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The state of the request detail", example = "ACCEPTED", required = true)
    private State state;

    @DateTimeFormat
    @Schema(description = "The date of the request detail", example = "2023-01-01 00:00:00", required = true)
    private Date date;

    @Size(max = 1000, message = "Observation must be less than 1000 characters")
    @Schema(description = "The observation of the request detail", example = "The request is accepted", required = true)
    private String observation;
    /**
     * The id of the AdoptionRequest.
     */
    @ManyToOne
    @JoinColumn(name = "adoption_request_id")
    @Schema(description = "The id of the adoption request", example = "1", required = true)
    private AdoptionRequest adoptionRequest;
}
