package com.edu.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.model.Order;
import com.edu.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderServices;
	
	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderServices.create(orderData);
	}
}
