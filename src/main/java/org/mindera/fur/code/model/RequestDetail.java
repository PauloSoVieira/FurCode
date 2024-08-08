package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Table(name = "request_details")
public class RequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Person person;

    @Enumerated(EnumType.STRING)
    private State state;

    @DateTimeFormat
    private Date date;

    private String observation;

    @ManyToOne
    @JoinColumn(name = "request_detail_id")
    private AdoptionRequest adoptionRequest;
}
