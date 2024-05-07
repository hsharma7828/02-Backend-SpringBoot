package com.learning.dto;

import java.util.Set;

import com.learning.entity.Address;
import com.learning.entity.Customer;
import com.learning.entity.Order;
import com.learning.entity.OrderItem;

import lombok.Data;

@Data
public class Purchase {
	
	private Customer customer;
	private Address shippingAddress;
	private Address billingAddress;
	private Order order;
	private Set<OrderItem> orderItems;
}
