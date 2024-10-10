package org.mindera.fur.code.dto.donation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DonationDTO {
    private Long id;
    private BigDecimal total;
    private String currency;
    private LocalDateTime date;
    private Long shelterId;
    private Long personId;
    private String paymentIntentId;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
