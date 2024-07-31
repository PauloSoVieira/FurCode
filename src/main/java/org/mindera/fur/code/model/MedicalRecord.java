package org.mindera.fur.code.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet petId;

    @Column
    private boolean isVaccinated;

    @Column
    private boolean isSterilized;

    @Column
    private boolean isDewormed;

    @Column
    private Date date;

    @Column
    private String observation;

}
