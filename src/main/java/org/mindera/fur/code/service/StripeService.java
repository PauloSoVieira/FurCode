package org.mindera.fur.code.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {


    /**
     * Create a payment intent for a given amount and currency
     * @param amount
     * @param currency
     * @return
     * @throws StripeException
     */
    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();
        return PaymentIntent.create(params);
    }
}