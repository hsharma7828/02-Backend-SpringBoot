package com.learning.service;

import com.learning.dto.Purchase;
import com.learning.dto.PurchaseResponse;

public interface CheckoutService {
	PurchaseResponse placeOrder(Purchase purchase);
}
