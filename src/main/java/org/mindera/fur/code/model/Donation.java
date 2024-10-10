package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.mindera.fur.code.model.form.Form;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the donation", example = "1", required = true)
    private Long id;

    @Column(name = "total", nullable = false)
    @Schema(description = "The total amount of the donation", example = "20.00", required = true)
    private BigDecimal total;

    @Column(name = "currency", length = 3, nullable = false)
    @Schema(description = "The currency of the donation", example = "EUR", required = true)
    private String currency;

    @Column(name = "date", nullable = false)
    @Schema(description = "The date of the donation", example = "2024-01-01T12:00:00", required = true)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    @Schema(description = "The shelter receiving the donation", required = true)
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @Schema(description = "The person who donated", required = true)
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "payment_intent_id")
    @Schema(description = "The Stripe payment intent ID", example = "pi_1234567890")
    private String paymentIntentId;

    @Column(name = "status", nullable = false)
    @Schema(description = "The status of the donation", example = "completed")
    private String status;

    @Column(name = "payment_method")
    @Schema(description = "The payment method used", example = "credit_card")
    private String paymentMethod;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}