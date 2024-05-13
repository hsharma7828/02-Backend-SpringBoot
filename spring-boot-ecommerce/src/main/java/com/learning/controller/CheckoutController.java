package com.learning.controller;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dto.PaymentInfo;
import com.learning.dto.Purchase;
import com.learning.dto.PurchaseResponse;
import com.learning.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	private CheckoutService checkoutService;

	public CheckoutController(CheckoutService checkoutService) {
		super();
		this.checkoutService = checkoutService;
	}
	
	@PostMapping("/purchase")
	public PurchaseResponse placeOrder(@RequestBody Purchase purchse) {
		
		PurchaseResponse purchaseResponse = checkoutService.placeOrder(purchse);
		
		return purchaseResponse;
	}
	
	@PostMapping("/payment-intent")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
		
		logger.info( "paymenyInfo.amount: "+paymentInfo.getAmount());
		
		PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);
		
		String paymentStr = paymentIntent.toJson();
		
		return new ResponseEntity<>(paymentStr, HttpStatus.OK);
	}
}




