package org.mindera.fur.code.dto.donation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DonationCreateDTO {
    private BigDecimal total;
    private String currency;
    private Long personId;
    private Long shelterId;
    private String paymentIntentId;
    private String paymentMethod;
}
