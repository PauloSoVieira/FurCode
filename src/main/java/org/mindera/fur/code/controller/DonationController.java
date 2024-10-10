package org.mindera.fur.code.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import org.mindera.fur.code.dto.donation.DonationCreateDTO;
import org.mindera.fur.code.dto.donation.DonationDTO;
import org.mindera.fur.code.model.*;
import org.mindera.fur.code.repository.DonationRepository;
import org.mindera.fur.code.repository.PersonRepository;
import org.mindera.fur.code.repository.ShelterRepository;
import org.mindera.fur.code.service.DonationService;
import org.mindera.fur.code.service.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class DonationController {

    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    private final StripeService stripeService;
    private final DonationService donationService;

    @Autowired
    public DonationController(StripeService stripeService, DonationService donationService) {
        this.stripeService = stripeService;
        this.donationService = donationService;
    }

    @PostMapping("/donations")
    public ResponseEntity<?> saveDonation(@RequestBody DonationCreateDTO donationCreateDTO) {
        try {
            logger.info("Received donation request: {}", donationCreateDTO);
            DonationDTO savedDonation = donationService.createDonation(donationCreateDTO);
            return ResponseEntity.ok(savedDonation);
        } catch (Exception e) {
            logger.error("Error saving donation", e);
            return ResponseEntity.status(500).body("Error saving donation: " + e.getMessage());
        }
    }

    @PostMapping("/donations/confirm")
    public ResponseEntity<?> confirmDonation(@RequestBody ConfirmDonationRequest request) {
        try {
            logger.info("Confirming donation: {}", request);
            PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());
            String status = paymentIntent.getStatus();

            switch (status) {
                case "succeeded":
                    DonationCreateDTO donationCreateDTO = new DonationCreateDTO();
                    donationCreateDTO.setTotal(BigDecimal.valueOf(paymentIntent.getAmount()).divide(BigDecimal.valueOf(100)));
                    donationCreateDTO.setCurrency(paymentIntent.getCurrency());
                    donationCreateDTO.setPersonId(request.getPersonId());
                    donationCreateDTO.setShelterId(request.getShelterId());
                    donationCreateDTO.setPaymentIntentId(paymentIntent.getId());
                    donationCreateDTO.setPaymentMethod(paymentIntent.getPaymentMethod());

                    DonationDTO savedDonation = donationService.createDonation(donationCreateDTO);
                    logger.info("Donation confirmed and saved: {}", savedDonation);
                    return ResponseEntity.ok(new ConfirmDonationResponse("Donation successfully recorded"));

                case "requires_payment_method":
                case "requires_confirmation":
                case "requires_action":
                case "processing":
                case "canceled":
                    logger.warn("Payment not successful. Status: {}", status);
                    return ResponseEntity.badRequest().body(new ConfirmDonationResponse("Payment not successful. Status: " + status));

                default:
                    logger.warn("Unexpected payment status: {}", status);
                    return ResponseEntity.badRequest().body(new ConfirmDonationResponse("Payment not successful. Unexpected status: " + status));
            }
        } catch (Exception e) {
            logger.error("Error confirming donation", e);
            return ResponseEntity.status(500).body(new ConfirmDonationResponse("Error confirming donation: " + e.getMessage()));
        }
    }

    @Operation(summary = "Create a payment intent for donation", description = "Creates a Stripe PaymentIntent for processing a donation")
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody DonationRequest request) {
        try {
            logger.info("Creating payment intent: {}", request);
            PaymentIntent intent = stripeService.createPaymentIntent(request.getAmount(), request.getCurrency());
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            response.put("paymentIntentId", intent.getId());
            logger.info("Payment intent created: {}", intent.getId());
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            logger.error("Error creating payment intent", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}