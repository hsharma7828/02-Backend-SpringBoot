package com.learning.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.learning.dao.CustomerRepository;
import com.learning.dto.PaymentInfo;
import com.learning.dto.Purchase;
import com.learning.dto.PurchaseResponse;
import com.learning.entity.Customer;
import com.learning.entity.Order;
import com.learning.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService{

	private CustomerRepository customerRepository;
	
	public CheckoutServiceImpl(CustomerRepository customerRepository, 
			@Value("${stripe.key.secret}") String secretKey) {
		this.customerRepository = customerRepository;
		
		// initialize Stripe API with secret key
		Stripe.apiKey = secretKey;
	}

	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {
	//  * retrieve the order info from dto
		Order order = purchase.getOrder();
	//	 * generate tracking number
		String orderTrackingNumber = generateOrderTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);
	//	 * populate order with orderItems
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item -> order.add(item));
	//	 * populate order with billingAddress & shippingAddress 
		order.setBillingAddress(purchase.getBillingAddress());
	//  * populate customer with order
		order.setShippingAddress(purchase.getShippingAddress());
		Customer customer = purchase.getCustomer();
		
		// check if this is existing customer 
		String theEmail = customer.getEmail();
		
		Customer customerFromDB = customerRepository.findByEmail(theEmail);
		
		if(customerFromDB != null) {
		//we found existing customer add value accordingly...
			customer = customerFromDB;
		}
		
		customer.add(order);
		//	 * save to the database
		customerRepository.save(customer);
		// * return a response 
		return new PurchaseResponse(orderTrackingNumber);
	}

	private String generateOrderTrackingNumber() {
		// generate a random UUID number(UUID version-4)
		//for details: https://en.wikipedia.org/wiki/Universally_unique_identifier
		//
		return UUID.randomUUID().toString();
	}

	@Override
	public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
		List<String> paymentMethodTypes = new ArrayList<>();
		paymentMethodTypes.add("card");
		
		Map<String, Object> params = new HashMap<>();
		
		params.put("amount", paymentInfo.getAmount());
		
		params.put("currency", paymentInfo.getCurrency());
		
		params.put("payment_method_types", paymentMethodTypes);
		
		params.put("description", "MYKART Purchase Item");
		
		params.put("receipt_email", paymentInfo.getReceiptEmail());
		
		return PaymentIntent.create(params);
	}
}
