package org.mindera.fur.code.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.mindera.fur.code.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeDonationController {

    private final StripeService stripeService;

    @Autowired
    public StripeDonationController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            Long amount = Long.parseLong(request.get("amount").toString());
            String currency = (String) request.get("currency");
            PaymentIntent intent = stripeService.createPaymentIntent(amount, currency);
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            response.put("paymentIntentId", intent.getId());
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<Map<String, String>> confirmPayment(@RequestBody Map<String, String> request) {
        try {
            String paymentIntentId = request.get("paymentIntentId");
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            Map<String, String> response = new HashMap<>();
            response.put("status", paymentIntent.getStatus());
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}