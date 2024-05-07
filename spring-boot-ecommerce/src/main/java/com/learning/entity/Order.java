package com.learning.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "order_tracking_number")
	private String orderTrackingNumber;
	
	@Column(name = "total_quantity")
	private int totalQuantity;
	
	@Column(name = "total_price")
	private BigDecimal totalPrice;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "date_created")
	@CreationTimestamp
	private Date dateCreated;
	
	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdated;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private Set<OrderItem> orderItems = new HashSet<>();
	
	public void add(OrderItem item) {
		if(item != null) {
			if(orderItems == null) {
				orderItems = new HashSet<>();
			}
			
			orderItems.add(item);
			item.setOrder(this);
		}
	}
}
