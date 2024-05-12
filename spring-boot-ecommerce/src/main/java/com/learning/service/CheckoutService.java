package com.learning.service;

import com.learning.dto.PaymentInfo;
import com.learning.dto.Purchase;
import com.learning.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {
	PurchaseResponse placeOrder(Purchase purchase);
	
	PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
