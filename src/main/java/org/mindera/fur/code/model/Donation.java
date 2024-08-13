package org.mindera.fur.code.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.form.Form;

import java.util.Date;

@Entity
@Table(name = "donation")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the donation", example = "1", required = true)
    private Long id;

    @Column(name = "total")
    @Schema(description = "The total amount of the donation", example = "20", required = true)
    private Double total;

    @Column(name = "date")
    @Schema(description = "The date of the donation", example = "2024-01-01", required = true)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Schema(description = "The shelter of the donation", required = true)
    private Shelter shelter;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    @Schema(description = "The person who donated", required = true)
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;
}
