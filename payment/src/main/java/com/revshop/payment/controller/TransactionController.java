package com.revshop.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.revshop.payment.entity.Transaction;
import com.revshop.payment.service.TransactionService;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:9090")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/createOrder")
	public ResponseEntity<Transaction> createOrder(@RequestBody Transaction orders) throws RazorpayException{
		Transaction razorpayOrder = transactionService.createOrder(orders);
		System.out.println(razorpayOrder.getOrderStatus());
		return new ResponseEntity<>(razorpayOrder, HttpStatus.CREATED);
	}
	
	@PostMapping("/paymentCallback")
	public String paymentCallback(@RequestParam Map<String, String> response) {
		// Integer status = restTemplate.postForObject("http://localhost:8095/api/v1/orders/paymentCallback", response, Integer.class);
		// if(status == 200) {			
		return "redirect:/order/history";	
		// }
		// return "notfound";
	}
}