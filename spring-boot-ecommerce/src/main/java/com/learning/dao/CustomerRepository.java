package com.learning.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.entity.Customer;

public interface CustomerRepository  extends JpaRepository<Customer, Long>{

	Customer findByEmail(String theEmail);
}
