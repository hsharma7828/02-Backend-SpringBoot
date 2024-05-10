package com.learning.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.learning.dao.CustomerRepository;
import com.learning.dto.Purchase;
import com.learning.dto.PurchaseResponse;
import com.learning.entity.Customer;
import com.learning.entity.Order;
import com.learning.entity.OrderItem;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService{

	private CustomerRepository customerRepository;
	
	public CheckoutServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
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
}
