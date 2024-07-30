package org.mindera.fur.code.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medical_record")
@Data
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;


}
